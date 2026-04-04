from simcapture.build.manifest import find_target_region, transition_resolution_key
from simcapture.model.session import Region


def test_transition_resolution_key_deterministic():
    key1 = transition_resolution_key("s1", "mouse_down", "r1", ["shift", "ctrl"], "left")
    key2 = transition_resolution_key("s1", "mouse_down", "r1", ["ctrl", "shift"], "left")
    assert key1 == key2


def test_find_target_region_contains_then_nearest():
    regions = [
        Region(id="r1", x=10, y=10, w=40, h=40),
        Region(id="r2", x=100, y=100, w=30, h=30),
    ]

    assert find_target_region(regions, 20, 20) == "r1"
    assert find_target_region(regions, 92, 92, nearest_tolerance=20) == "r2"
    assert find_target_region(regions, 300, 300, nearest_tolerance=20) is None
