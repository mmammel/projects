from __future__ import annotations

import argparse
from pathlib import Path

from simcapture.build.bundle import validate_bundle, write_diagnostic_html


def main() -> int:
    parser = argparse.ArgumentParser(prog="simcapture", description="simCapture session recorder and validator")
    sub = parser.add_subparsers(dest="command", required=True)

    sub.add_parser("record", help="launch overlay recorder UI")

    validate_parser = sub.add_parser("validate", help="validate output bundle")
    validate_parser.add_argument("bundle_path", type=Path)
    validate_parser.add_argument("--diagnostic", action="store_true", help="emit diagnostic.html from manifest")

    args = parser.parse_args()

    if args.command == "record":
        from simcapture.app import SimCaptureApplication

        app = SimCaptureApplication()
        return app.run()

    if args.command == "validate":
        errors = validate_bundle(args.bundle_path)
        if errors:
            print("Missing assets:")
            for err in errors:
                print(f"- {err}")
            return 1
        if args.diagnostic:
            report = write_diagnostic_html(args.bundle_path)
            print(f"Diagnostic HTML written: {report}")
        print("Bundle is valid")
        return 0

    return 1


if __name__ == "__main__":
    raise SystemExit(main())
