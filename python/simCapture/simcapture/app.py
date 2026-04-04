from __future__ import annotations

import time
import traceback
from dataclasses import dataclass
from pathlib import Path

import numpy as np
from PySide6.QtCore import QTimer
from PySide6.QtWidgets import QApplication, QMessageBox

from simcapture.build.bundle import write_bundle
from simcapture.editor import PendingTransition, SessionProject
from simcapture.input.hooks import InputHookManager
from simcapture.model.session import EditorMode, InputEvent
from simcapture.platform import is_macos
from simcapture.ui.overlay import OverlayWindow


@dataclass
class BundlePayload:
    manifest: object
    state_frames: dict[str, np.ndarray]
    snippet_frames: dict[str, np.ndarray]


class SimCaptureApplication:
    def __init__(self) -> None:
        self.qt_app = QApplication([])
        self.window = OverlayWindow()
        self.window.request_capture_root.connect(self._capture_root_state)
        self.window.request_auto_cut.connect(self._auto_cut)
        self.window.request_refresh_mapping.connect(self._refresh_mapping_frame)
        self.window.request_record_start.connect(self._start_recording)
        self.window.request_record_stop.connect(self._stop_recording)
        self.window.request_finalize_state.connect(self._finalize_pending_state)
        self.window.request_export.connect(self._export_bundle)
        self.window.state_selected.connect(self._select_state)
        self.window.request_set_mode.connect(self._set_mode)
        self.window.request_add_overlay.connect(self._add_overlay)
        self.window.request_remove_overlay.connect(self._remove_overlay)
        self.window.capture_frame.overlay_changed.connect(self._sync_overlays_from_frame)

        self.project: SessionProject | None = None
        self._mode: EditorMode = "map"
        self._recording_started_at: float | None = None
        self._recorded_events: list[InputEvent] = []
        self._hooks: InputHookManager | None = None
        self._poll_timer = QTimer()
        self._poll_timer.setInterval(30)
        self._poll_timer.timeout.connect(self._poll_safe_input)
        self._last_mouse_buttons = 0
        self._last_mouse_pos: tuple[int, int] | None = None

    def _ensure_project(self) -> SessionProject:
        if self.project is None:
            self.project = SessionProject(self.window.session_config())
        else:
            self.project.config = self.window.session_config()
        return self.project

    def _capture_root_state(self, config) -> None:
        try:
            self.project = SessionProject(config)
            frame = self.project.capture_current_frame()
            root_id = self.project.ensure_root_state(frame)
            self.window.load_state_frame(frame, self.project.overlays_for_state(root_id))
            self.window.update_tree(list(self.project.states.values()), root_id)
            self.window.set_mode("map")
            self._mode = "map"
        except Exception as exc:
            self._show_error("Failed to capture root state", exc)

    def _refresh_mapping_frame(self) -> None:
        if self.project is None or self.project.current_state_id is None:
            return
        try:
            frame = self.project.capture_current_frame()
            self.project.state_frames[self.project.current_state_id] = frame
            overlays = self.project.overlays_for_state(self.project.current_state_id)
            self.window.load_state_frame(frame, overlays)
        except Exception as exc:
            self._show_error("Failed to refresh mapping frame", exc)

    def _auto_cut(self) -> None:
        if self.project is None or self.project.current_state_id is None:
            return
        try:
            frame = self.project.capture_current_frame()
            overlays = self.project.auto_cut_overlays(self.project.current_state_id, frame)
            self.window.load_state_frame(frame, overlays)
        except Exception as exc:
            self._show_error("Failed to auto-cut overlays", exc)

    def _sync_overlays_from_frame(self) -> None:
        if self.project is None or self.project.current_state_id is None:
            return
        self.project.replace_overlays(self.project.current_state_id, self.window.capture_frame.overlays())

    def _add_overlay(self, x: int, y: int, parent_overlay_id: str) -> None:
        if self.project is None or self.project.current_state_id is None:
            return
        z_index = 0
        if parent_overlay_id:
            for existing in self.project.overlays_for_state(self.project.current_state_id):
                if existing.id == parent_overlay_id:
                    z_index = existing.z_index + 1
                    break
        overlay = self.project.new_overlay(self.project.current_state_id, x, y, 80, 40, z_index=z_index)
        overlays = self.project.overlays_for_state(self.project.current_state_id)
        overlays.append(overlay)
        self.project.replace_overlays(self.project.current_state_id, overlays)
        self.window.load_state_frame(self.project.frame_for_state(self.project.current_state_id), overlays)

    def _remove_overlay(self, overlay_id: str) -> None:
        if self.project is None or self.project.current_state_id is None:
            return
        overlays = [overlay for overlay in self.project.overlays_for_state(self.project.current_state_id) if overlay.id != overlay_id]
        self.project.replace_overlays(self.project.current_state_id, overlays)
        self.window.load_state_frame(self.project.frame_for_state(self.project.current_state_id), overlays)

    def _set_mode(self, mode: str) -> None:
        self._mode = "map" if mode == "map" else "action"
        self.window.set_mode(self._mode)
        if self.project is None or self.project.current_state_id is None:
            return
        frame = self.project.frame_for_state(self.project.current_state_id) if self._mode == "map" else None
        overlays = self.project.overlays_for_state(self.project.current_state_id) if self._mode == "map" else []
        self.window.load_state_frame(frame, overlays)

    def _start_recording(self) -> None:
        if self.project is None or self.project.current_state_id is None:
            return
        try:
            self._recorded_events = []
            self._recording_started_at = self.project.start_action_recording()
            self._last_mouse_buttons = 0
            self._last_mouse_pos = None
            if not self.project.config.input_mode == "safe_polling" and not is_macos():
                self._hooks = InputHookManager(on_event=self._capture_event, start_ts=self._recording_started_at)
                self._hooks.start()
            else:
                self._hooks = None
                self._poll_timer.start()
            self.window.set_recording(True)
            self.window.showMinimized()
        except Exception as exc:
            self._show_error("Failed to start action recording", exc)

    def _capture_event(self, event: InputEvent) -> None:
        if self.project is None:
            return
        if event.pos is not None:
            event.pos.x -= self.project.config.capture_region.x
            event.pos.y -= self.project.config.capture_region.y
        self._recorded_events.append(event)

    def _stop_recording(self) -> None:
        if self.project is None or self._recording_started_at is None:
            return
        try:
            self._poll_timer.stop()
            if self._hooks is not None:
                self._hooks.stop()
                self._hooks = None
            frame = self.project.capture_current_frame()
            if self._hooks is None and not self._recorded_events:
                self._recorded_events.append(
                    InputEvent(
                        id="safe_polling_event",
                        ts_ms=0,
                        type="mouse_down",
                    )
                )
            pending = self.project.stop_action_recording(self._recording_started_at, self._recorded_events, frame)
            self.window.set_recording(False)
            self._recording_started_at = None
            self._recorded_events = []
            self.window.set_mode("map")
            self._mode = "map"
            if pending is None:
                QMessageBox.information(self.window, "simCapture", "No changed state was detected.")
                self.window.load_state_frame(self.project.frame_for_state(self.project.current_state_id), self.project.overlays_for_state(self.project.current_state_id))
                return
            self._load_pending_state(pending)
        except Exception as exc:
            self._show_error("Failed to stop action recording", exc)

    def _load_pending_state(self, pending: PendingTransition) -> None:
        self.window.update_tree(list(self.project.states.values()), pending.state_id)
        self.window.load_state_frame(pending.frame, [])
        QMessageBox.information(
            self.window,
            "simCapture",
            "Transition recorded. You are back in Mode 1 on the new image. Add/adjust overlays, then click Finalize State.",
        )

    def _finalize_pending_state(self) -> None:
        if self.project is None:
            return
        try:
            had_pending = self.project.pending_transition is not None
            state_id = self.project.finalize_pending_state()
            if state_id is None:
                state_id = self.project.finalize_current_state()
                if state_id is None:
                    QMessageBox.information(self.window, "simCapture", "No state is available to finalize.")
                    return
            overlays = self.window.capture_frame.overlays()
            self.project.replace_overlays(state_id, overlays)
            self.window.update_tree(list(self.project.states.values()), state_id)
            self.window.load_state_frame(self.project.frame_for_state(state_id), overlays)
            if not had_pending:
                QMessageBox.information(self.window, "simCapture", f"{state_id} finalized.")
        except Exception as exc:
            self._show_error("Failed to finalize state", exc)

    def _select_state(self, state_id: str) -> None:
        if self.project is None:
            return
        self.project.set_current_state(state_id)
        frame = self.project.frame_for_state(state_id) if self._mode == "map" else None
        overlays = self.project.overlays_for_state(state_id) if self._mode == "map" else []
        self.window.load_state_frame(frame, overlays)

    def _export_bundle(self) -> None:
        if self.project is None:
            return
        try:
            manifest = self.project.build_manifest()
            output_dir = Path(self.project.config.output_dir) / self.project.config.session_name
            payload = BundlePayload(
                manifest=manifest,
                state_frames=self.project.state_frames,
                snippet_frames=self.project.snippet_frames,
            )
            write_bundle(payload, output_dir)
            QMessageBox.information(self.window, "simCapture", f"Bundle written to:\n{output_dir}")
        except Exception as exc:
            self._show_error("Failed to export bundle", exc)

    def _show_error(self, title: str, exc: Exception) -> None:
        QMessageBox.critical(self.window, "simCapture", f"{title}:\n{exc}\n\n{traceback.format_exc(limit=5)}")

    def _poll_safe_input(self) -> None:
        if self.project is None or self._recording_started_at is None:
            return
        try:
            from PySide6.QtGui import QCursor

            global_pos = QCursor.pos()
            local_x = global_pos.x() - self.project.config.capture_region.x
            local_y = global_pos.y() - self.project.config.capture_region.y
            inside = (
                0 <= local_x < self.project.config.capture_region.w
                and 0 <= local_y < self.project.config.capture_region.h
            )
            pos_tuple = (local_x, local_y)
            if inside and pos_tuple != self._last_mouse_pos:
                self._recorded_events.append(
                    InputEvent(
                        id=f"poll_move_{len(self._recorded_events)}",
                        ts_ms=int((time.monotonic() - self._recording_started_at) * 1000),
                        type="mouse_move",
                        pos={"x": local_x, "y": local_y},
                    )
                )
                self._last_mouse_pos = pos_tuple

            buttons = self._pressed_mouse_buttons()
            if buttons != self._last_mouse_buttons:
                event_type = "mouse_down" if buttons else "mouse_up"
                button_name = self._button_name(buttons or self._last_mouse_buttons)
                self._recorded_events.append(
                    InputEvent(
                        id=f"poll_click_{len(self._recorded_events)}",
                        ts_ms=int((time.monotonic() - self._recording_started_at) * 1000),
                        type=event_type,
                        pos={"x": local_x, "y": local_y} if inside else None,
                        button=button_name,
                    )
                )
                self._last_mouse_buttons = buttons
        except Exception:
            self._poll_timer.stop()

    def _pressed_mouse_buttons(self) -> int:
        if is_macos():
            from AppKit import NSEvent

            return int(NSEvent.pressedMouseButtons())
        return 0

    def _button_name(self, button_mask: int) -> str:
        if button_mask & 1:
            return "left"
        if button_mask & 2:
            return "right"
        if button_mask & 4:
            return "middle"
        return "left"

    def run(self) -> int:
        self.window.show()
        return self.qt_app.exec()
