# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Running the App

No build system or dependencies to install. Open either HTML file directly in a browser:

- `primeSpiralCanv.html` — the current, canvas-based version (primary)
- `primeSpiral.html` — the older SVG-based version

External deps (jQuery, Bootstrap, Google Fonts) are loaded from CDNs.

## Architecture

The app visualizes mathematical sequences as 2D spirals. At each iteration `i`, a **checker** function decides whether to plot a dot (`draw: true`) and/or change direction (`turn: true`). The angle advances by `deltaTheta` on each turn, and the cursor always moves `2 * cos/sin(currTheta)` per step.

### Script load order (both HTML files)

1. `bigConstants.js` — large string/array constants: `piString` (digits of π), Collatz sequence arrays
2. `bitmask.js` — `BitMask` class (32-bit integer array backed), available for prime sieve use
3. `checkers.js` — defines `checkers[]` array and `isPrime()`, `getCollatzConvergenceNum()`
4. `utils.js` — `getURLParameter(name)` for URL state restore (canvas version only)
5. Inline `<script>` in each HTML file — rendering loop, UI wiring, animation

### Checker contract (`checkers.js`)

Each entry in the `checkers[]` array is an object with:

```js
{
  id: string,
  name: string,
  description: string,
  default: boolean,          // optional, pre-selects in dropdown
  stateObj: { ... },         // deep-cloned via JSON before each render
  checker: function(state, i, j, k) { return { draw, turn, turnIncrease? }; },
  extraVars: [               // optional, drives dynamic form inputs
    { id, label, defaultVal, values?, changeFunc? }
  ]
}
```

`j` and `k` map to `extraVarValues[0]` and `extraVarValues[1]` from the UI. A `changeFunc` on an `extraVar` is wired to the input's `change` event and mutates `stateObj` live.

### Canvas version (`primeSpiralCanv.html`) features over SVG version

- Draws to an offscreen `stagingCanvas`, then blits to `canvasRoot` — avoids flicker
- URL state serialization via `buildContextURL()` / `setStateFromURL()` (query param abbreviations: `vb`, `ctr`, `i`, `ti`, `r`, `as`, `dt`, `ct`, `cd`, `pr`, `cc`)
- Multi-color radial gradient: up to 7 color pickers, gradient anchored at the origin
- "Connect the dots" mode: draws stroked lines between consecutive plotted points
- Smooth animated zoom via `centerOnOffsetInner()` promise chain
- Touch and mouse drag panning via `touchContext`
- `turnIncrease` accumulates on each turn (in addition to per-checker `turnIncrease`)
- Animation uses `requestAnimationFrame` (not `setInterval`)

## Adding a New Checker

Add an entry to the `checkers[]` array in `checkers.js`. The `stateObj` is deep-cloned before each render so it resets automatically. Return `{ draw: false, turn: false }` as a safe default; add `turnIncrease` to the return value to override the global `turnIncrease` for that step.
