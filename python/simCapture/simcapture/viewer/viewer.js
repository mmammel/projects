const canvas = document.getElementById("stage");
const ctx = canvas.getContext("2d");
const hint = document.getElementById("hint");
const sessionName = document.getElementById("session-name");

let manifest = null;
let currentState = "s0";
let stateFrameMap = new Map();
let snippetMap = new Map();
let transitionsByState = new Map();

function fitCanvasToViewport() {
  if (!manifest) {
    return;
  }
  const maxWidth = Math.max(window.innerWidth - 56, 240);
  const maxHeight = Math.max(window.innerHeight - 120, 180);
  const scale = Math.min(maxWidth / canvas.width, maxHeight / canvas.height, 1);
  const displayWidth = Math.max(1, Math.floor(canvas.width * scale));
  const displayHeight = Math.max(1, Math.floor(canvas.height * scale));
  canvas.style.width = `${displayWidth}px`;
  canvas.style.height = `${displayHeight}px`;
}

function showHint(text) {
  hint.textContent = text;
  if (!text) {
    return;
  }
  window.setTimeout(() => {
    if (hint.textContent === text) {
      hint.textContent = "";
    }
  }, 900);
}

function eventPayloadKey(event, textValue) {
  if (event.type.startsWith("mouse")) {
    if (typeof event.button === "number") {
      if (event.button === 0) return "left";
      if (event.button === 1) return "middle";
      if (event.button === 2) return "right";
    }
    return event.button || "_";
  }
  if (event.type.startsWith("key")) {
    return event.key || "_";
  }
  if (event.type === "text_input") {
    return textValue || "_";
  }
  if (event.type === "wheel") {
    return `${event.deltaX || 0}:${event.deltaY || 0}`;
  }
  return "_";
}

function findRegionId(x, y) {
  const matching = manifest.regions
    .filter((region) => region.state_id === currentState)
    .sort((a, b) => {
      const zDiff = (b.z_index || 0) - (a.z_index || 0);
      if (zDiff !== 0) {
        return zDiff;
      }
      const areaDiff = (a.w * a.h) - (b.w * b.h);
      if (areaDiff !== 0) {
        return areaDiff;
      }
      return String(a.id).localeCompare(String(b.id));
    });

  for (const region of matching) {
    if (x >= region.x && x <= region.x + region.w && y >= region.y && y <= region.y + region.h) {
      return region.id;
    }
  }
  return null;
}

function initialStateId() {
  const root = manifest?.states?.find((state) => state.parent_state_id === null);
  return root?.id || "s0";
}

function buildKey(state, eventType, regionId, payload) {
  return [state, eventType, regionId || "_", "", payload || "_"].join("|");
}

function drawState(stateId) {
  const img = stateFrameMap.get(stateId);
  if (!img) {
    showHint(`Missing state: ${stateId}`);
    return;
  }
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  ctx.drawImage(img, 0, 0, canvas.width, canvas.height);
}

async function loadImage(path) {
  const img = new Image();
  img.src = path;
  await img.decode();
  return img;
}

async function loadManifest() {
  if (window.__SIMCAPTURE_MANIFEST__) {
    manifest = window.__SIMCAPTURE_MANIFEST__;
  } else {
    const res = await fetch("manifest.json");
    manifest = await res.json();
  }
  sessionName.textContent = manifest.session.name;

  for (const state of manifest.assets.states) {
    stateFrameMap.set(state.state_id, await loadImage(state.frame));
  }
  for (const snippet of manifest.assets.snippets) {
    snippetMap.set(snippet.id, await loadImage(snippet.file));
  }

  for (const transition of manifest.transitions) {
    if (!transitionsByState.has(transition.from_state)) {
      transitionsByState.set(transition.from_state, new Map());
    }
    transitionsByState.get(transition.from_state).set(transition.resolution_key, transition);
  }

  currentState = initialStateId();
  const initialImage = stateFrameMap.get(currentState) || stateFrameMap.get("s0");
  const fallbackWidth = initialImage?.naturalWidth || initialImage?.width || 1;
  const fallbackHeight = initialImage?.naturalHeight || initialImage?.height || 1;
  const manifestWidth = Number(manifest?.session?.canvas_size?.x) || 0;
  const manifestHeight = Number(manifest?.session?.canvas_size?.y) || 0;

  canvas.width = manifestWidth > 0 ? manifestWidth : fallbackWidth;
  canvas.height = manifestHeight > 0 ? manifestHeight : fallbackHeight;

  drawState(currentState);
  fitCanvasToViewport();
}

function handleInteraction(eventType, domEvent, textValue = null) {
  if (!manifest) {
    return;
  }
  const rect = canvas.getBoundingClientRect();
  const scaleX = canvas.width / rect.width;
  const scaleY = canvas.height / rect.height;
  const x = Math.round((domEvent.clientX - rect.left) * scaleX);
  const y = Math.round((domEvent.clientY - rect.top) * scaleY);
  const regionId = findRegionId(x, y);
  const payload = eventPayloadKey({ type: eventType, button: domEvent.button, key: domEvent.key, deltaX: domEvent.deltaX, deltaY: domEvent.deltaY }, textValue);
  const key = buildKey(currentState, eventType, regionId, payload);

  const stateMap = transitionsByState.get(currentState);
  const transition = stateMap?.get(key);

  if (!transition) {
    showHint("Not recorded");
    return;
  }

  currentState = transition.to_state;
  drawState(currentState);
}

canvas.addEventListener("mousemove", (e) => handleInteraction("mouse_move", e));
canvas.addEventListener("mousedown", (e) => handleInteraction("mouse_down", e));
canvas.addEventListener("mouseup", (e) => handleInteraction("mouse_up", e));
canvas.addEventListener("wheel", (e) => {
  e.preventDefault();
  handleInteraction("wheel", e);
});

window.addEventListener("keydown", (e) => {
  const fakeEvent = { clientX: -1, clientY: -1, key: e.key, button: null };
  handleInteraction("key_down", fakeEvent);
  if (e.key.length === 1) {
    handleInteraction("text_input", fakeEvent, e.key);
  }
});
window.addEventListener("keyup", (e) => {
  const fakeEvent = { clientX: -1, clientY: -1, key: e.key, button: null };
  handleInteraction("key_up", fakeEvent);
});
window.addEventListener("resize", fitCanvasToViewport);

loadManifest().catch((err) => {
  console.error(err);
  showHint(`Failed to load manifest: ${String(err)}`);
});
