import numpy as np

from simcapture.diff.detector import DiffDetector


def test_detect_hover_like_change_region():
    before = np.zeros((100, 100, 3), dtype=np.uint8)
    after = before.copy()
    after[20:40, 30:60] = [255, 255, 255]

    detector = DiffDetector(pixel_threshold=5, min_area=20)
    regions = detector.detect(before, after)

    assert regions
    box = regions[0]
    assert box.x <= 30
    assert box.y <= 20
    assert box.x + box.w >= 60
    assert box.y + box.h >= 40
