const touchContext = {
  prevTime : (new Date()).getTime(),
  prevX : 0,
  prevY : 0,
  lastRate : 0.0,
  dragging : false,
  clickTime : null,
  inDblClick : false // are we in a sustained double click?
};

class StarField {
  constructor(containerId,numShapes) {
    this.containerId = containerId;
    this.svgRoot = null;
    this.viewBox = [0,0,360,360];
    this.ctx = null;
    this.stars = [];
    this.dimensionX = 0;
    this.dimensionY = 0;
    this.radius = 0;
    this.rotationAngle = 0;
    this.rotationRate = 0;
    this.numShapes = numShapes;
    this.lightYearRadius = 100;

    this.handleResize();
    this.rotationAxis = $V([0,0,1]).toUnitVector();
    //this.rotationAxis = this.getRotationAxisFromPoints(this.dimension/2, 0, this.dimension/2, this.dimension/2 );

    // for( var i = 0; i < this.numShapes; i++ ) {
    //   this.shapes.push( new Shape(this, shapeTypes[i%3], generateRandomSphericalCoord(this.radius)));
    // }
    //this.shapes.push( new Shape(this,'p', generateRandomSphericalCoord(this.radius)));
    //this.shapes.push( new Shape(this,'c', generateRandomSphericalCoord(this.radius)));
  }

  loadConstellations = (cons) => {
    this.stars = [];
    var tempStars = null;
    var maxDist = 0;
    for( var i = 0; i < cons.length; i++ ) {
      tempStars = starDataMap[cons[i]];
      this.stars = this.stars.concat(tempStars);
    }

    for( var s = 0; s < this.stars.length; s++ ) {
      if( this.stars[s].distance > maxDist ) {
        maxDist = this.stars[s].distance;
      }
    }

    this.lightYearRadius = maxDist;
    for( var s = 0; s < this.stars.length; s++ ) {
      this.stars[s].radiusChange(this.radius);
    }
  }

