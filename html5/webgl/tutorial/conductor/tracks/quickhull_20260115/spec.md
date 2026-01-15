# Track Specification: Optimize Convex Hull Algorithm (QuickHull)

## Overview
The current `VertexGroup.prototype.findFaces` method uses an $O(n^4)$ brute-force algorithm to identify the faces of the convex hull. This implementation is significantly inefficient for larger vertex sets. This track replaces the brute-force method with a 3D QuickHull algorithm ($O(n \log n)$) to improve performance while maintaining the existing data structures and rendering output. Additionally, this track enables local testing by adding a "Render" button and gracefully handling API failures.

## Functional Requirements
- **Algorithm Replacement:** Replace the triple-loop brute-force logic in `findFaces` with a 3D QuickHull implementation.
- **External Utility:** The QuickHull algorithm will be implemented in a new standalone file `rhombicTriacontahedron/hull.js`.
- **Integration:** Update `rhombicTriacontahedron/index.html` to include the new `hull.js` script.
- **Data Adapter:** Implement an adapter within `findFaces` to convert the raw output of the QuickHull algorithm (e.g., face indices) into the existing `Face` and `FaceGroup` objects required by the `Polyhedron` class.
- **Compatibility:** The new implementation must produce identical results to the current algorithm for standard convex shapes (Cube, Pyramid, etc.).
- **Local Rendering:** Add a "Render" button to the UI that allows users to visualize the vertices entered in the textarea directly, bypassing the "Save" requirement.
- **Graceful Error Handling:** Modify `apiConnect.js` (or the calling code) to catch API connection errors (like failed `savePolyhedron` or `loadPolyhedron` calls) and allow the application to continue functioning using local data.

## Non-Functional Requirements
- **Performance:** Significant reduction in execution time for complex polyhedra (vertices > 100).
- **Modularity:** Adhere to "Minimalist & Modern" guidelines by keeping the Hull logic separate from the high-level shape logic.
- **Precision:** Handle floating-point comparisons using the project's established precision constants where applicable.
- **Testability:** Enable manual testing of the rendering logic without a running backend server.

## Acceptance Criteria
- [ ] A new file `rhombicTriacontahedron/hull.js` exists containing a stable 3D QuickHull implementation.
- [ ] `rhombicTriacontahedron/index.html` includes `hull.js` and a new "Render" button.
- [ ] Clicking "Render" updates the 3D view with the shape defined in the text area, without triggering a backend save.
- [ ] `VertexGroup.prototype.findFaces` in `shapeClasses.js` uses the new QuickHull logic.
- [ ] The 3D viewer renders the standard shapes correctly without artifacts or missing faces.
- [ ] The application loads and functions locally (file:// or localhost) even if API calls to the backend fail.

## Out of Scope
- Implementing support for non-convex (concave) shapes.
- Modifying the WebGL shader programs.
- Refactoring the `Sylvester.js` library.
