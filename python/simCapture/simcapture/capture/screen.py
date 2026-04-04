from __future__ import annotations

import numpy as np
from PySide6.QtCore import Qt

from simcapture.model.session import Rect


class ScreenCapture:
    """Capture using Qt screen APIs to avoid platform-specific native crashes."""

    def capture_region(self, rect: Rect) -> np.ndarray:
        from PySide6.QtGui import QGuiApplication, QImage

        app = QGuiApplication.instance()
        if app is None:
            raise RuntimeError("QGuiApplication must be initialized before capture")

        screen = QGuiApplication.primaryScreen()
        if screen is None:
            raise RuntimeError("No active screen detected")

        pixmap = screen.grabWindow(0, rect.x, rect.y, rect.w, rect.h)
        if pixmap.isNull():
            raise RuntimeError(
                "Screen grab returned empty image. Check screen-capture permissions and capture region."
            )

        image = pixmap.toImage().convertToFormat(QImage.Format.Format_RGB888)
        if image.width() != rect.w or image.height() != rect.h:
            image = image.scaled(
                rect.w,
                rect.h,
                Qt.AspectRatioMode.IgnoreAspectRatio,
                Qt.TransformationMode.SmoothTransformation,
            )
        width = image.width()
        height = image.height()
        bytes_per_line = image.bytesPerLine()
        size = image.sizeInBytes()

        buffer = image.constBits()
        arr = np.frombuffer(buffer, dtype=np.uint8, count=size)
        arr = arr.reshape((height, bytes_per_line))
        arr = arr[:, : width * 3].reshape((height, width, 3)).copy()
        return arr
