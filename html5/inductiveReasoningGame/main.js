const touchContext = {
  prevTime : (new Date()).getTime(),
  prevX : 0,
  prevY : 0,
  lastRate : 0.0,
  dragging : false
};

class Game {
  constructor(containerId,numShapes) {
    this.containerId = containerId;
    this.svgRoot = null;
    this.viewBox = [0,0,360,360];
    this.ctx = null;
    this.shapes = [];
    this.dimension = 0;
    this.radius = 0;
    this.rotationAngle = 0;
    this.rotationRate = 0;
    this.numShapes = numShapes;

    this.guideSphere = {
      centerX: 0,
      centerY: 0,
      radius: 0
    };

    this.handleResize();
    this.rotationAxis = $V([0,0,1]).toUnitVector();
    //this.rotationAxis = this.getRotationAxisFromPoints(this.dimension/2, 0, this.dimension/2, this.dimension/2 );

    var shapeTypes = [ 't', 'p', 'c' ];

    for( var i = 0; i < this.numShapes; i++ ) {
      this.shapes.push( new Shape(this, shapeTypes[i%3], generateRandomSphericalCoord(this.radius)));
    }
    //this.shapes.push( new Shape(this,'p', generateRandomSphericalCoord(this.radius)));
    //this.shapes.push( new Shape(this,'c', generateRandomSphericalCoord(this.radius)));
  }

  initializeHandlers = () => {
    $('#canvasRoot').on( "touchstart", (event) => {
      event.preventDefault();
      touchContext.prevX = event.originalEvent.touches[0].pageX;
      touchContext.prevY = event.originalEvent.touches[0].pageY;
      var d = new Date();
      touchContext.prevTime = d.getTime();
      touchContext.dragging = true;
      touchContext.lastRate = 0.0;
     }).on("touchend", (event) => {
      event.preventDefault();
      touchContext.currX = event.originalEvent.changedTouches[0].pageX;
      touchContext.currY = event.originalEvent.changedTouches[0].pageY;
      var d = new Date();
      touchContext.currTime = d.getTime();
      touchContext.dragging = false;
      this.processTouch();
     }).on( "mousedown", (event) => {
      touchContext.prevX = event.pageX;
      touchContext.prevY = event.pageY;
      var d = new Date();
      touchContext.prevTime = d.getTime();
      touchContext.dragging = true;
      touchContext.lastRate = 0.0;
     }).on("mouseup", (event) => {
      touchContext.currX = event.pageX;
      touchContext.currY = event.pageY;
      var d = new Date();
      touchContext.currTime = d.getTime();
      touchContext.dragging = false;
      this.processTouch();
     }).on("mousemove", (event) => {
      if( touchContext.dragging ) {
        event.preventDefault();
        touchContext.currX = event.pageX;
        touchContext.currY = event.pageY;
        var d = new Date();
        touchContext.currTime = d.getTime();
        this.processTouch();
      }
     }).on("touchmove", (event) => {
      if( touchContext.dragging ) {
        event.preventDefault();
        touchContext.currX = event.originalEvent.changedTouches[0].pageX;
        touchContext.currY = event.originalEvent.changedTouches[0].pageY;
        var d = new Date();
        touchContext.currTime = d.getTime();
        this.processTouch();
      }
     });
  }

  handleResize = () => {
    this.dimension = window.innerHeight - $('#canvas').offset().top - 40;
    this.radius = .9 * (this.dimension / 2.0);
    $('#canvas').css('width',''+this.dimension+'px');
    $('#canvasRoot').css('height',''+this.dimension+'px');
    $('#canvasRoot').css('width',''+this.dimension+'px');
    var c = document.getElementById("canvasRoot");
    c.height = this.dimension;
    c.width = this.dimension;
    this.ctx = c.getContext("2d");

    for( var i = 0; i < this.shapes.length; i++ ) {
      this.shapes[i].radiusChange(this.radius);
    }
  }