  initializeHandlers = () => {
    $('#canvasRoot').on( "touchstart", (event) => {
      event.preventDefault();
      touchContext.prevX = event.originalEvent.touches[0].pageX;
      touchContext.prevY = event.originalEvent.touches[0].pageY;
      var d = new Date();
      touchContext.prevTime = d.getTime();
      if( d.getTime() - touchContext.clickTime < 250 ) {
        touchContext.inDblClick = true;
        console.log("In touch double click!");
      }
      touchContext.clickTime = d.getTime();
      touchContext.dragging = true;
      touchContext.lastRate = 0.0;
     }).on("touchend", (event) => {
      event.preventDefault();
      touchContext.currX = event.originalEvent.changedTouches[0].pageX;
      touchContext.currY = event.originalEvent.changedTouches[0].pageY;
      var d = new Date();
      touchContext.currTime = d.getTime();
      touchContext.dragging = false;
      touchContext.inDblClick = false;
      if( touchContext.currTime - touchContext.clickTime < 250 ) {
        touchContext.clickTime = touchContext.currTime;
      } 
      this.processTouch();
     }).on( "mousedown", (event) => {
      touchContext.prevX = event.pageX;
      touchContext.prevY = event.pageY;
      var d = new Date();
      touchContext.prevTime = d.getTime();
      if( d.getTime() - touchContext.clickTime < 250 ) {
        touchContext.inDblClick = true;
        console.log("In mouse double click!");
      }
      touchContext.clickTime = d.getTime();
      touchContext.dragging = true;
      touchContext.lastRate = 0.0;
     }).on("mouseup", (event) => {
      touchContext.currX = event.pageX;
      touchContext.currY = event.pageY;
      var d = new Date();
      touchContext.currTime = d.getTime();
      touchContext.dragging = false;
      touchContext.inDblClick = false;
      if( touchContext.currTime - touchContext.clickTime < 250 ) {
        touchContext.clickTime = touchContext.currTime;
      } 
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

  resetStars = () => {
    for( var i = 0; i < this.stars.length; i++ ) {
      this.stars[i].reset();
    }
  }

  changeLightYearRadius = (lightYearRadius) => {
    this.lightYearRadius = lightYearRadius;
    for( var i = 0; i < this.stars.length; i++ ) {
      this.stars[i].radiusChange(this.radius);
    }
  }

  handleResize = () => {
    this.dimensionY = $(window).innerHeight() - $('#controlPanel').height() - 10;
    this.dimensionX = $('#canvas').width();
    this.radius = .9 * (Math.max(this.dimensionX,this.dimensionY) / 2.0);

    $('#canvas').css('height',''+this.dimensionY+'px');
    //$('#canvasRoot').css('height',''+this.dimensionY+'px');
    //$('#canvasRoot').css('width',''+this.dimensionX+'px');
    var c = document.getElementById("canvasRoot");
    c.height = this.dimensionY;
    c.width = this.dimensionX;
    this.ctx = c.getContext("2d");

    for( var i = 0; i < this.stars.length; i++ ) {
      this.stars[i].radiusChange(this.radius);
    }
  }

  drawCanvas() {
    
    this.ctx.fillStyle = "#000000";
    this.ctx.fillRect(0,0,this.dimensionX,this.dimensionY);

    // earth
    this.ctx.strokeStyle = 'blue';
    this.ctx.beginPath();
    this.ctx.arc(this.dimensionX/2, this.dimensionY/2, .02*(this.dimensionY/2), 0, 2 * Math.PI);
    this.ctx.stroke();

    for( var i = 0; i < this.stars.length; i++ ) {
      this.stars[i].draw();
    }
  }

  clearCanvas() {
    this.ctx.clearRect(0,0,this.dimensionX,this.dimensionY);
  }

  getSphereCoordFromPageCoord( pageX, pageY ) {
    var realX = pageX - ($('#canvasRoot').position().left + this.dimensionX/2.0);
    var realY = this.dimensionY/2.0 - (pageY - $('#canvasRoot').position().top);
    // get distance from the center, when looking down the z-axis
    var littleR = Math.sqrt( Math.pow( realX, 2) + Math.pow( realY, 2 ) );
    if( littleR > this.radius ) littleR = this.radius;
    var realZ = Math.sqrt( Math.pow( this.radius, 2) - Math.pow( littleR, 2) );
    //console.log( '('+realX+','+realY+','+realZ+')');
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
      var zoomInOrOut = 1; // positive is in
      if( touchContext.inDblClick ) {
        if( touchContext.prevY > touchContext.currY ) {
          zoomInOrOut = -1;
        }

        var newlightYearRadius = this.lightYearRadius + (zoomInOrOut * 250.0 * (dP / dT));
        if( newlightYearRadius < 100 ) {
          newlightYearRadius = 100;
        } else if( newlightYearRadius > maxDistance ) {
          newlightYearRadius = maxDistance;
        }

        console.log("handling zoom: " + newlightYearRadius);

        this.changeLightYearRadius(newlightYearRadius);
      } else {
        this.rotationAxis = this.getRotationAxisFromPoints( touchContext.prevX, touchContext.prevY, touchContext.currX, touchContext.currY );
        this.rotationRate = 0.1 * (dP / dT);
        touchContext.lastRate = this.rotationRate;
        touchContext.prevTime = touchContext.currTime;
        touchContext.prevX = touchContext.currX;
        touchContext.prevY = touchContext.currY;
      }
    } else if( touchContext.dragging ) {
      this.rotationRate = 0;
      touchContext.lastRate = 0.0;
    } else {
      this.rotationRate = touchContext.lastRate;
    }
  }
}

class Star {
  constructor(starField, starData ) {
    this.starField = starField;
    this.constellation = starData[0];

    this.distance = starData[3];
    this.theta = starData[1];
    this.phi = starData[2];
    this.magnitude = starData[4];

    // scale the radius with the starField...
    //this.sphericalCoord = [ (starData[3] / this.starField.lightYearRadius) * this.starField.radius, starData[1], starData[2] ];
    this.sphericalCoord = [ this.starField.radius, starData[1], starData[2] ];
    //this.sphericalCoord = [ this.starField.radius, starData[1], starData[2] ];
    this.coord = $V(sphericalToCartesian(this.sphericalCoord));
    this.rootCoord = $V([this.coord.e(1),this.coord.e(2),this.coord.e(3)]);
    this.quadrantBoundaries = [
      [Math.PI / 2.0, 0 ],
      [Math.PI, Math.PI / 2.0],
      [3.0 * (Math.PI / 2.0), Math.PI],
      [2.0 * Math.PI, 3.0 * (Math.PI / 2) ]
    ];
  }

  reset() {
    this.coord = $V([this.rootCoord.e(1),this.rootCoord.e(2),this.rootCoord.e(3)]);
  }

  radiusChange( newRadius ) {
    var x = this.coord.elements[0];
    var y = this.coord.elements[1];
    var z = this.coord.elements[2];
    var quadrant = 0;

    if( x > 0 && y > 0 ) {
      quadrant = 0;
    } else if( x < 0 && y > 0 ) {
      quadrant = 1;
    } else if( x < 0 && y < 0 ) {
      quadrant = 2;
    } else if( x > 0 && y < 0 ) {
      quadrant = 3;
    }

    var theta = 0;
    if( x == 0 ) {
      if( y > 0 ) {
        theta = Math.PI / 2.0;
      } else {
        theta = 3.0 * (Math.PI / 2.0 );
      }
    } else {
      theta = this.adjustThetaToQuadrant(Math.atan( y / x ), quadrant);
    }

    var phi = Math.acos( z / Math.sqrt( Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z,2) ));
    
    var rho = newRadius;
    if( !flattenMode ) {
      rho = bufferDistance + (this.distance / this.starField.lightYearRadius) * newRadius;
    }
    this.coord = $V(sphericalToCartesian([rho,theta,phi]));
  }

  adjustThetaToQuadrant( theta, quadrant ) {
    // quadrant 0 by default.
    var lt = this.quadrantBoundaries[quadrant][0];
    var gt = this.quadrantBoundaries[quadrant][1];

    while( theta >= lt || theta <= gt ) {
      theta += (Math.PI / 2.0);
    }

    return theta;
  }

  applyRotation( m ) {
    this.coord = m.x( this.coord ); 
  }

  draw() {
    // scale the size based on how far away it is.
    var size = 6 * ( 1 - ((this.magnitude + -1*maxMagnitude)/(minMagnitude + -1*maxMagnitude)));
    drawCircle( this.starField.ctx, size, this.coord.elements[0] + (this.starField.dimensionX/2), (this.starField.dimensionY/2) - this.coord.elements[1] );
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

function drawCircle( ctx, radius, x, y ) {
  ctx.beginPath();
  ctx.arc(x, y, radius, 0, 2 * Math.PI);
  ctx.fillStyle = "#FFFFFF";
  ctx.lineWidth = 1;
  ctx.fill();
}

function animate() {
  theStarField.clearCanvas();
  theStarField.drawCanvas();
  //console.log("Rotating at " + theCanvasGame.rotationAngle + " around (" +theCanvasGame.rotationAxis.e(1) + ',' + theCanvasGame.rotationAxis.e(2) + ',' + theCanvasGame.rotationAxis.e(3) + ')' );
  var m = Matrix.Rotation(theStarField.rotationAngle, theStarField.rotationAxis);
  theStarField.rotationAngle = theStarField.rotationRate;
  if( theStarField.rotationAngle >= 2.0 * Math.PI ) {
    theStarField.rotationAngle = 0.0;
  }

  for( var i = 0; i < theStarField.stars.length; i++ ) {
    theStarField.stars[i].applyRotation(m);
  }
  window.requestAnimationFrame( animate );
}
