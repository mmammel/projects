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

var mvMatrix;
var shaderProgram;
var vertexPositionAttribute;
var vertexColorAttribute;
var perspectiveMatrix;

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

    initBuffers();

    // Set up to draw the scene periodically.

    setInterval(drawScene, 15);
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

  // Now create an array of vertices for the cube.

  var unorderedVertices = [
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

  var vertexGroup = new VertexGroup( unorderedVertices );
  var autoFaces = vertexGroup.findFaces();

  var unorderedFaceGroupings = [
  0,3,8,9,
  0,1,5,4,
  0,1,2,3,
  0,4,12,10,
  0,10,19,9,
  12,4,5,13,
  12,13,23,22,
  12,22,28,20,
  12,20,19,10,
  16,7,2,11,
  16,11,15,21,
  16,21,30,25,
  16,25,26,17,
  16,17,8,7,
  8,3,2,7,
  8,17,26,18,
  8,18,19,9,
  26,18,19,27,
  26,27,28,31,
  26,31,30,25,
  2,1,5,6,
  2,6,15,11,
  23,14,15,24,
  23,24,30,29,
  23,29,28,22,
  23,13,5,14,
  5,6,15,14,
  15,21,30,24,
  28,20,19,27,
  28,29,30,31
    ];

  var faceGroup = new FaceGroup( unorderedVertices, unorderedFaceGroupings );

  faceGroupings = faceGroup.buildColorOrderedArray();

  var vertices = getFaceOrderedVertices( unorderedVertices, faceGroupings );
    
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
  var cubeVertexIndices = getTriangleIndexArray( faceGroupings );

  // Now send the element array to GL

  gl.bufferData(gl.ELEMENT_ARRAY_BUFFER,
      new Uint16Array(cubeVertexIndices), gl.STATIC_DRAW);
}

function setColors() {
  var generatedColors = [];

  for (var j=0; j<5; j++) {
    var c = faceColors[j];
    for( var k = 0; k < 6; k++ ) {
      // Repeat each color four times for the four vertices of the face
      for (var i=0; i<4; i++) {
        generatedColors = generatedColors.concat(c);
      }
    }
  }

  cubeVerticesColorBuffer = gl.createBuffer();
  gl.bindBuffer(gl.ARRAY_BUFFER, cubeVerticesColorBuffer);
  gl.bufferData(gl.ARRAY_BUFFER, new Float32Array(generatedColors), gl.STATIC_DRAW);
}

//
// drawScene
//
// Draw the scene.
//
function drawScene() {
  // Clear the canvas before we start drawing on it.

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
  mvRotate(cubeRotation, [0.5, 0.2, 1]);
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
  gl.drawElements(gl.TRIANGLES, 180, gl.UNSIGNED_SHORT, 0);

  // Restore the original matrix

  mvPopMatrix();

  // Update the rotation for the next draw, if it's time to do so.

  var currentTime = (new Date).getTime();
  if (lastCubeUpdateTime) {
    var delta = currentTime - lastCubeUpdateTime;

    cubeRotation += (30 * delta) / 1000.0;
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