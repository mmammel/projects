# Implementation Plan: Optimize Convex Hull Algorithm (QuickHull)

## Phase 1: Local Testing Infrastructure [checkpoint: 290eff2]
- [x] Task: Enable Local Rendering [1f25fb9]
    - [x] Add a "Render" button to `rhombicTriacontahedron/index.html` next to the "Save" button.
    - [x] Implement a `renderManual` function in `index.html` (or `rhombicTriaconta.js`) that reads the `polyVerts` textarea and calls `loadVertices` directly.
- [x] Task: Graceful API Error Handling [1f25fb9]
    - [x] Wrap API calls in `apiConnect.js` (or their call sites) with try-catch blocks to prevent script termination on connection failure.
    - [x] Update UI to show a non-intrusive warning instead of failing when the backend is unavailable.
- [x] Task: Conductor - User Manual Verification 'Phase 1: Local Testing Infrastructure' (Protocol in workflow.md)

## Phase 2: QuickHull Implementation
- [~] Task: Scaffold hull.js
    - [ ] Create `rhombicTriacontahedron/hull.js`.
    - [ ] Include `hull.js` in `rhombicTriacontahedron/index.html` via a `<script>` tag.
- [ ] Task: Implement 3D QuickHull Algorithm
    - [ ] Implement the core QuickHull algorithm in `hull.js`, targeting $O(n \log n)$ complexity.
    - [ ] Ensure the algorithm returns raw face indices (triples or arrays of indices).
- [ ] Task: Conductor - User Manual Verification 'Phase 2: QuickHull Implementation' (Protocol in workflow.md)

## Phase 3: Integration and Refactoring
- [ ] Task: Integrate with VertexGroup
    - [ ] Modify `VertexGroup.prototype.findFaces` in `shapeClasses.js` to call the new QuickHull function.
    - [ ] Implement an adapter within `findFaces` to convert QuickHull index arrays into the existing `Face` and `FaceGroup` object structure.
- [ ] Task: Precision and Stability Polish
    - [ ] Ensure the new implementation uses existing precision constants for floating-point comparisons.
    - [ ] Verify handling of edge cases (e.g., duplicate vertices, coplanar points).
- [ ] Task: Conductor - User Manual Verification 'Phase 3: Integration and Refactoring' (Protocol in workflow.md)

## Phase 4: Final Verification
- [ ] Task: Comparative Testing
    - [ ] Verify that the standard shapes (Cube, Pyramid) render identically to the previous version.
    - [ ] Test with complex vertex sets via the new "Render" button to verify performance improvements.
- [ ] Task: Conductor - User Manual Verification 'Phase 4: Final Verification' (Protocol in workflow.md)
