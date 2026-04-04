from __future__ import annotations

from dataclasses import dataclass

import numpy as np

try:
    import cv2
except ImportError:  # pragma: no cover - exercised by environments without opencv
    cv2 = None


@dataclass(frozen=True)
class DiffRegion:
    x: int
    y: int
    w: int
    h: int


class DiffDetector:
    def __init__(self, pixel_threshold: int = 15, min_area: int = 25, merge_gap: int = 4) -> None:
        self.pixel_threshold = pixel_threshold
        self.min_area = min_area
        self.merge_gap = merge_gap

    def _to_gray(self, image: np.ndarray) -> np.ndarray:
        if cv2 is not None:
            return cv2.cvtColor(image, cv2.COLOR_BGR2GRAY)
        if image.ndim == 2:
            return image
        return np.mean(image[..., :3], axis=2).astype(np.uint8)

    def _find_boxes(self, mask: np.ndarray) -> list[DiffRegion]:
        if cv2 is not None:
            contours, _ = cv2.findContours(mask, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
            boxes = []
            for contour in contours:
                x, y, w, h = cv2.boundingRect(contour)
                if w * h >= self.min_area:
                    boxes.append(DiffRegion(x=x, y=y, w=w, h=h))
            return boxes

        ys, xs = np.where(mask > 0)
        if len(xs) == 0:
            return []
        x0, x1 = int(xs.min()), int(xs.max())
        y0, y1 = int(ys.min()), int(ys.max())
        return [DiffRegion(x=x0, y=y0, w=x1 - x0 + 1, h=y1 - y0 + 1)]

    def _merge_regions(self, regions: list[DiffRegion]) -> list[DiffRegion]:
        if not regions:
            return []

        remaining = regions[:]
        merged: list[DiffRegion] = []
        while remaining:
            base = remaining.pop()
            changed = True
            while changed:
                changed = False
                next_remaining: list[DiffRegion] = []
                for candidate in remaining:
                    if self._touching(base, candidate):
                        base = self._merge_pair(base, candidate)
                        changed = True
                    else:
                        next_remaining.append(candidate)
                remaining = next_remaining
            merged.append(base)
        return merged

    def _touching(self, a: DiffRegion, b: DiffRegion) -> bool:
        ax2 = a.x + a.w
        ay2 = a.y + a.h
        bx2 = b.x + b.w
        by2 = b.y + b.h
        return not (
            bx2 + self.merge_gap < a.x
            or ax2 + self.merge_gap < b.x
            or by2 + self.merge_gap < a.y
            or ay2 + self.merge_gap < b.y
        )

    def _merge_pair(self, a: DiffRegion, b: DiffRegion) -> DiffRegion:
        x0 = min(a.x, b.x)
        y0 = min(a.y, b.y)
        x1 = max(a.x + a.w, b.x + b.w)
        y1 = max(a.y + a.h, b.y + b.h)
        return DiffRegion(x=x0, y=y0, w=x1 - x0, h=y1 - y0)

    def detect(self, before: np.ndarray, after: np.ndarray) -> list[DiffRegion]:
        if before.shape != after.shape:
            raise ValueError("before and after frames must have the same shape")

        g_before = self._to_gray(before)
        g_after = self._to_gray(after)
        delta = np.abs(g_after.astype(np.int16) - g_before.astype(np.int16)).astype(np.uint8)
        mask = (delta >= self.pixel_threshold).astype(np.uint8) * 255

        if cv2 is not None:
            kernel = np.ones((3, 3), np.uint8)
            mask = cv2.morphologyEx(mask, cv2.MORPH_OPEN, kernel)
            mask = cv2.morphologyEx(mask, cv2.MORPH_CLOSE, kernel)

        boxes = self._find_boxes(mask)
        return self._merge_regions(boxes)
