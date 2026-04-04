from __future__ import annotations

import time
import uuid
from dataclasses import dataclass, field

import numpy as np

from simcapture.build.manifest import find_target_region, image_hash, to_patch, transition_resolution_key
from simcapture.capture.screen import ScreenCapture
from simcapture.diff.detector import DiffDetector
from simcapture.model.session import (
    Assets,
    InputEvent,
    Manifest,
    OverlayRegion,
    PatchRef,
    Point,
    SessionConfig,
    SessionInfo,
    SnippetAsset,
    StateAsset,
    StateNode,
    Transition,
)


@dataclass
class PendingTransition:
    state_id: str
    parent_state_id: str
    event: InputEvent
    frame: np.ndarray
    patches: list[PatchRef]
    snippets: dict[str, np.ndarray]


@dataclass
class SessionProject:
    config: SessionConfig
    screen: ScreenCapture = field(default_factory=ScreenCapture)
    detector: DiffDetector = field(default_factory=DiffDetector)
    state_frames: dict[str, np.ndarray] = field(default_factory=dict)
    states: dict[str, StateNode] = field(default_factory=dict)
    overlays_by_state: dict[str, list[OverlayRegion]] = field(default_factory=dict)
    events: list[InputEvent] = field(default_factory=list)
    transitions: list[Transition] = field(default_factory=list)
    snippet_frames: dict[str, np.ndarray] = field(default_factory=dict)
    pending_transition: PendingTransition | None = None
    current_state_id: str | None = None
    _event_counter: int = 0
    _overlay_counter: int = 0
    _state_counter: int = 0
    _transition_counter: int = 0
    _snippet_counter: int = 0
    _pending_target_region_id: str | None = None
    _pending_payload: str | None = None

    def capture_current_frame(self) -> np.ndarray:
        return self.screen.capture_region(self.config.capture_region)

    def ensure_root_state(self, frame: np.ndarray) -> str:
        if "s0" in self.states:
            return "s0"
        state = StateNode(id="s0", parent_state_id=None, title="State 0", frame="assets/states/s0.png")
        self.states[state.id] = state
        self.state_frames[state.id] = frame
        self.overlays_by_state[state.id] = []
        self.current_state_id = state.id
        self._state_counter = 0
        return state.id

    def set_current_state(self, state_id: str) -> None:
        if state_id not in self.states:
            raise KeyError(f"Unknown state id: {state_id}")
        self.current_state_id = state_id

    def frame_for_state(self, state_id: str) -> np.ndarray:
        return self.state_frames[state_id]

    def overlays_for_state(self, state_id: str) -> list[OverlayRegion]:
        return list(self.overlays_by_state.get(state_id, []))

    def replace_overlays(self, state_id: str, overlays: list[OverlayRegion]) -> None:
        normalized: list[OverlayRegion] = []
        for overlay in overlays:
            normalized.append(
                OverlayRegion(
                    id=overlay.id,
                    state_id=state_id,
                    x=overlay.x,
                    y=overlay.y,
                    w=max(1, overlay.w),
                    h=max(1, overlay.h),
                    label=overlay.label,
                    z_index=overlay.z_index,
                )
            )
        normalized.sort(key=lambda overlay: (overlay.z_index, overlay.w * overlay.h))
        self.overlays_by_state[state_id] = normalized

    def new_overlay(self, state_id: str, x: int, y: int, w: int, h: int, z_index: int = 0) -> OverlayRegion:
        self._overlay_counter += 1
        return OverlayRegion(
            id=f"r{self._overlay_counter}",
            state_id=state_id,
            x=x,
            y=y,
            w=max(1, w),
            h=max(1, h),
            z_index=z_index,
        )

    def auto_cut_overlays(self, state_id: str, frame: np.ndarray) -> list[OverlayRegion]:
        boxes = self._detect_candidate_regions(frame)
        overlays = [
            self.new_overlay(state_id, box[0], box[1], box[2], box[3], z_index=box[4])
            for box in boxes
        ]
        self.replace_overlays(state_id, overlays)
        self.state_frames[state_id] = frame
        return overlays

    def _detect_candidate_regions(self, frame: np.ndarray) -> list[tuple[int, int, int, int, int]]:
        try:
            import cv2
        except ImportError:
            cv2 = None

        if cv2 is None:
            h, w = frame.shape[:2]
            return [(0, 0, w, h, 0)]

        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
        edges = cv2.Canny(gray, 40, 120)
        kernel = np.ones((3, 3), np.uint8)
        edges = cv2.dilate(edges, kernel, iterations=1)
        contours, hierarchy = cv2.findContours(edges, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
        hierarchy = hierarchy[0] if hierarchy is not None else []

        candidates: list[tuple[int, int, int, int, int, int]] = []
        for index, contour in enumerate(contours):
            x, y, w, h = cv2.boundingRect(contour)
            area = w * h
            if area < 100 or w < 8 or h < 8:
                continue
            if area > int(frame.shape[0] * frame.shape[1] * 0.9):
                continue
            depth = self._hierarchy_depth(index, hierarchy)
            score = area
            candidates.append((x, y, w, h, depth, score))

        candidates.sort(key=lambda item: (item[4], -(item[2] * item[3]), item[1], item[0]))
        accepted: list[tuple[int, int, int, int, int]] = []
        for x, y, w, h, depth, _ in candidates:
            candidate = (x, y, w, h, depth)
            if any(self._is_duplicate(candidate, other) for other in accepted):
                continue
            accepted.append(candidate)
            if len(accepted) >= 80:
                break

        accepted.sort(key=lambda item: (item[4], item[1], item[0], item[2] * item[3]))
        return accepted

    def _is_duplicate(
        self,
        candidate: tuple[int, int, int, int, int],
        existing: tuple[int, int, int, int, int],
    ) -> bool:
        cx, cy, cw, ch, _ = candidate
        ex, ey, ew, eh, _ = existing
        ix0 = max(cx, ex)
        iy0 = max(cy, ey)
        ix1 = min(cx + cw, ex + ew)
        iy1 = min(cy + ch, ey + eh)
        if ix1 <= ix0 or iy1 <= iy0:
            return False
        overlap = (ix1 - ix0) * (iy1 - iy0)
        return overlap / float(cw * ch) > 0.92 and overlap / float(ew * eh) > 0.92

    def _hierarchy_depth(self, index: int, hierarchy) -> int:
        depth = 0
        parent = hierarchy[index][3] if len(hierarchy) > index else -1
        while parent != -1:
            depth += 1
            parent = hierarchy[parent][3]
        return depth

    def start_action_recording(self) -> float:
        return time.monotonic()

    def stop_action_recording(
        self,
        started_at: float,
        recorded_events: list[InputEvent],
        final_frame: np.ndarray,
    ) -> PendingTransition | None:
        if self.current_state_id is None:
            return None
        if not recorded_events:
            return None

        parent_state_id = self.current_state_id
        before = self.state_frames[parent_state_id]
        regions = self.detector.detect(before=before, after=final_frame)
        if not regions:
            return None

        event = self._choose_primary_event(recorded_events)
        overlays = self.overlays_by_state.get(parent_state_id, [])
        target_region_id = find_target_region(
            overlays,
            x=(event.pos.x if event.pos else None),
            y=(event.pos.y if event.pos else None),
        )

        snippets: dict[str, np.ndarray] = {}
        patches: list[PatchRef] = []
        for region in regions:
            snippet_id = self._next_snippet_id()
            snippet = final_frame[region.y : region.y + region.h, region.x : region.x + region.w].copy()
            snippets[snippet_id] = snippet
            patches.append(to_patch(snippet_id, region))

        payload = event.text or event.key or event.button
        event_record = InputEvent(
            id=self._next_event_id(),
            ts_ms=int((time.monotonic() - started_at) * 1000),
            type=event.type,
            pos=event.pos,
            button=event.button,
            key=event.key,
            modifiers=event.modifiers,
            text=event.text,
        )
        self.events.append(event_record)

        frame_hash = image_hash(final_frame)
        existing_state_id: str | None = None
        for state_id, frame in self.state_frames.items():
            if image_hash(frame) == frame_hash:
                existing_state_id = state_id
                break

        if existing_state_id is None:
            self._state_counter += 1
            state_id = f"s{self._state_counter}"
            self.states[state_id] = StateNode(
                id=state_id,
                parent_state_id=parent_state_id,
                title=f"State {self._state_counter}",
                frame=f"assets/states/{state_id}.png",
            )
            self.state_frames[state_id] = final_frame
            self.overlays_by_state[state_id] = []
        else:
            state_id = existing_state_id

        for snippet_id, snippet in snippets.items():
            self.snippet_frames[snippet_id] = snippet

        resolution_key = transition_resolution_key(
            current_state=parent_state_id,
            event_type=event_record.type,
            region_id=target_region_id,
            modifiers=event_record.modifiers,
            key_or_button_or_text=payload,
        )
        self._transition_counter += 1
        self.transitions.append(
            Transition(
                id=f"t{self._transition_counter}",
                from_state=parent_state_id,
                to_state=state_id,
                trigger_event_id=event_record.id,
                target_region_id=target_region_id,
                patches=patches,
                resolution_key=resolution_key,
            )
        )

        pending = PendingTransition(
            state_id=state_id,
            parent_state_id=parent_state_id,
            event=event_record,
            frame=final_frame,
            patches=patches,
            snippets=snippets,
        )
        self.pending_transition = pending
        self._pending_target_region_id = target_region_id
        self._pending_payload = payload
        self.current_state_id = state_id
        return pending

    def finalize_pending_state(self) -> str | None:
        if self.pending_transition is None:
            return None
        state_id = self.pending_transition.state_id
        self.current_state_id = state_id
        self.pending_transition = None
        return state_id

    def finalize_current_state(self) -> str | None:
        if self.current_state_id is None:
            return None
        return self.current_state_id

    def _choose_primary_event(self, events: list[InputEvent]) -> InputEvent:
        for preferred in ("mouse_down", "mouse_up", "mouse_move", "key_down", "text_input"):
            for event in events:
                if event.type == preferred:
                    return event
        return events[-1]

    def _next_event_id(self) -> str:
        self._event_counter += 1
        return f"e{self._event_counter}"

    def _next_snippet_id(self) -> str:
        self._snippet_counter += 1
        return f"sn_{self._snippet_counter:04d}"

    def build_manifest(self) -> Manifest:
        if "s0" not in self.states:
            raise RuntimeError("No root state has been defined.")

        state_assets = [StateAsset(state_id=state_id, frame=f"assets/states/{state_id}.png") for state_id in self.states]
        snippet_assets = [SnippetAsset(id=snippet_id, file=f"assets/snippets/{snippet_id}.png") for snippet_id in self.snippet_frames]
        session = SessionInfo(
            id=str(uuid.uuid4()),
            name=self.config.session_name,
            capture_region=self.config.capture_region,
            canvas_size=Point(x=self.config.capture_region.w, y=self.config.capture_region.h),
        )

        all_regions: list[OverlayRegion] = []
        for state_id in self.states:
            all_regions.extend(self.overlays_by_state.get(state_id, []))

        return Manifest(
            session=session,
            assets=Assets(
                initial_frame="assets/base/state_0000.png",
                states=state_assets,
                snippets=snippet_assets,
            ),
            states=list(self.states.values()),
            regions=all_regions,
            events=self.events,
            transitions=self.transitions,
        )
