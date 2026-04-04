from __future__ import annotations

from pathlib import Path

import numpy as np
from PySide6.QtCore import QPoint, QRect, QSettings, Qt, Signal
from PySide6.QtGui import QAction, QColor, QCursor, QImage, QPainter, QPen, QPixmap
from PySide6.QtWidgets import (
    QCheckBox,
    QFileDialog,
    QHBoxLayout,
    QLabel,
    QLineEdit,
    QMenu,
    QPushButton,
    QSpinBox,
    QTreeWidget,
    QTreeWidgetItem,
    QVBoxLayout,
    QWidget,
)

from simcapture.model.session import EditorMode, OverlayRegion, Rect, SessionConfig, StateNode
from simcapture.platform import platform_health_warnings


class CaptureFrame(QWidget):
    overlay_changed = Signal()
    context_add_overlay = Signal(int, int, str)
    context_remove_overlay = Signal(str)

    def __init__(self) -> None:
        super().__init__()
        self.setWindowTitle("simCapture Capture Frame")
        self.setWindowFlags(Qt.WindowType.Window | Qt.WindowType.FramelessWindowHint | Qt.WindowType.WindowStaysOnTopHint)
        self.setAttribute(Qt.WidgetAttribute.WA_TranslucentBackground, True)
        self.setMouseTracking(True)
        self.setMinimumSize(240, 160)
        self.resize(1280, 800)

        self._border = 3
        self._handle = 10
        self._mode = "move_frame"
        self._drag_start = QPoint()
        self._start_geom = QRect()
        self._edges = (False, False, False, False)
        self._frame_clickthrough = False

        self._editor_mode: EditorMode = "map"
        self._background: QPixmap | None = None
        self._overlays: list[OverlayRegion] = []
        self._selected_overlay_id: str | None = None
        self._overlay_drag_origin = QPoint()
        self._overlay_start_rect: QRect | None = None
        self._overlay_resize_edges = (False, False, False, False)

    def set_editor_mode(self, mode: EditorMode) -> None:
        self._editor_mode = mode
        self.set_clickthrough(mode == "action")
        self.update()

    def set_background_frame(self, frame: np.ndarray | None) -> None:
        if frame is None:
            self._background = None
            self.update()
            return
        rgb = frame[:, :, ::-1].copy()
        height, width = rgb.shape[:2]
        image = QImage(rgb.data, width, height, width * 3, QImage.Format.Format_RGB888)
        self._background = QPixmap.fromImage(image.copy())
        self.update()

    def set_overlays(self, overlays: list[OverlayRegion]) -> None:
        self._overlays = sorted(overlays, key=lambda overlay: (overlay.z_index, overlay.w * overlay.h))
        if self._selected_overlay_id and not any(o.id == self._selected_overlay_id for o in self._overlays):
            self._selected_overlay_id = None
        self.update()

    def overlays(self) -> list[OverlayRegion]:
        return list(self._overlays)

    def paintEvent(self, event) -> None:  # type: ignore[override]
        painter = QPainter(self)
        painter.setRenderHint(QPainter.RenderHint.Antialiasing)

        if self._editor_mode == "map" and self._background is not None:
            painter.drawPixmap(self.inner_rect(), self._background, self._background.rect())

        painter.setBrush(Qt.BrushStyle.NoBrush)
        border_color = QColor(16, 185, 129, 230)
        painter.setPen(QPen(border_color, self._border))
        painter.drawRect(self.rect().adjusted(self._border, self._border, -self._border, -self._border))

        if self._editor_mode == "map":
            for overlay in self._overlays:
                rect = self._overlay_rect(overlay)
                is_selected = overlay.id == self._selected_overlay_id
                painter.setPen(QPen(QColor(245, 158, 11, 230) if is_selected else QColor(56, 189, 248, 230), 2))
                painter.setBrush(QColor(56, 189, 248, 30) if not is_selected else QColor(245, 158, 11, 40))
                painter.drawRect(rect)

    def inner_rect(self) -> QRect:
        inset = self._border + 2
        return self.rect().adjusted(inset, inset, -inset, -inset)

    def capture_rect(self) -> Rect:
        local = self.inner_rect()
        top_left = self.mapToGlobal(local.topLeft())
        return Rect(x=top_left.x(), y=top_left.y(), w=local.width(), h=local.height())

    def set_clickthrough(self, enabled: bool) -> None:
        if self._frame_clickthrough == enabled:
            return
        self._frame_clickthrough = enabled
        self.setWindowFlag(Qt.WindowType.WindowTransparentForInput, enabled)
        self.show()
        self.update()

    def _edge_flags(self, pos: QPoint) -> tuple[bool, bool, bool, bool]:
        x = pos.x()
        y = pos.y()
        w = self.width()
        h = self.height()
        return (x <= self._handle, x >= w - self._handle, y <= self._handle, y >= h - self._handle)

    def _cursor_for_edges(self, edges: tuple[bool, bool, bool, bool]) -> Qt.CursorShape:
        left, right, top, bottom = edges
        if (left and top) or (right and bottom):
            return Qt.CursorShape.SizeFDiagCursor
        if (right and top) or (left and bottom):
            return Qt.CursorShape.SizeBDiagCursor
        if left or right:
            return Qt.CursorShape.SizeHorCursor
        if top or bottom:
            return Qt.CursorShape.SizeVerCursor
        return Qt.CursorShape.SizeAllCursor

    def _overlay_rect(self, overlay: OverlayRegion) -> QRect:
        inner = self.inner_rect()
        return QRect(inner.x() + overlay.x, inner.y() + overlay.y, overlay.w, overlay.h)

    def _overlay_at(self, pos: QPoint) -> OverlayRegion | None:
        for overlay in reversed(self._overlays):
            if self._overlay_rect(overlay).contains(pos):
                return overlay
        return None

    def _overlay_corner_flags(self, overlay: OverlayRegion, pos: QPoint) -> tuple[bool, bool, bool, bool]:
        rect = self._overlay_rect(overlay)
        return (
            abs(pos.x() - rect.left()) <= self._handle and abs(pos.y() - rect.top()) <= self._handle,
            abs(pos.x() - rect.right()) <= self._handle and abs(pos.y() - rect.top()) <= self._handle,
            abs(pos.x() - rect.left()) <= self._handle and abs(pos.y() - rect.bottom()) <= self._handle,
            abs(pos.x() - rect.right()) <= self._handle and abs(pos.y() - rect.bottom()) <= self._handle,
        )

    def mousePressEvent(self, event) -> None:  # type: ignore[override]
        if self._frame_clickthrough:
            return
        pos = event.position().toPoint()
        if event.button() == Qt.MouseButton.RightButton and self._editor_mode == "map":
            self._show_context_menu(event.globalPosition().toPoint(), pos)
            return
        if event.button() != Qt.MouseButton.LeftButton:
            return

        self._drag_start = event.globalPosition().toPoint()
        self._start_geom = self.geometry()

        if self._editor_mode == "map":
            overlay = self._overlay_at(pos)
            if overlay is not None:
                self._selected_overlay_id = overlay.id
                self._overlay_drag_origin = pos
                self._overlay_start_rect = self._overlay_rect(overlay)
                self._overlay_resize_edges = self._overlay_corner_flags(overlay, pos)
                self._mode = "resize_overlay" if any(self._overlay_resize_edges) else "move_overlay"
                self.update()
                return
            self._selected_overlay_id = None

        self._edges = self._edge_flags(pos)
        self._mode = "resize_frame" if any(self._edges) else "move_frame"
        self.update()

    def mouseMoveEvent(self, event) -> None:  # type: ignore[override]
        if self._frame_clickthrough:
            return
        pos = event.position().toPoint()
        global_pos = event.globalPosition().toPoint()

        if self._mode == "move_overlay":
            self._move_selected_overlay(pos)
            return
        if self._mode == "resize_overlay":
            self._resize_selected_overlay(pos)
            return
        if self._mode == "move_frame":
            if event.buttons() & Qt.MouseButton.LeftButton:
                self.move(self._start_geom.topLeft() + (global_pos - self._drag_start))
            else:
                self.setCursor(QCursor(Qt.CursorShape.SizeAllCursor))
            return
        if self._mode == "resize_frame":
            if event.buttons() & Qt.MouseButton.LeftButton:
                self._resize_frame(global_pos)
            else:
                self.setCursor(QCursor(self._cursor_for_edges(self._edge_flags(pos))))
            return

        if self._editor_mode == "map":
            overlay = self._overlay_at(pos)
            if overlay is not None:
                corners = self._overlay_corner_flags(overlay, pos)
                if any(corners):
                    self.setCursor(QCursor(self._cursor_for_overlay_corners(corners)))
                else:
                    self.setCursor(QCursor(Qt.CursorShape.SizeAllCursor))
                return
        edges = self._edge_flags(pos)
        self.setCursor(QCursor(self._cursor_for_edges(edges) if any(edges) else Qt.CursorShape.SizeAllCursor))

    def mouseReleaseEvent(self, event) -> None:  # type: ignore[override]
        if self._frame_clickthrough:
            return
        self._mode = "move_frame"
        self._overlay_start_rect = None

    def _resize_frame(self, global_pos: QPoint) -> None:
        left, right, top, bottom = self._edges
        g = QRect(self._start_geom)
        delta = global_pos - self._drag_start
        min_w = self.minimumWidth()
        min_h = self.minimumHeight()
        if left:
            g.setLeft(min(g.right() - min_w, g.left() + delta.x()))
        if right:
            g.setRight(max(g.left() + min_w, g.right() + delta.x()))
        if top:
            g.setTop(min(g.bottom() - min_h, g.top() + delta.y()))
        if bottom:
            g.setBottom(max(g.top() + min_h, g.bottom() + delta.y()))
        self.setGeometry(g)

    def _move_selected_overlay(self, pos: QPoint) -> None:
        overlay = self._selected_overlay()
        if overlay is None or self._overlay_start_rect is None:
            return
        delta = pos - self._overlay_drag_origin
        rect = QRect(self._overlay_start_rect)
        rect.moveTo(rect.x() + delta.x(), rect.y() + delta.y())
        self._apply_overlay_rect(overlay.id, rect)

    def _resize_selected_overlay(self, pos: QPoint) -> None:
        overlay = self._selected_overlay()
        if overlay is None or self._overlay_start_rect is None:
            return
        top_left, top_right, bottom_left, bottom_right = self._overlay_resize_edges
        rect = QRect(self._overlay_start_rect)
        delta = pos - self._overlay_drag_origin
        if top_left or bottom_left:
            rect.setLeft(min(rect.right() - 12, rect.left() + delta.x()))
        if top_right or bottom_right:
            rect.setRight(max(rect.left() + 12, rect.right() + delta.x()))
        if top_left or top_right:
            rect.setTop(min(rect.bottom() - 12, rect.top() + delta.y()))
        if bottom_left or bottom_right:
            rect.setBottom(max(rect.top() + 12, rect.bottom() + delta.y()))
        self._apply_overlay_rect(overlay.id, rect)

    def _cursor_for_overlay_corners(self, corners: tuple[bool, bool, bool, bool]) -> Qt.CursorShape:
        top_left, top_right, bottom_left, bottom_right = corners
        if top_left or bottom_right:
            return Qt.CursorShape.SizeFDiagCursor
        if top_right or bottom_left:
            return Qt.CursorShape.SizeBDiagCursor
        return Qt.CursorShape.SizeAllCursor

    def _apply_overlay_rect(self, overlay_id: str, rect: QRect) -> None:
        inner = self.inner_rect()
        bounded = rect.intersected(inner)
        if bounded.width() < 12 or bounded.height() < 12:
            return
        updated: list[OverlayRegion] = []
        for overlay in self._overlays:
            if overlay.id == overlay_id:
                updated.append(
                    OverlayRegion(
                        id=overlay.id,
                        state_id=overlay.state_id,
                        x=bounded.x() - inner.x(),
                        y=bounded.y() - inner.y(),
                        w=bounded.width(),
                        h=bounded.height(),
                        label=overlay.label,
                        z_index=overlay.z_index,
                    )
                )
            else:
                updated.append(overlay)
        self._overlays = updated
        self.overlay_changed.emit()
        self.update()

    def _selected_overlay(self) -> OverlayRegion | None:
        if self._selected_overlay_id is None:
            return None
        for overlay in self._overlays:
            if overlay.id == self._selected_overlay_id:
                return overlay
        return None

    def _show_context_menu(self, global_pos: QPoint, local_pos: QPoint) -> None:
        menu = QMenu(self)
        overlay = self._overlay_at(local_pos)
        add_action = QAction("Add overlay", self)
        add_action.triggered.connect(lambda: self._emit_add_overlay(local_pos, overlay.id if overlay is not None else ""))
        menu.addAction(add_action)
        if overlay is not None:
            self._selected_overlay_id = overlay.id
            remove_action = QAction("Remove overlay", self)
            remove_action.triggered.connect(lambda: self.context_remove_overlay.emit(overlay.id))
            menu.addAction(remove_action)
        menu.exec(global_pos)

    def _emit_add_overlay(self, local_pos: QPoint, parent_overlay_id: str) -> None:
        inner = self.inner_rect()
        x = max(0, local_pos.x() - inner.x() - 40)
        y = max(0, local_pos.y() - inner.y() - 20)
        self.context_add_overlay.emit(x, y, parent_overlay_id)