  drawCanvas() {
    
    this.ctx.fillStyle = "#1F5452";
    this.ctx.fillRect(0,0,this.dimension,this.dimension);

    // Create gradient
    var grd = this.ctx.createLinearGradient(0, 0, 0, this.dimension/2);
    grd.addColorStop(0, "#143532");
    grd.addColorStop(1, "#1F5452");
    
    // Fill with gradient
    this.ctx.fillStyle = grd;
    this.ctx.fillRect(0, 0, this.dimension/2, this.dimension/2);

    // circles
    this.ctx.lineWidth = 5;
    this.ctx.strokeStyle = '#1FB399';
    this.ctx.beginPath();
    this.ctx.arc(this.dimension/2, this.dimension/2, .18*(this.dimension/2), 0, 2 * Math.PI);
    this.ctx.stroke();
    this.ctx.beginPath();
    this.ctx.arc(this.dimension/2, this.dimension/2, .36*(this.dimension/2), 0, 2 * Math.PI);
    this.ctx.stroke();
    this.ctx.beginPath();
    this.ctx.arc(this.dimension/2, this.dimension/2, .54*(this.dimension/2), 0, 2 * Math.PI);
    this.ctx.stroke();
    this.ctx.beginPath();
    this.ctx.arc(this.dimension/2, this.dimension/2, .72*(this.dimension/2), 0, 2 * Math.PI);
    this.ctx.stroke();
    this.ctx.beginPath();
    this.ctx.arc(this.dimension/2, this.dimension/2, .90*(this.dimension/2.0), 0, 2 * Math.PI);
    this.ctx.stroke();

    for( var i = 0; i < this.shapes.length; i++ ) {
      this.shapes[i].draw();
    }
  }

  clearCanvas() {
    this.ctx.clearRect(0,0,this.dimension,this.dimension);
  }

  getSphereCoordFromPageCoord( pageX, pageY ) {
    var realX = pageX - ($('#canvasRoot').position().left + this.dimension/2.0);
    var realY = this.dimension/2.0 - (pageY - $('#canvasRoot').position().top);
    // get distance from the center, when looking down the z-axis
    var littleR = Math.sqrt( Math.pow( realX, 2) + Math.pow( realY, 2 ) );
    if( littleR > this.radius ) littleR = this.radius;
    var realZ = Math.sqrt( Math.pow( this.radius, 2) - Math.pow( littleR, 2) );
    console.log( '('+realX+','+realY+','+realZ+')');
    return $V([realX, realY, realZ]).toUnitVector();
  }

  getRotationAxisFromPoints( startX, startY, endX, endY ) {
    var vecA = this.getSphereCoordFromPageCoord( startX, startY );
    var vecB = this.getSphereCoordFromPageCoord( endX, endY );
    var cross = vecA.cross( vecB ).toUnitVector();
    console.log('Rot Axis: (' + cross.e(1) + ',' + cross.e(2) + ',' + cross.e(3) + ')' );
    return cross;
  }

  processTouch() {
    var dT = touchContext.currTime - touchContext.prevTime;
    var dP = Math.sqrt( Math.pow((touchContext.currX - touchContext.prevX),2) + Math.pow((touchContext.currY - touchContext.prevY),2));
  
    if( dP > 1.0 && dT > 1 ) {
      this.rotationAxis = this.getRotationAxisFromPoints( touchContext.prevX, touchContext.prevY, touchContext.currX, touchContext.currY );
      this.rotationRate = 0.1 * (dP / dT);
      touchContext.lastRate = this.rotationRate;
      touchContext.prevTime = touchContext.currTime;
      touchContext.prevX = touchContext.currX;
      touchContext.prevY = touchContext.currY;
    } else if( touchContext.dragging ) {
      this.rotationRate = 0;
      touchContext.lastRate = 0.0;
    } else {
      this.rotationRate = touchContext.lastRate;
    }
  }
}

class Shape {
  constructor(game, shape, sphericalCoord) {
    this.game = game;
    this.shape = shape; // t, p, or, c (triangle, pentagon, circle) 
    this.sphericalCoord = sphericalCoord;
    this.coord = $V(sphericalToCartesian(sphericalCoord));
  }

