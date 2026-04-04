# simCapture

simCapture records visual changes inside a desktop overlay region and outputs a static interactive replay bundle.

## Install

```bash
python -m venv .venv
source .venv/bin/activate
pip install -e .[dev]
```

## Record

```bash
simcapture record
```

1. Move/resize the overlay over your target app.
2. Keep controls in the top banner; only the green boxed area is captured.
3. Set session name/output directory.
4. If startup fails on your OS permissions/hooks, enable `Safe mode` and try again.
5. Click `Start`, interact with target app, then `Stop`.
6. Open `<output>/<session>/index.html` in a browser.

## Validate bundle

```bash
simcapture validate /path/to/output/session
```

## Notes

- macOS may require Screen Recording + Accessibility permissions.
- On Wayland, global hooks may be limited depending on compositor policy.
- v1 only replays recorded transitions; unknown interactions show `Not recorded`.
