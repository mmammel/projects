var canvas;
var gl;

var cubeVerticesBuffer;
var cubeVerticesColorBuffer;
var cubeVerticesIndexBuffer;
var cubeRotation = 0.0;
var cubeXOffset = 0.0;
var cubeYOffset = 0.0;
var cubeZOffset = 0.0;
var lastCubeUpdateTime = 0;
var xIncValue = 0.2;
var yIncValue = -0.4;
var zIncValue = 0.3;
var phi = (1.0 + Math.sqrt(5.0)) / 2.0;
var phi2 = Math.pow(phi,2);
var phi3 = Math.pow(phi,3);
var invphi = (Math.sqrt(5.0) - 1) / 2.0;
var invphi2 = Math.pow(invphi,2);
var invphi3 = Math.pow(invphi,3);
var sqrt3 = Math.sqrt(3.0);

var mvMatrix;
var shaderProgram;
var vertexPositionAttribute;
var vertexColorAttribute;
var perspectiveMatrix;

var poly; // the polyhedron

// The vertices to use
// hardcoded for now, could pass them in.
var pyramid_polyVerts = [
  0,0,2,
  2,2,-1,
  2,-2,-1,
  -2,2,-1,
  -2,-2,-1
];

var tetartoid_polyVerts = [
  -0.4,-0.8,2.0,
  -0.4,0.8,-2.0,
  -0.8,-2.0,0.4,
  -0.8,2.0,-0.4,
  -1.0,-1.0,-1.0,
  -1.0,1.0,1.0,
  -1.5,-1.5,1.5,
  -1.5,1.5,-1.5,
  -2.0,-0.4,0.8,
  -2.0,0.4,-0.8,
  0.4,-0.8,-2.0,
  0.4,0.8,2.0,
  0.8,-2.0,-0.4,
  0.8,2.0,0.4,
  1.0,-1.0,1.0,
  1.0,1.0,-1.0,
  1.5,-1.5,-1.5,
  1.5,1.5,1.5,
  2.0,-0.4,-0.8,
  2.0,0.4,0.8
];

var squatSoma_polyVerts = [
  0,-1,.5,
  0,1,-.5,
  0,-1,-1.5,
  0,1,1.5,
  sqrt3,1,.5,
  -1*sqrt3,1,.5,
  sqrt3,-1,-.5,
  -1*sqrt3,-1,-.5
];

var fccPrimitive_polyVerts = [
  0,-.5,-1.5,
  0,-.5,.5,
  1,.5,.5,
  -1,.5,.5,
  0,.5,-.5,
  0,.5,1.5,
  1,-.5,-.5,
  -1,-.5,-.5
];

var officialPrimitive_polyVerts = [
  0,1,0,
  1,1,1,
  0,0,1,
  1,0,0,
  -1,0,0,
  0,0,-1,
  0,-1,0,
  -1,-1,-1
];

