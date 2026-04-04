from __future__ import annotations

import threading
import time
from typing import Callable

from simcapture.model.session import InputEvent, Point

try:
    from pynput import keyboard, mouse
except ImportError:  # pragma: no cover
    keyboard = None
    mouse = None


class InputHookManager:
    def __init__(self, on_event: Callable[[InputEvent], None], start_ts: float) -> None:
        self._on_event = on_event
        self._start_ts = start_ts
        self._mouse_listener = None
        self._keyboard_listener = None
        self._lock = threading.Lock()
        self._counter = 0

    def _next_event_id(self) -> str:
        with self._lock:
            self._counter += 1
            return f"e{self._counter}"

    def _ts_ms(self) -> int:
        return int((time.monotonic() - self._start_ts) * 1000)

    def _emit(self, event: InputEvent) -> None:
        self._on_event(event)

    def start(self) -> None:
        if mouse is not None:
            self._mouse_listener = mouse.Listener(
                on_move=self._on_mouse_move,
                on_click=self._on_mouse_click,
                on_scroll=self._on_mouse_scroll,
            )
            self._mouse_listener.start()

        if keyboard is not None:
            self._keyboard_listener = keyboard.Listener(
                on_press=self._on_key_press,
                on_release=self._on_key_release,
            )
            self._keyboard_listener.start()

    def stop(self) -> None:
        if self._mouse_listener is not None:
            self._mouse_listener.stop()
            self._mouse_listener = None
        if self._keyboard_listener is not None:
            self._keyboard_listener.stop()
            self._keyboard_listener = None

    def _on_mouse_move(self, x: int, y: int) -> None:
        self._emit(
            InputEvent(
                id=self._next_event_id(),
                ts_ms=self._ts_ms(),
                type="mouse_move",
                pos=Point(x=int(x), y=int(y)),
            )
        )

    def _on_mouse_click(self, x: int, y: int, button, pressed: bool) -> None:
        button_name = getattr(button, "name", str(button))
        self._emit(
            InputEvent(
                id=self._next_event_id(),
                ts_ms=self._ts_ms(),
                type="mouse_down" if pressed else "mouse_up",
                pos=Point(x=int(x), y=int(y)),
                button=button_name,
            )
        )

    def _on_mouse_scroll(self, x: int, y: int, dx: int, dy: int) -> None:
        self._emit(
            InputEvent(
                id=self._next_event_id(),
                ts_ms=self._ts_ms(),
                type="wheel",
                pos=Point(x=int(x), y=int(y)),
                text=f"{dx}:{dy}",
            )
        )

    def _key_to_string(self, key) -> str:
        if hasattr(key, "char") and key.char is not None:
            return str(key.char)
        name = getattr(key, "name", None)
        if name:
            return str(name)
        return str(key)

    def _on_key_press(self, key) -> None:
        key_text = self._key_to_string(key)
        self._emit(
            InputEvent(
                id=self._next_event_id(),
                ts_ms=self._ts_ms(),
                type="key_down",
                key=key_text,
            )
        )
        if len(key_text) == 1:
            self._emit(
                InputEvent(
                    id=self._next_event_id(),
                    ts_ms=self._ts_ms(),
                    type="text_input",
                    text=key_text,
                )
            )

    def _on_key_release(self, key) -> None:
        self._emit(
            InputEvent(
                id=self._next_event_id(),
                ts_ms=self._ts_ms(),
                type="key_up",
                key=self._key_to_string(key),
            )
        )