class OverlayWindow(QWidget):
    request_capture_root = Signal(SessionConfig)
    request_auto_cut = Signal()
    request_refresh_mapping = Signal()
    request_record_start = Signal()
    request_record_stop = Signal()
    request_finalize_state = Signal()
    request_export = Signal()
    state_selected = Signal(str)
    request_set_mode = Signal(str)
    request_add_overlay = Signal(int, int, str)
    request_remove_overlay = Signal(str)

    def __init__(self) -> None:
        super().__init__()
        self._settings = QSettings("simcapture", "simcapture")
        self.capture_frame = CaptureFrame()

        self.setWindowTitle("simCapture Controls")
        self.setWindowFlag(Qt.WindowType.WindowStaysOnTopHint, True)
        self.setMinimumWidth(980)
        self._mode: EditorMode = "map"

        self._build_ui()
        self._load_settings()
        self.capture_frame.context_add_overlay.connect(self.request_add_overlay.emit)
        self.capture_frame.context_remove_overlay.connect(self.request_remove_overlay.emit)

    def _build_ui(self) -> None:
        root = QVBoxLayout(self)
        root.setContentsMargins(8, 8, 8, 8)
        root.setSpacing(6)

        row = QHBoxLayout()
        row.setSpacing(6)
        warnings = platform_health_warnings()

        hint = QLabel("Mode 1 maps hotspots. Mode 2 records transitions from the selected state.")
        hint.setObjectName("hint")
        row.addWidget(hint)

        self.session_name = QLineEdit("session")
        self.session_name.setFixedWidth(140)
        row.addWidget(QLabel("Session"))
        row.addWidget(self.session_name)

        self.output_dir = QLineEdit(str(Path.cwd() / "output"))
        self.output_dir.setMinimumWidth(240)
        browse = QPushButton("Browse")
        browse.clicked.connect(self._browse_output)
        row.addWidget(QLabel("Output"))
        row.addWidget(self.output_dir, 1)
        row.addWidget(browse)

        self.settle_ms = QSpinBox()
        self.settle_ms.setRange(10, 5000)
        self.settle_ms.setValue(120)
        self.settle_ms.setFixedWidth(72)
        row.addWidget(QLabel("Settle"))
        row.addWidget(self.settle_ms)

        self.max_settle_ms = QSpinBox()
        self.max_settle_ms.setRange(10, 10000)
        self.max_settle_ms.setValue(400)
        self.max_settle_ms.setFixedWidth(72)
        row.addWidget(QLabel("Max"))
        row.addWidget(self.max_settle_ms)

        self.safe_mode = QCheckBox("Safe mode")
        self.safe_mode.setChecked(any("macOS" in warning for warning in warnings))
        row.addWidget(self.safe_mode)
        root.addLayout(row)

        actions = QHBoxLayout()
        actions.setSpacing(6)
        self.mode_map_btn = QPushButton("Mode 1: Map")
        self.mode_action_btn = QPushButton("Mode 2: Action")
        self.capture_root_btn = QPushButton("Capture Root")
        self.refresh_btn = QPushButton("Refresh")
        self.auto_cut_btn = QPushButton("Auto-cut")
        self.finalize_btn = QPushButton("Finalize State")
        self.record_btn = QPushButton("Record")
        self.stop_btn = QPushButton("Stop")
        self.export_btn = QPushButton("Export")

        self.mode_map_btn.clicked.connect(lambda: self.request_set_mode.emit("map"))
        self.mode_action_btn.clicked.connect(lambda: self.request_set_mode.emit("action"))
        self.capture_root_btn.clicked.connect(self._emit_capture_root)
        self.refresh_btn.clicked.connect(self.request_refresh_mapping.emit)
        self.auto_cut_btn.clicked.connect(self.request_auto_cut.emit)
        self.finalize_btn.clicked.connect(self.request_finalize_state.emit)
        self.record_btn.clicked.connect(self.request_record_start.emit)
        self.stop_btn.clicked.connect(self.request_record_stop.emit)
        self.export_btn.clicked.connect(self.request_export.emit)

        for widget in [
            self.mode_map_btn,
            self.mode_action_btn,
            self.capture_root_btn,
            self.refresh_btn,
            self.auto_cut_btn,
            self.finalize_btn,
            self.record_btn,
            self.stop_btn,
            self.export_btn,
        ]:
            actions.addWidget(widget)
        root.addLayout(actions)

        self.tree = QTreeWidget()
        self.tree.setHeaderLabels(["State Tree"])
        self.tree.itemSelectionChanged.connect(self._on_tree_selection_changed)
        root.addWidget(self.tree, 1)

        if warnings:
            warning_label = QLabel(warnings[0])
            warning_label.setObjectName("warning")
            root.addWidget(warning_label)

        self.setStyleSheet(
            """
            QWidget { background: rgba(10, 16, 24, 245); color: #e5e7eb; }
            QLabel { color: #e5e7eb; }
            #hint { background: rgba(30, 41, 59, 180); padding: 4px 6px; border-radius: 4px; }
            #warning { color: #fcd34d; }
            QLineEdit, QSpinBox, QTreeWidget {
                background: rgba(15, 23, 42, 255);
                color: #f8fafc;
                border: 1px solid rgba(148, 163, 184, 120);
                border-radius: 4px;
                padding: 2px 4px;
            }
            QPushButton {
                background: rgba(15, 23, 42, 255);
                color: #f8fafc;
                border: 1px solid rgba(148, 163, 184, 150);
                border-radius: 5px;
                padding: 4px 8px;
            }
            QPushButton:disabled {
                color: rgba(248, 250, 252, 120);
                border-color: rgba(148, 163, 184, 80);
            }
            """
        )
        self.set_mode("map")
        self.set_recording(False)

    def show(self) -> None:  # type: ignore[override]
        super().show()
        self.capture_frame.show()

    def closeEvent(self, event) -> None:  # type: ignore[override]
        self.capture_frame.close()
        return super().closeEvent(event)

    def session_config(self) -> SessionConfig:
        return SessionConfig(
            session_name=self.session_name.text().strip() or "session",
            output_dir=self.output_dir.text().strip(),
            capture_region=self.capture_frame.capture_rect(),
            settle_ms=self.settle_ms.value(),
            max_settle_ms=self.max_settle_ms.value(),
            input_mode="safe_polling" if self.safe_mode.isChecked() else "global_hooks",
        )

    def _emit_capture_root(self) -> None:
        self._save_settings()
        self.request_capture_root.emit(self.session_config())

    def _browse_output(self) -> None:
        selected = QFileDialog.getExistingDirectory(self, "Select Output Directory", self.output_dir.text())
        if selected:
            self.output_dir.setText(selected)

    def _on_tree_selection_changed(self) -> None:
        items = self.tree.selectedItems()
        if not items:
            return
        state_id = items[0].data(0, Qt.ItemDataRole.UserRole)
        if state_id:
            self.state_selected.emit(state_id)

    def set_mode(self, mode: EditorMode) -> None:
        self._mode = mode
        self.capture_frame.set_editor_mode(mode)
        is_map = mode == "map"
        self.mode_map_btn.setEnabled(not is_map)
        self.mode_action_btn.setEnabled(is_map)
        self.capture_root_btn.setEnabled(is_map)
        self.refresh_btn.setEnabled(is_map)
        self.auto_cut_btn.setEnabled(is_map)
        self.finalize_btn.setEnabled(is_map)
        self.record_btn.setEnabled(not is_map)
        self.stop_btn.setEnabled(False)

    def set_recording(self, recording: bool) -> None:
        self.record_btn.setEnabled(not recording and self._mode == "action")
        self.stop_btn.setEnabled(recording and self._mode == "action")
        self.capture_frame.set_clickthrough(self._mode == "action")
        if not recording:
            self.showNormal()

    def update_tree(self, states: list[StateNode], current_state_id: str | None) -> None:
        self.tree.clear()
        by_parent: dict[str | None, list[StateNode]] = {}
        for state in states:
            by_parent.setdefault(state.parent_state_id, []).append(state)
        for siblings in by_parent.values():
            siblings.sort(key=lambda state: state.id)

        def add_children(parent_item: QTreeWidgetItem | None, parent_state_id: str | None) -> None:
            for state in by_parent.get(parent_state_id, []):
                item = QTreeWidgetItem([state.title])
                item.setData(0, Qt.ItemDataRole.UserRole, state.id)
                if parent_item is None:
                    self.tree.addTopLevelItem(item)
                else:
                    parent_item.addChild(item)
                add_children(item, state.id)

        add_children(None, None)
        self.tree.expandAll()
        if current_state_id is not None:
            matches = self.tree.findItems("*", Qt.MatchFlag.MatchWildcard | Qt.MatchFlag.MatchRecursive)
            for item in matches:
                if item.data(0, Qt.ItemDataRole.UserRole) == current_state_id:
                    self.tree.setCurrentItem(item)
                    break

    def load_state_frame(self, frame: np.ndarray | None, overlays: list[OverlayRegion]) -> None:
        self.capture_frame.set_background_frame(frame)
        self.capture_frame.set_overlays(overlays)

    def _save_settings(self) -> None:
        self._settings.setValue("session_name", self.session_name.text())
        self._settings.setValue("output_dir", self.output_dir.text())
        controls = self.geometry()
        self._settings.setValue("controls_x", controls.x())
        self._settings.setValue("controls_y", controls.y())
        self._settings.setValue("controls_w", controls.width())
        self._settings.setValue("controls_h", controls.height())
        self._settings.setValue("safe_mode", self.safe_mode.isChecked())

        frame = self.capture_frame.geometry()
        self._settings.setValue("frame_x", frame.x())
        self._settings.setValue("frame_y", frame.y())
        self._settings.setValue("frame_w", frame.width())
        self._settings.setValue("frame_h", frame.height())

    def _load_settings(self) -> None:
        session_name = self._settings.value("session_name")
        output_dir = self._settings.value("output_dir")
        if isinstance(session_name, str) and session_name:
            self.session_name.setText(session_name)
        if isinstance(output_dir, str) and output_dir:
            self.output_dir.setText(output_dir)

        cx = self._settings.value("controls_x")
        cy = self._settings.value("controls_y")
        cw = self._settings.value("controls_w")
        ch = self._settings.value("controls_h")
        if all(v is not None for v in [cx, cy, cw, ch]):
            self.setGeometry(int(cx), int(cy), int(cw), int(ch))

        fx = self._settings.value("frame_x")
        fy = self._settings.value("frame_y")
        fw = self._settings.value("frame_w")
        fh = self._settings.value("frame_h")
        if all(v is not None for v in [fx, fy, fw, fh]):
            self.capture_frame.setGeometry(int(fx), int(fy), int(fw), int(fh))

        safe_mode = self._settings.value("safe_mode")
        if safe_mode is not None:
            self.safe_mode.setChecked(str(safe_mode).lower() in {"1", "true", "yes"})
