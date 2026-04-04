from datetime import datetime, timezone

import numpy as np

from simcapture.build.bundle import validate_bundle, write_bundle
from simcapture.model.session import Assets, Manifest, Point, SessionInfo, SnippetAsset, StateAsset
from simcapture.recorder import ManifestBundle


def test_bundle_roundtrip_validation(tmp_path):
    frame = np.zeros((64, 64, 3), dtype=np.uint8)

    manifest = Manifest(
        session=SessionInfo(
            id="test-session",
            name="demo",
            created_at=datetime.now(timezone.utc),
            capture_region={"x": 0, "y": 0, "w": 64, "h": 64},
            canvas_size=Point(x=64, y=64),
        ),
        assets=Assets(
            initial_frame="assets/base/state_0000.png",
            states=[StateAsset(state_id="s0", frame="assets/states/s0.png")],
            snippets=[SnippetAsset(id="sn_0001", file="assets/snippets/sn_0001.png")],
        ),
        regions=[],
        events=[],
        transitions=[],
    )

    bundle = ManifestBundle(
        manifest=manifest,
        state_frames={"s0": frame},
        snippet_frames={"sn_0001": frame},
    )

    bundle_dir = tmp_path / "bundle"
    write_bundle(bundle, bundle_dir)
    errors = validate_bundle(bundle_dir)
    assert errors == []
