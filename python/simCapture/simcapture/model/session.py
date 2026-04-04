from __future__ import annotations

from datetime import datetime, timezone
from pathlib import Path
from typing import Literal

from pydantic import BaseModel, Field, field_validator


EventType = Literal[
    "mouse_move",
    "mouse_down",
    "mouse_up",
    "wheel",
    "key_down",
    "key_up",
    "text_input",
]

EditorMode = Literal["map", "action"]


class Rect(BaseModel):
    x: int
    y: int
    w: int = Field(gt=0)
    h: int = Field(gt=0)


class Point(BaseModel):
    x: int
    y: int


class SessionConfig(BaseModel):
    session_name: str = "session"
    output_dir: str
    capture_region: Rect
    settle_ms: int = Field(default=120, ge=10, le=5000)
    max_settle_ms: int = Field(default=400, ge=10, le=10000)
    retry_on_instability: int = Field(default=2, ge=0, le=10)
    fidelity_mode: Literal["max_visual"] = "max_visual"
    platform_mode: Literal["cross_platform"] = "cross_platform"
    input_mode: Literal["global_hooks", "safe_polling"] = "global_hooks"

    @field_validator("max_settle_ms")
    @classmethod
    def validate_max_settle(cls, value: int, info):
        settle_ms = info.data.get("settle_ms", 120)
        if value < settle_ms:
            raise ValueError("max_settle_ms must be >= settle_ms")
        return value


class SessionInfo(BaseModel):
    id: str
    name: str
    created_at: datetime = Field(default_factory=lambda: datetime.now(tz=timezone.utc))
    capture_region: Rect
    canvas_size: Point


class SnippetAsset(BaseModel):
    id: str
    file: str


class StateAsset(BaseModel):
    state_id: str
    frame: str


class Assets(BaseModel):
    initial_frame: str
    states: list[StateAsset]
    snippets: list[SnippetAsset]


class OverlayRegion(BaseModel):
    id: str
    state_id: str
    x: int
    y: int
    w: int = Field(gt=0)
    h: int = Field(gt=0)
    label: str | None = None
    z_index: int = 0


class Region(OverlayRegion):
    pass


class InputEvent(BaseModel):
    id: str
    ts_ms: int = Field(ge=0)
    type: EventType
    pos: Point | None = None
    button: str | None = None
    key: str | None = None
    modifiers: list[str] = Field(default_factory=list)
    text: str | None = None


class PatchRef(BaseModel):
    snippet_id: str
    x: int
    y: int
    w: int = Field(gt=0)
    h: int = Field(gt=0)


class Transition(BaseModel):
    id: str
    from_state: str
    to_state: str
    trigger_event_id: str
    target_region_id: str | None = None
    patches: list[PatchRef] = Field(default_factory=list)
    fallback: Literal["noop_hint"] = "noop_hint"
    resolution_key: str


class StateNode(BaseModel):
    id: str
    parent_state_id: str | None = None
    title: str
    frame: str


class Manifest(BaseModel):
    version: Literal["2.0"] = "2.0"
    session: SessionInfo
    assets: Assets
    states: list[StateNode]
    regions: list[OverlayRegion]
    events: list[InputEvent]
    transitions: list[Transition]

    def validate_asset_paths(self, bundle_path: Path) -> list[str]:
        missing: list[str] = []

        def check_path(rel_path: str) -> None:
            if not (bundle_path / rel_path).exists():
                missing.append(rel_path)

        check_path(self.assets.initial_frame)
        for state in self.assets.states:
            check_path(state.frame)
        for snippet in self.assets.snippets:
            check_path(snippet.file)
        for state in self.states:
            check_path(state.frame)
        return missing
