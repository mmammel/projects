from __future__ import annotations

import threading
import time
import uuid
from dataclasses import dataclass
from datetime import datetime, timezone
from pathlib import Path

import numpy as np

from simcapture.build.manifest import (
    find_target_region,
    image_hash,
    to_patch,
    to_region,
    transition_resolution_key,
)
from simcapture.capture.screen import ScreenCapture
from simcapture.diff.detector import DiffDetector, DiffRegion
from simcapture.input.hooks import InputHookManager
from simcapture.model.session import (
    Assets,
    InputEvent,
    Manifest,
    PatchRef,
    Point,
    Region,
    SessionConfig,
    SessionInfo,
    SnippetAsset,
    StateAsset,
    Transition,
)


@dataclass
class SnippetBlob:
    id: str
    image: np.ndarray


@dataclass
class TransitionDraft:
    id: str
    from_state: str
    to_state: str
    event: InputEvent
    target_region_id: str | None
    patches: list[PatchRef]


class SessionRecorder:
    def __init__(self, config: SessionConfig, detector: DiffDetector | None = None) -> None:
        self.config = config
        self.detector = detector or DiffDetector()
        self.screen = ScreenCapture()

        self._lock = threading.Lock()
        self._process_lock = threading.Lock()
        self._running = False
        self._start_ts = 0.0

        self.events: list[InputEvent] = []
        self.regions: list[Region] = []
        self.transitions: list[TransitionDraft] = []
        self.snippets: list[SnippetBlob] = []

        self._state_frames: dict[str, np.ndarray] = {}
        self._state_hash_to_id: dict[str, str] = {}
        self._current_state = "s0"
        self._state_counter = 0
        self._region_counter = 0
        self._snippet_counter = 0
        self._transition_counter = 0

        self._hooks: InputHookManager | None = None
        self._poll_thread: threading.Thread | None = None
        self._poll_stop = threading.Event()
        self._local_event_counter = 0

    def start(self) -> None:
        with self._lock:
            if self._running:
                return
            self._running = True
            self._start_ts = time.monotonic()
            baseline = self.screen.capture_region(self.config.capture_region)
            self._register_state("s0", baseline)
            self._current_state = "s0"
            if self.config.input_mode == "global_hooks":
                try:
                    self._hooks = InputHookManager(on_event=self._on_input_event, start_ts=self._start_ts)
                    self._hooks.start()
                except Exception as exc:
                    self._running = False
                    self._hooks = None
                    raise RuntimeError(
                        "Failed to start global input hooks. Retry with safe mode enabled."
                    ) from exc
            else:
                self._poll_stop.clear()
                self._poll_thread = threading.Thread(target=self._poll_loop, daemon=True)
                self._poll_thread.start()

    def stop(self) -> ManifestBundle:
        with self._lock:
            if not self._running:
                raise RuntimeError("Session was not started")
            self._running = False
            if self._hooks is not None:
                self._hooks.stop()
                self._hooks = None
            self._poll_stop.set()
            if self._poll_thread is not None:
                self._poll_thread.join(timeout=1.0)
                self._poll_thread = None

        transitions: list[Transition] = []
        for draft in self.transitions:
            payload = draft.event.text or draft.event.key or draft.event.button
            resolution_key = transition_resolution_key(
                current_state=draft.from_state,
                event_type=draft.event.type,
                region_id=draft.target_region_id,
                modifiers=draft.event.modifiers,
                key_or_button_or_text=payload,
            )
            transitions.append(
                Transition(
                    id=draft.id,
                    from_state=draft.from_state,
                    to_state=draft.to_state,
                    trigger_event_id=draft.event.id,
                    target_region_id=draft.target_region_id,
                    patches=draft.patches,
                    resolution_key=resolution_key,
                )
            )

        state_assets = [StateAsset(state_id=sid, frame=f"assets/states/{sid}.png") for sid in self._state_frames]
        snippet_assets = [SnippetAsset(id=s.id, file=f"assets/snippets/{s.id}.png") for s in self.snippets]

        session = SessionInfo(
            id=str(uuid.uuid4()),
            name=self.config.session_name,
            created_at=datetime.now(timezone.utc),
            capture_region=self.config.capture_region,
            canvas_size=Point(x=self.config.capture_region.w, y=self.config.capture_region.h),
        )
        manifest = Manifest(
            session=session,
            assets=Assets(
                initial_frame="assets/base/state_0000.png",
                states=state_assets,
                snippets=snippet_assets,
            ),
            regions=self.regions,
            events=self.events,
            transitions=transitions,
        )
        return ManifestBundle(
            manifest=manifest,
            state_frames=self._state_frames,
            snippet_frames={s.id: s.image for s in self.snippets},
        )

    def _register_state(self, state_id: str, frame: np.ndarray) -> None:
        self._state_frames[state_id] = frame
        self._state_hash_to_id[image_hash(frame)] = state_id

    def _next_state_id(self) -> str:
        self._state_counter += 1
        return f"s{self._state_counter}"

    def _next_region_id(self) -> str:
        self._region_counter += 1
        return f"r{self._region_counter}"

    def _next_snippet_id(self) -> str:
        self._snippet_counter += 1
        return f"sn_{self._snippet_counter:04d}"

    def _next_transition_id(self) -> str:
        self._transition_counter += 1
        return f"t{self._transition_counter}"

    def _next_local_event_id(self) -> str:
        self._local_event_counter += 1
        return f"lp{self._local_event_counter}"

    def _poll_loop(self) -> None:
        while not self._poll_stop.is_set():
            event = InputEvent(
                id=self._next_local_event_id(),
                ts_ms=int((time.monotonic() - self._start_ts) * 1000),
                type="mouse_move",
            )
            self._on_input_event(event)
            self._poll_stop.wait(self.config.settle_ms / 1000.0)

    def _on_input_event(self, event: InputEvent) -> None:
        with self._process_lock:
            self._process_event_locked(event)

    def _process_event_locked(self, event: InputEvent) -> None:
        with self._lock:
            if not self._running:
                return

            self.events.append(event)
            before = self._state_frames[self._current_state]
            from_state = self._current_state

        settle_start = time.monotonic()
        time.sleep(self.config.settle_ms / 1000.0)
        after = self.screen.capture_region(self.config.capture_region)
        retry_count = 0
        while retry_count < self.config.retry_on_instability:
            elapsed_ms = (time.monotonic() - settle_start) * 1000
            if elapsed_ms >= self.config.max_settle_ms:
                break
            time.sleep(self.config.settle_ms / 1000.0)
            next_after = self.screen.capture_region(self.config.capture_region)
            unstable_regions = self.detector.detect(before=after, after=next_after)
            after = next_after
            retry_count += 1
            if not unstable_regions:
                break

        regions = self.detector.detect(before=before, after=after)
        if not regions:
            return

        with self._lock:
            if not self._running:
                return

            state_hash = image_hash(after)
            to_state = self._state_hash_to_id.get(state_hash)
            if to_state is None:
                to_state = self._next_state_id()
                self._register_state(to_state, after)

            patches: list[PatchRef] = []
            for box in regions:
                region_id = self._find_or_add_region(box)
                snippet_id = self._next_snippet_id()
                snippet = after[box.y : box.y + box.h, box.x : box.x + box.w].copy()
                self.snippets.append(SnippetBlob(id=snippet_id, image=snippet))
                patches.append(to_patch(snippet_id, box))

            target_region_id = find_target_region(
                self.regions,
                x=(event.pos.x - self.config.capture_region.x) if event.pos else None,
                y=(event.pos.y - self.config.capture_region.y) if event.pos else None,
            )

            self.transitions.append(
                TransitionDraft(
                    id=self._next_transition_id(),
                    from_state=from_state,
                    to_state=to_state,
                    event=event,
                    target_region_id=target_region_id,
                    patches=patches,
                )
            )
            self._current_state = to_state

    def _find_or_add_region(self, box: DiffRegion) -> str:
        for region in self.regions:
            if (
                abs(region.x - box.x) <= 2
                and abs(region.y - box.y) <= 2
                and abs(region.w - box.w) <= 2
                and abs(region.h - box.h) <= 2
            ):
                return region.id

        region_id = self._next_region_id()
        self.regions.append(to_region(region_id, box))
        return region_id


@dataclass
class ManifestBundle:
    manifest: Manifest
    state_frames: dict[str, np.ndarray]
    snippet_frames: dict[str, np.ndarray]

    @property
    def output_dir(self) -> Path:
        return Path(self.manifest.session.name)
