from __future__ import annotations

import os
import platform


def current_platform() -> str:
    return platform.system().lower()


def is_macos() -> bool:
    return current_platform() == "darwin"


def platform_health_warnings() -> list[str]:
    system = current_platform()
    warnings: list[str] = []

    if system == "darwin":
        warnings.append("macOS: ensure Screen Recording and Accessibility permissions are granted.")
    elif system == "linux":
        session_type = os.environ.get("XDG_SESSION_TYPE", "").lower()
        if session_type == "wayland":
            warnings.append("Wayland detected: global input hooks may be restricted by compositor policy.")
    return warnings