var giant_polyVerts = [
0.0000000000000011,0.0,2.6180339887499,
0.356822089773094,0.872677996249968,2.28470065541658,
-0.0000000000000002,1.74535599249993,1.95136732208324,
-0.356822089773092,0.872677996249962,2.28470065541657,
2.44569498711506,0.333333333333379,0.872677996249958,
2.08887289734197,1.20601132958334,0.539344662916625,
1.51152262815234,1.95136732208326,0.872677996249972,
1.86834471792543,1.0786893258333,1.20601132958331,
1.51152262815234,0.872677996249996,-1.95136732208325,
0.934172358962706,1.61803398874991,-1.6180339887499,
0.934172358962709,2.28470065541658,-0.872677996249962,
1.51152262815234,1.53934466291666,-1.20601132958331,
-1.51152262815235,0.872677996249931,-1.95136732208321,
-1.51152262815234,1.5393446629166,-1.20601132958328,
-0.934172358962723,2.28470065541653,-0.872677996249951,
-0.934172358962726,1.61803398874987,-1.61803398874988,
-2.44569498711505,0.333333333333284,0.872677996249975,
-1.86834471792543,1.07868932583322,1.20601132958331,
-1.51152262815234,1.95136732208318,0.872677996249976,
-2.08887289734196,1.20601132958325,0.539344662916645,
-0.934172358962709,-2.28470065541658,0.872677996249962,
-0.934172358962706,-1.61803398874991,1.61803398874989,
-1.51152262815234,-0.872677996249996,1.95136732208324,
-1.51152262815234,-1.53934466291666,1.20601132958331,
0.0000000000000002,-1.74535599249993,-1.95136732208324,
-0.577350269189622,-2.07868932583328,-1.20601132958331,
-1.51152262815234,-1.95136732208326,-0.872677996249972,
-0.934172358962719,-1.61803398874992,-1.6180339887499,
0.934172358962717,0.127322003750059,-2.28470065541658,
-0.0000000000000012,-0.0000000000000003,-2.6180339887499,
0.57735026918962,0.745355992499936,-2.28470065541657,
1.51152262815234,1.95136732208327,-0.12732200375003,
0.934172358962709,2.28470065541658,0.127322003750038,
-0.577350269189627,2.07868932583324,1.2060113295833,
-0.934172358962715,1.61803398874987,1.61803398874991,
-2.08887289734196,-0.53934466291668,1.20601132958331,
-1.86834471792543,-0.0000000000000326,1.61803398874991,
-0.577350269189621,-0.745355992499936,2.28470065541657,
-0.934172358962716,-0.127322003750059,2.28470065541658,
1.51152262815235,-0.872677996249931,1.95136732208321,
2.08887289734196,-0.539344662916586,1.20601132958328,
1.86834471792544,0.0000000000000361,1.61803398874989,
0.934172358962723,-2.28470065541653,0.872677996249951,
0.93417235896272,-2.28470065541653,-0.127322003750048,
1.51152262815234,-1.95136732208318,-0.872677996249976,
1.51152262815234,-1.95136732208318,0.127322003750023,
-1.51152262815234,-1.95136732208327,0.12732200375003,
-0.93417235896271,-2.28470065541658,-0.127322003750038,
0.934172358962719,-0.127322003750011,2.28470065541656,
0.57735026918963,-0.745355992499916,2.28470065541655,
0.934172358962718,1.61803398874992,1.6180339887499,
0.577350269189622,2.07868932583328,1.20601132958331,
-0.93417235896272,2.28470065541653,0.127322003750048,
-1.51152262815234,1.95136732208318,-0.127322003750022,
-0.57735026918963,0.745355992499915,-2.28470065541655,
-0.934172358962719,0.127322003750011,-2.28470065541656,
0.934172358962715,-1.61803398874987,-1.61803398874991,
0.577350269189627,-2.07868932583324,-1.2060113295833,
1.51152262815234,-1.5393446629166,1.20601132958328,
0.934172358962725,-1.61803398874987,1.61803398874988,
2.44569498711505,-0.333333333333284,-0.872677996249975,
2.44569498711506,0.333333333333382,-0.127322003750043,
2.44569498711505,-0.333333333333284,0.127322003750025,
-0.0000000000000084,2.41202265916659,-0.539344662916623,
-0.0000000000000071,2.15737865166651,-1.20601132958328,
-2.44569498711506,-0.333333333333379,-0.872677996249958,
-2.44569498711506,-0.333333333333382,0.127322003750043,
-2.44569498711505,0.333333333333284,-0.127322003750025,
1.86834471792543,0.0000000000000326,-1.61803398874991,
2.08887289734196,0.539344662916681,-1.20601132958331,
-2.08887289734197,-1.20601132958334,-0.539344662916625,
-1.86834471792543,-1.0786893258333,-1.20601132958331,
0.0000000000000084,-2.41202265916659,0.539344662916623,
0.000000000000007,-2.15737865166652,1.20601132958329,
1.86834471792543,-1.07868932583322,-1.20601132958331,
2.08887289734196,-1.20601132958325,-0.539344662916645,
-2.08887289734197,0.539344662916586,-1.20601132958328,
-1.86834471792544,-0.0000000000000364,-1.61803398874989,
0.356822089773093,-0.872677996249963,-2.28470065541657,
-0.356822089773094,-0.872677996249968,-2.28470065541658,
-1.29099444873581,0.745355992499905,1.95136732208325,
1.29099444873581,0.745355992499957,1.95136732208324,
2.08887289734198,1.20601132958335,-0.460655337083377,
-0.00000000000001,1.49071198499985,-1.95136732208322,
-2.08887289734196,1.20601132958325,-0.460655337083354,
-2.08887289734197,-1.20601132958335,0.460655337083377,
-1.29099444873581,-0.745355992499957,-1.95136732208324,
-0.0000000000000073,2.41202265916659,0.460655337083377,
2.08887289734196,-1.20601132958325,0.460655337083353,
0.0000000000000069,-2.41202265916659,-0.460655337083377,
0.0000000000000096,-1.49071198499985,1.95136732208322,
1.29099444873581,-0.745355992499905,-1.95136732208325
];