  radiusChange( newRadius ) {
    var x = this.coord.elements[0];
    var y = this.coord.elements[1];
    var z = this.coord.elements[2];

    var theta = 0;
    if( x == 0 ) {
      if( y > 0 ) {
        theta = Math.PI / 2.0;
      } else {
        theta = 3.0 * (Math.PI / 2.0 );
      }
    } else {
      theta = Math.atan( y / x );
      //if( theta < 0 ) theta += 2.0 * Math.PI;
    }

    var phi = Math.acos( z / Math.sqrt( Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z,2) ));
    var rho = newRadius;
    this.coord = $V(sphericalToCartesian([rho,theta,phi]));
  }

  applyRotation( m ) {
    this.coord = m.x( this.coord ); 
  }

  draw() {
    // scale the size based on how far away it is.
    var size = ((this.coord.elements[2] + this.game.radius) + (2.0 * this.game.radius)) / (4.0 * this.game.radius);
    size = size * this.game.dimension * .02;

    if( this.shape === 't' ) {
      drawPolygon( this.game.ctx, 3, size, this.coord.elements[0] + (this.game.dimension/2), (this.game.dimension/2) - this.coord.elements[1] );
    } else if( this.shape === 'p' ) {
      drawPolygon( this.game.ctx, 5, size, this.coord.elements[0] + (this.game.dimension/2), (this.game.dimension/2) - this.coord.elements[1] );
    } else if( this.shape === 'c' ) {
      drawCircle( this.game.ctx, size, this.coord.elements[0] + (this.game.dimension/2), (this.game.dimension/2) - this.coord.elements[1] );
    }
  }
}

function generateRandomSphericalCoord( radius ) {
  return [ radius, Math.random() * 2 * Math.PI, Math.random() * Math.PI ];
}

function sphericalToCartesian( coord ) {
  var rho = coord[0];
  var theta = coord[1];
  var phi = coord[2];
  var x = rho * Math.sin(phi) * Math.cos(theta);
  var y = rho * Math.sin(phi) * Math.sin(theta);
  var z = rho * Math.cos(phi);
  return [ x, y, z ];
}

function drawPolygon(ctx, sides, sz, x, y ) {

  var numberOfSides = sides,
      size = sz,
      Xcenter = x,
      Ycenter = y,
      step  = 2 * Math.PI / numberOfSides,//Precalculate step value
      shift = sides == 3 ? (Math.PI / 180.0) * 30 : (Math.PI / 180.0) * -18;//Quick fix ;)

  ctx.beginPath();
  //ctx.moveTo (Xcenter +  size * Math.cos(0), Ycenter +  size *  Math.sin(0));          

  for (var i = 0; i <= numberOfSides;i++) {
    var curStep = i * step + shift;
    ctx.lineTo (Xcenter + size * Math.cos(curStep), Ycenter + size * Math.sin(curStep));
  }

  ctx.fillStyle = "#FFFFFF";
  ctx.lineWidth = 1;
  ctx.fill();
}

function drawCircle( ctx, radius, x, y ) {
  ctx.beginPath();
  ctx.arc(x, y, radius, 0, 2 * Math.PI);
  ctx.fillStyle = "#FFFFFF";
  ctx.lineWidth = 1;
  ctx.fill();
}

function animate() {
  theCanvasGame.clearCanvas();
  theCanvasGame.drawCanvas();
  //console.log("Rotating at " + theCanvasGame.rotationAngle + " around (" +theCanvasGame.rotationAxis.e(1) + ',' + theCanvasGame.rotationAxis.e(2) + ',' + theCanvasGame.rotationAxis.e(3) + ')' );
  var m = Matrix.Rotation(theCanvasGame.rotationAngle, theCanvasGame.rotationAxis);
  theCanvasGame.rotationAngle = theCanvasGame.rotationRate;
  if( theCanvasGame.rotationAngle >= 2.0 * Math.PI ) {
    theCanvasGame.rotationAngle = 0.0;
  }
  for( var i = 0; i < theCanvasGame.shapes.length; i++ ) {
    theCanvasGame.shapes[i].applyRotation(m);
  }
  window.requestAnimationFrame( animate );
}

function makeSVG(tag, attrs) {
  var el= document.createElementNS('http://www.w3.org/2000/svg', tag);
  for (var k in attrs)
    el.setAttribute(k, attrs[k]);
  return el;
}