var icosahedron_polyVerts = [
 phi2 , 0, phi3,
 -1.0*phi2 , 0, phi3,
  0, phi3 , phi2,
  0,-1.0*phi3 , phi2,
  phi3 , phi2 , 0,
 -1.0*phi3 , phi2 , 0,
 -1.0*phi3 ,-1.0*phi2 , 0,
  phi3 ,-1.0*phi2 , 0,
  0, phi3 ,-1.0*phi2,
  0,-1.0*phi3 ,-1.0*phi2,
  phi2 , 0,-1.0*phi3,
 -1.0*phi2 , 0,-1.0*phi3
];

var rhombicDodecahedron_polyVerts = [
 0, phi , phi3,
 phi,-1.0*phi2 , phi3,
-1.0*phi3 , phi, phi2,
-1.0*phi2 ,-1.0*phi2 , phi2,
 phi3 , 0, phi,
 phi2 , phi3 , phi,
-1.0*phi , phi3 , 0,
phi ,-1.0*phi3 , 0,
-1.0*phi3 , 0,-1.0*phi,
-1.0*phi2 ,-1.0*phi3 ,-1.0*phi,
 phi2 , phi2 ,-1.0*phi2,
 phi3 ,-1.0*phi,-1.0*phi2,
-1.0*phi, phi2 ,-1.0*phi3,
 0,-1.0*phi ,-1.0*phi3
];

var cube_polyVerts = [
  1,1,1,
  1,-1,1,
  -1,1,1,
  -1,-1,1,
  1,1,-1,
  1,-1,-1,
  -1,1,-1,
  -1,-1,-1
];

var dodecahedron_polyVerts = [
  1,1,1,
  1,-1,1,
  -1,1,1,
  -1,-1,1,
  1,1,-1,
  1,-1,-1,
  -1,1,-1,
  -1,-1,-1,
  0, 1.0+invphi, 1.0 - invphi2,
  0, 1.0+invphi, -1.0 + invphi2,
  0, -1.0 - invphi, 1 - invphi2,
  0, -1.0 - invphi, -1.0 + invphi2,
  1.0+invphi, 1.0 - invphi2, 0,
  1.0+invphi, -1.0 + invphi2, 0,
  -1.0 - invphi, 1 - invphi2, 0,
  -1.0 - invphi, -1.0 + invphi2, 0,
  1.0 - invphi2, 0, 1.0+invphi,
  -1.0 + invphi2, 0, 1.0+invphi,
  1 - invphi2, 0, -1.0 - invphi,
  -1.0 + invphi2, 0, -1.0 - invphi
];

var rt_polyVerts = [
      phi2, 0.0, phi3,
      0.0, phi, phi3,
      -1.0*phi2, 0.0, phi3,
      0.0,-1.0*phi, phi3,
      phi2, phi2, phi2,
      0.0, phi3, phi2,
      -1.0*phi2, phi2, phi2,
      -1.0*phi2,-1.0*phi2, phi2,
      0.0,-1.0*phi3, phi2,
      phi2,-1.0*phi2, phi2,
      phi3, 0.0, phi,
      -1.0*phi3, 0.0, phi,
      phi3, phi2,0.0,
      phi , phi3, 0.0,
      -1.0*phi , phi3, 0.0,
      -1.0*phi3, phi2, 0.0,
      -1.0*phi3,-1.0*phi2, 0.0,
      -1.0*phi ,-1.0*phi3, 0.0,
      phi ,-1.0*phi3, 0.0,
      phi3,-1.0*phi2, 0.0,
      phi3, 0.0,-1.0*phi,
      -1.0*phi3, 0.0,-1.0*phi,
      phi2, phi2,-1.0*phi2,
      0.0, phi3,-1.0*phi2,
      -1.0*phi2, phi2,-1.0*phi2,
      -1.0*phi2,-1.0*phi2,-1.0*phi2,
      0.0,-1.0*phi3,-1.0*phi2,
      phi2,-1.0*phi2,-1.0*phi2,
      phi2, 0.0,-1.0*phi3,
      0.0, phi,-1.0*phi3,
      -1.0*phi2, 0.0,-1.0*phi3,
      0.0,-1.0*phi,-1.0*phi3
];

//
// start
//
// Called when the canvas is created to get the ball rolling.
//
function start() {
  canvas = document.getElementById("glcanvas");

  initWebGL(canvas);      // Initialize the GL context

  // Only continue if WebGL is available and working

  if (gl) {
    gl.clearColor(0.0, 0.0, 0.0, 1.0);  // Clear to black, fully opaque (R,G,B,alpha) 
    gl.clearDepth(1.0);                 // Clear everything
    gl.enable(gl.DEPTH_TEST);           // Enable depth testing
    gl.depthFunc(gl.LEQUAL);            // Near things obscure far things

    // Initialize the shaders; this is where all the lighting for the
    // vertices and so forth is established.

    initShaders();

    // Here's where we call the routine that builds all the objects
    // we'll be drawing.
    poly = new Polyhedron( dodecahedron_polyVerts );
    initBuffers();

    // Set up to draw the scene periodically.

    drawInterval = setInterval(drawScene, 15);
    //drawScene();
  }
}

//
// initWebGL
//
// Initialize WebGL, returning the GL context or null if
// WebGL isn't available or could not be initialized.
//
function initWebGL() {
  gl = null;

  try {
    gl = canvas.getContext("experimental-webgl");
  }
  catch(e) {
  }

  // If we don't have a GL context, give up now

  if (!gl) {
    alert("Unable to initialize WebGL. Your browser may not support it.");
  }
}

//
// initBuffers
//
// Initialize the buffers we'll need. For this demo, we just have
// one object -- a simple two-dimensional cube.
//
function initBuffers() {

  // Create a buffer for the cube's vertices.

  cubeVerticesBuffer = gl.createBuffer();

  // Select the cubeVerticesBuffer as the one to apply vertex
  // operations to from here out.

  gl.bindBuffer(gl.ARRAY_BUFFER, cubeVerticesBuffer);

  var vertices = poly.getFaceOrderedVertices();
    
  // Now pass the list of vertices into WebGL to build the shape. We
  // do this by creating a Float32Array from the JavaScript array,
  // then use it to fill the current vertex buffer.
  gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(vertices), gl.STATIC_DRAW);

  // Now set up the colors for the faces. We'll use solid colors
  // for each face.
  setColors();

  // Build the element array buffer; this specifies the indices
  // into the vertex array for each face's vertices.
  cubeVerticesIndexBuffer = gl.createBuffer();
  gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, cubeVerticesIndexBuffer);

  // This array defines each face as two triangles, using the
  // indices into the vertex array to specify each triangle's
  // position.
  var cubeVertexIndices = poly.triangleVertices;

  // Now send the element array to GL

  gl.bufferData(gl.ELEMENT_ARRAY_BUFFER,
      new Uint16Array(cubeVertexIndices), gl.STATIC_DRAW);
}

function setColors() {
  var generatedColors = [];

  var colorSets = poly.faceGroup.coloringSets;

  if( colorSets.length == 1 ) {
    // one set, just cycle through the colors for each face.
    var faces = colorSets[0];
    for( var f = 0; f < faces.length; f++ ) {
      var c = faceColors[f%15];
      for( var v = 0; v < faces[f].vertices.length; v++ ) {
        generatedColors = generatedColors.concat( c );
      }
    }
  } else {
    for( var s = 0; s < colorSets.length; s++ ) {
      var c = faceColors[s%15];
      var faces = colorSets[s];
      for( var f = 0; f < faces.length; f++ ) {
        for( var v = 0; v < faces[f].vertices.length; v++ ) {
          generatedColors = generatedColors.concat( c );
        }
      }
    }
  }

  cubeVerticesColorBuffer = gl.createBuffer();
  gl.bindBuffer(gl.ARRAY_BUFFER, cubeVerticesColorBuffer);
  gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(generatedColors), gl.STATIC_DRAW);
}

function getRotationAxis(ra, dec) {
  var r = ra;
  var d = dec;
  var cosR = Math.cos( ra * Math.PI/180);
  var sinR = Math.sin( ra * Math.PI/180);
  var cosD = Math.cos( d * Math.PI/180);
  var sinD = Math.sin( d * Math.PI/180);
  return $V([cosR*cosD,sinR*cosD,sinD]); 
}

//
// drawScene
//
// Draw the scene.
//
function drawScene() {
  // Clear the canvas before we start drawing on it.

  var rotVec = getRotationAxis(ra, dec);

  gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);

  // Establish the perspective with which we want to view the
  // scene. Our field of view is 45 degrees, with a width/height
  // ratio of 640:480, and we only want to see objects between 0.1 units
  // and 100 units away from the camera.

  perspectiveMatrix = makePerspective(45, 640.0/480.0, .1, 100.0);

  // Set the drawing position to the "identity" point, which is
  // the center of the scene.

  loadIdentity();

  // Now move the drawing position a bit to where we want to start
  // drawing the cube.

  mvTranslate([0.0, 0.0, zoomLevel]);

  // Save the current matrix, then rotate before we draw.
  mvPushMatrix();

  mvRotate(cubeRotation, rotVec.elements);

  // Use z,x',z''
  // first rotate around the z-axis by ztheta
  ////mvRotate(rotZTheta, [0,0,1]);
  // now find the new x (x') by performing the same rotation again on the original x axis
  // also find y', the new y after the z spin.
  ////var xprime = $V([1,0,0]).rotate(rotZTheta * Math.PI / 180.0, Line.Z);
  ////var yprime = $V([0,1,0]).rotate(rotZTheta * Math.PI / 180.0, Line.Z);
  ////mvRotate(rotXTheta, xprime.elements);
  // now apply the xprime turn on yprime, and then rotate around y
  ////mvRotate(rotYTheta, yprime.rotate(rotXTheta * Math.PI / 180.0,$L([0,0,0],xprime)).elements);

  //mvTranslate([cubeXOffset, cubeYOffset, cubeZOffset]);

  if( colorChange ) {
    setColors();
    colorChange = false;
  }

  // Draw the cube by binding the array buffer to the cube's vertices
  // array, setting attributes, and pushing it to GL.
  gl.bindBuffer(gl.ARRAY_BUFFER, cubeVerticesBuffer);
  gl.vertexAttribPointer(vertexPositionAttribute, 3, gl.FLOAT, false, 0, 0);

  // Set the colors attribute for the vertices.
  gl.bindBuffer(gl.ARRAY_BUFFER, cubeVerticesColorBuffer);
  gl.vertexAttribPointer(vertexColorAttribute, 4, gl.FLOAT, false, 0, 0);

  // Draw the cube.

  gl.bindBuffer(gl.ELEMENT_ARRAY_BUFFER, cubeVerticesIndexBuffer);
  setMatrixUniforms();
  gl.drawElements(gl.TRIANGLES, poly.triangleVertices.length, gl.UNSIGNED_SHORT, 0);

  // Restore the original matrix

  mvPopMatrix();

  // Update the rotation for the next draw, if it's time to do so.

  var currentTime = (new Date).getTime();
  if (lastCubeUpdateTime ) {
    //var delta = currentTime - lastCubeUpdateTime;
    cubeRotation += spinRate;
    cubeRotation %= 360;
    //rotXTheta += rotX;
    //rotXTheta = rotXTheta % 360;
    //rotYTheta += rotY;
    //rotYTheta = rotYTheta % 360;
    //rotZTheta += rotZ;
    //rotZTheta = rotZTheta % 360;
    //cubeXOffset += xIncValue * ((30 * delta) / 1000.0);
    //cubeYOffset += yIncValue * ((30 * delta) / 1000.0);
    //cubeZOffset += zIncValue * ((30 * delta) / 1000.0);

    //if (Math.abs(cubeYOffset) > 1.5) {
    //  xIncValue = -xIncValue;
    // yIncValue = -yIncValue;
    //  zIncValue = -zIncValue;
    //}
  }

  lastCubeUpdateTime = currentTime;
}

//
// initShaders
//
// Initialize the shaders, so WebGL knows how to light our scene.
//
function initShaders() {
  var fragmentShader = getShader(gl, "shader-fs");
  var vertexShader = getShader(gl, "shader-vs");

  // Create the shader program

  shaderProgram = gl.createProgram();
  gl.attachShader(shaderProgram, vertexShader);
  gl.attachShader(shaderProgram, fragmentShader);
  gl.linkProgram(shaderProgram);

  // If creating the shader program failed, alert

  if (!gl.getProgramParameter(shaderProgram, gl.LINK_STATUS)) {
    alert("Unable to initialize the shader program.");
  }

  gl.useProgram(shaderProgram);

  vertexPositionAttribute = gl.getAttribLocation(shaderProgram, "aVertexPosition");
  gl.enableVertexAttribArray(vertexPositionAttribute);

  vertexColorAttribute = gl.getAttribLocation(shaderProgram, "aVertexColor");
  gl.enableVertexAttribArray(vertexColorAttribute);
}

//
// getShader
//
// Loads a shader program by scouring the current document,
// looking for a script with the specified ID.
//
function getShader(gl, id) {
  var shaderScript = document.getElementById(id);

  // Didn't find an element with the specified ID; abort.

  if (!shaderScript) {
    return null;
  }

  // Walk through the source element's children, building the
  // shader source string.

  var theSource = "";
  var currentChild = shaderScript.firstChild;

  while(currentChild) {
    if (currentChild.nodeType == 3) {
      theSource += currentChild.textContent;
    }

    currentChild = currentChild.nextSibling;
  }

  // Now figure out what type of shader script we have,
  // based on its MIME type.

  var shader;

  if (shaderScript.type == "x-shader/x-fragment") {
    shader = gl.createShader(gl.FRAGMENT_SHADER);
  } else if (shaderScript.type == "x-shader/x-vertex") {
    shader = gl.createShader(gl.VERTEX_SHADER);
  } else {
    return null;  // Unknown shader type
  }

  // Send the source to the shader object

  gl.shaderSource(shader, theSource);

  // Compile the shader program

  gl.compileShader(shader);

  // See if it compiled successfully

  if (!gl.getShaderParameter(shader, gl.COMPILE_STATUS)) {
    alert("An error occurred compiling the shaders: " + gl.getShaderInfoLog(shader));
    return null;
  }

  return shader;
}

//
// Matrix utility functions
//

function loadIdentity() {
  mvMatrix = Matrix.I(4);
}

function multMatrix(m) {
  mvMatrix = mvMatrix.x(m);
}

function mvTranslate(v) {
  multMatrix(Matrix.Translation($V([v[0], v[1], v[2]])).ensure4x4());
}

function setMatrixUniforms() {
  var pUniform = gl.getUniformLocation(shaderProgram, "uPMatrix");
  gl.uniformMatrix4fv(pUniform, false, new Float32Array(perspectiveMatrix.flatten()));

  var mvUniform = gl.getUniformLocation(shaderProgram, "uMVMatrix");
  gl.uniformMatrix4fv(mvUniform, false, new Float32Array(mvMatrix.flatten()));
}

var mvMatrixStack = [];

function mvPushMatrix(m) {
  if (m) {
    mvMatrixStack.push(m.dup());
    mvMatrix = m.dup();
  } else {
    mvMatrixStack.push(mvMatrix.dup());
  }
}

function mvPopMatrix() {
  if (!mvMatrixStack.length) {
    throw("Can't pop from an empty matrix stack.");
  }

  mvMatrix = mvMatrixStack.pop();
  return mvMatrix;
}

function mvRotate(angle, v) {
  var inRadians = angle * Math.PI / 180.0;

  var m = Matrix.Rotation(inRadians, $V([v[0], v[1], v[2]])).ensure4x4();
  multMatrix(m);
}

function getFaceOrderedVertices( unordered, faceordered ) {
  var retVal = [];
  var vtx = 0;
  for( var i = 0; i < faceordered.length; i++ ) {
    vtx = faceordered[i];
    retVal.push( unordered[vtx*3] );
    retVal.push( unordered[vtx*3+1] );
    retVal.push( unordered[vtx*3+2] );
  }

  return retVal;
}

//
// 0,1,3,2,1,3,
function getTriangleIndexArray() {
  var retVal = [];
  for( var i = 0; i < 120; i+=4 ) {
    retVal.push( i );
    retVal.push( i+1 );
    retVal.push( i+3 );
    retVal.push( i+2 );
    retVal.push( i+1 );
    retVal.push( i+3 );
  }

  return retVal;
}
