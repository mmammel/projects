

function Shape ( faceDefinitions, edgeDefinitions ) {
  if( faceDefinitions != null ) {
    this.drawMode = "FACES";
    this.vertexGroup = new VertexGroup( faceDefinitions, null );
    this.faceGroup = new FaceGroup( this.vertexGroup.createFaces( faceDefinitions ) );
    this.triangleVertices = this.getTriangleVertices();
    this.edgeGroup = null;
    this.lineVertices = null;
  } else if( edgeDefinitions != null ) {
    this.drawMode = "EDGES";
    this.vertexGroup = new VertexGroup( null, edgeDefinitions );
    this.faceGroup = null;
    this.triangeVertices = null;
    this.edgeGroup = null; // new EdgeGroup( this.vertextGroup.createEdges( edgeDefinitions ) );
    this.lineVertices = null; // this.getLineVertices();
  }
}

Shape.prototype.getEdgeCount = function() {
  var sideCount = this.faceGroup.getSideCount();
  return (sideCount / 2);
}

Shape.prototype.getVertexIndexArray = function() {
  var retVal = [];
  for( var i = 0; i < this.faceGroup.faces.length; i++ ) {
    retVal = retVal.concat( this.faceGroup.faces[i].idxArray );
  }

  return retVal;
}

Shape.prototype.getFaceOrderedVertices = function() {
  var retVal = [];
  var vidx = 0;
  var vertexIndices = this.getVertexIndexArray();
  for( var i = 0; i < vertexIndices.length; i++ ) {
    vidx = vertexIndices[i];
    retVal.push( this.vertexGroup.coords[ 3*vidx ] );
    retVal.push( this.vertexGroup.coords[ 3*vidx + 1 ] );
    retVal.push( this.vertexGroup.coords[ 3*vidx + 2 ] );
  }

  return retVal;
}

Shape.prototype.getTriangleVertices = function() {
  var retVal = [];
  var tempVerts = null;
  var offset = 0;
  for( var i = 0; i < this.faceGroup.faces.length; i++ ) {
    tempVerts = this.faceGroup.faces[i].getTriangleVertices();
    for( var j = 0; j < tempVerts.length; j++ ) {
      retVal.push( offset + tempVerts[j] );
    }
    offset += this.faceGroup.faces[i].vertices.length;
  }

  return retVal;
}

function Edge( vertices ) {
  this.vertices = vertices;
  this.line = $L([vertices[0],vertices[1],vertices[2]], $V([(vertices[3]-vertices[0]),(vertices[4]-vertices[1]),(vertices[5]-vertices[2])]));
  this.direction = line.direction; // normalized
}

Edge.prototype.isParallel = function(edge) {
  return this.line.isParallelTo(edge.line);
}

Edge.prototype.getLength = function() {
  return Math.sqrt( 
    Math.pow(this.vertices[3] - this.vertices[0], 2) +
    Math.pow(this.vertices[4] - this.vertices[1], 2) +
    Math.pow(this.vertices[5] - this.vertices[2], 2)
  );
}

function Face ( plane, vertices, idxArray ) {
  this.vertices = vertices;
  this.idxArray = idxArray;
  this.normal = plane.normal;
  this.plane = plane;
}

/*
 * Put the vertices in "edge order"
 * Again, could be optimized, but eh, were talking like up to ten vertices tops for this stuff.
 */
Face.prototype.sortVertices = function() {
  var maxAngle = -1, tempAngle = 0;
  var adjacencyArray = []; // holds an array of [j,k] where
                           // j and k are the two neighbors of i,
                           // where i is the index into the array.
  var tempNeighbors = [];
  if( this.vertices.length > 3 ) {
    // obviously if there are only 3 vertices we're set.
    for( var i = 0; i < this.vertices.length; i++ ) {
      maxAngle = -1;
      for( var j = 0; j < this.vertices.length - 1; j++ ) {
        if( j == i ) continue;
        for( var k = j+1; k < this.vertices.length; k++ ) {
          if( k == i ) continue;
          tempAngle = this.vertices[j].subtract( this.vertices[i] ).angleFrom( this.vertices[k].subtract( this.vertices[i] ) );
          if( tempAngle > maxAngle ) {
            maxAngle = tempAngle;
            tempNeighbors = [ j, k ];
          } 
        }
      }
      adjacencyArray.push( tempNeighbors );
    }  
    
    var newOrder = [];
    // we always start at 0
    var vidx = 0, prev = 0; // vidx is current, prev is where we came from.
    do {
      newOrder.push( vidx );
      vidx = adjacencyArray[ vidx ][ 0 ] == prev ? adjacencyArray[ vidx ][ 1 ] : adjacencyArray[ vidx ][ 0 ] ;
      prev = newOrder[ newOrder.length - 1 ];
    } while( vidx != 0 );

    var orderedVertices = [];
    var orderedIndices = [];
    for( var i = 0; i < newOrder.length; i++ ) {
      orderedVertices.push( this.vertices[ newOrder[i] ] );
      orderedIndices.push( this.idxArray[ newOrder[i] ] );
    }

    this.vertices = orderedVertices;
    this.idxArray = orderedIndices;
  }
};

Face.prototype.isOrthogonal = function( face ) {
  return this.normal.isPerpendicularTo( face.normal );
};

Face.prototype.isOpposite = function( face ) {
  return this.normal.isAntiparallelTo( face.normal );
};

Face.prototype.isParallel = function( face ) {
  return this.normal.isParallelTo( face.normal );
};

Face.prototype.getTriangleVertices = function() {
  var retVal = [];
  for( var i = 1; i < this.idxArray.length - 1; i++ ) {
    retVal.push( 0, i, i+1 );
  }
  return retVal;
}

Face.prototype.getVertexAverage = function() {
  var sumX = 0.0, sumY = 0.0, sumZ = 0.0;
  for( var i = 0; i < this.vertices.length; i++ ) {
    sumX += this.vertices[i].elements[0];
    sumY += this.vertices[i].elements[1];
    sumZ += this.vertices[i].elements[2];
  }

  return $V([ sumX / this.vertices.length, sumY / this.vertices.length, sumZ / this.vertices.length]);
}


Face.prototype.getArea = function() {
  var area = 0.0;
  var trianglePerimeter = 0.0;
  var s = 0.0;
  var side1, side2, side3;
  var v = 0;
  for( var i = 1; i < this.vertices.length - 1; i++ ) {
    // Three verts of the current triangle are v[0], v[i], v[i+1]
    trianglePerimeter = 0.0;
    trianglePerimeter += (side1 = this.vertices[0].distanceFrom( this.vertices[i] ));
    trianglePerimeter += (side2 = this.vertices[i].distanceFrom( this.vertices[i+1] ));
    trianglePerimeter += (side3 = this.vertices[i+1].distanceFrom( this.vertices[0] ));
    s = trianglePerimeter / 2.0;
    area += Math.sqrt( (s * (s - side1) * (s - side2) * (s - side3))  );
  }

  return area;
}

/**
 * Takes an array of $V vectors of dimension 2 or 3
 */
function getArea( vertices ) {
  var area = 0.0;
  var trianglePerimeter = 0.0;
  var s = 0.0;
  var side1, side2, side3;
  var v = 0;
  for( var i = 1; i < vertices.length - 1; i++ ) {
    // Three verts of the current triangle are v[0], v[i], v[i+1]
    trianglePerimeter = 0.0;
    trianglePerimeter += (side1 = vertices[0].distanceFrom( vertices[i] ));
    trianglePerimeter += (side2 = vertices[i].distanceFrom( vertices[i+1] ));
    trianglePerimeter += (side3 = vertices[i+1].distanceFrom( vertices[0] ));
    s = trianglePerimeter / 2.0;
    area += Math.sqrt( (s * (s - side1) * (s - side2) * (s - side3))  );
  }

  return area;
}

/*
 * faces: an array of generic faces, might have any number of sides.
 */
function FaceGroup ( faces ) {
  this.faces = faces;
  this.coloringSets = this.sortForColoring();
}

FaceGroup.prototype.getSideCount = function() {
  var retVal = 0;
  for( var i = 0; i < this.faces.length; i++ ) {
    retVal += this.faces[i].vertices.length;
  }
  return retVal;
}

FaceGroup.prototype.sortForColoring = function() {
  var indexArray = [];
  var sets = [];
  var tempSet = null;
  var tempFace = null;
  var face = null;
  // An array that represents the faces left in the list.
  for( var i = 0; i < this.faces.length; i++ ) indexArray.push(i);
  
  while( indexArray.length > 0 ) {
    tempSet = [];
    face = this.faces[ indexArray[indexArray.length - 1] ];
    tempSet.push( face );
    indexArray.pop();
    for( var i = indexArray.length - 1; i >= 0; i-- ) {
      tempFace = this.faces[ indexArray[i] ];
      if( face.isOpposite( tempFace ) || face.isParallel( tempFace) ) {
        tempSet.push( tempFace );
        indexArray.splice( i, 1 ); // remove this index.
      }
    }

    if( tempSet.length > 0 ) {
      sets.push( tempSet );
    }
  }

  var colorOrdered = [];
  for( var j = 0; j < sets.length; j++ ) {
    for( var k = 0; k < sets[j].length; k++ ) {
      colorOrdered.push( sets[j][k] );
    }
  }

  this.faces = colorOrdered;
  return sets;
}


function VertexGroup ( faceDefinitions, edgeDefinitions ) {
  var rawCoords = [];
  this.vertexIndex = {};
  this.coords = [];
  var tempFace;

  if( faceDefinitions != null ) {
    // flatten out the faces
    for( var f = 0; f < faceDefinitions.length; f++ ) {
      tempFace = faceDefinitions[ f ];
      for( var v = 0; v < tempFace.vertices.length; v++ ) {
        rawCoords.push( tempFace.vertices[v] );
      }
    }
  } else if( edgeDefinitions != null ) {
    // just an array of points.
    for( var v = 0; v < edgeDefinitions.length; v++ ) {
      rawCoords.push( edgeDefinitions[v] );
    }
  }
  this.vertices = [];
  var tempVertex = null;
  var tempVertexID = "";
  var xsum = 0.0, ysum = 0.0, zsum = 0.0;
  for( var i = 0; i < rawCoords.length; i+=3 ) {
    tempVertex = $V([rawCoords[i], rawCoords[i+1], rawCoords[i+2]]);
    tempVertexID = vectorToString( tempVertex );
    if( this.vertexIndex[ tempVertexID ] == null ) {
      // first time we've seen this one.
      xsum += rawCoords[i];
      ysum += rawCoords[i+1];
      zsum += rawCoords[i+2];
      this.vertexIndex[ tempVertexID ] = { vertex: tempVertex, index : this.vertices.length };
      this.vertices.push( tempVertex );
      this.coords = this.coords.concat( tempVertex.elements );
    }
  }

  this.origin = $V([ xsum, ysum, zsum ]).x( 1.0 / (1.0*this.vertices.length) ).snapTo(0);
}

VertexGroup.prototype.applyRotation = function( m ) {
  this.coords = [];
  for( var i = 0; i < this.vertices.length; i++ ) {
    this.vertices[i] = m.x( this.vertices[i] );
    this.coords = this.coords.concat( this.vertices[i].elements );
  }
}

/*
 * plane - a plane from sylvester, maybe already adorned with "D"
 * vector - a vector built around the coord to check.
 */
function planeCoordinateTest( plane, vector ) {
  if( plane.D == null ) {
    plane.D = -1 * plane.anchor.dot( plane.normal );
  }

  var result = vector.dot( plane.normal ) + plane.D;
  result = Math.abs(result) < .000001 ? 0 : result;
  return result == 0 ? 0 : (result/Math.abs(result)); // return [-1|0|1]
}

/**
 * We have:
 *  1. faceDefinitions: an array of objects, each of which defines the x0,y0,z0,x1,y1,z1,...,xN,yN,zN face vertices.
 *  2. this.vertices: An array of $V objects defining the *distinct* vertices found in the faceDefinitions
 *  3. this.vertexIndex: A lookup table that we can use to find the index into the vertices array corresponding 
 *     to a particular face vertex.
 *
 * This will let us translate the faces into groups of indices into the vertex array.
 */
VertexGroup.prototype.createFaces = function( faceDefinitions ) {
  var tempFaceDef = null;
  var tempVertex = null;
  var tempPlane = null;
  var tempVertexID = '';
  var tempVertexIndex = null;
  var tempFaceVertices = [];
  var tempFace = null;
  var result = [];
  var x = 0.0, y = 0.0, z = 0.0;
  for( var i = 0; i < faceDefinitions.length; i++ ) {
    tempFaceDef = faceDefinitions[i];
    for( var j = 0; j < tempFaceDef.vertices.length; j+=3 ) {
      x = tempFaceDef.vertices[j];
      y = tempFaceDef.vertices[j+1];
      z = tempFaceDef.vertices[j+2];
      tempVertex = $V([x,y,z]);
      tempFaceVertices.push(tempVertex);
    }
    tempPlane = $P( tempFaceVertices[0], tempFaceVertices[1], tempFaceVertices[2] );
    tempFace = new Face( tempPlane, [], [] );
    for( var k = 0; k < tempFaceVertices.length; k++ ) {
      tempVertexID = vectorToString( tempFaceVertices[k] );
      tempVertexIndex = this.vertexIndex[ tempVertexID ];
      if( tempVertexIndex != null ) {
        tempFace.idxArray.push( tempVertexIndex.index );
        tempFace.vertices.push( tempVertexIndex.vertex );
      }
    }
    tempFace.sortVertices();
    result.push( tempFace );
    tempFaceVertices = [];
  }

  return result;
}

VertexGroup.prototype.findFaces = function() {
  var tempPlane = null;
  var tempTest = 0;
  var test = 0;
  var goodPlanes = [];
  //var goodPlanesIndex = {};
  var face = false;
  for( var i = 0; i < this.vertices.length - 2; i++ ) {
    for( var j = i+1; j < this.vertices.length - 1; j++ ) {
      for( var k = j+1; k < this.vertices.length; k++ ) {
        tempPlane = $P( this.vertices[i], this.vertices[j], this.vertices[k] );
        if( tempPlane == null ) {
          // possible with bogus verts.
          face = false;
        } else {
          test = 0;
          face = true;
          for( var z = 0; z < this.vertices.length; z++ ) {
            if( z == i || z == j || z == k ) continue;
            tempTest = planeCoordinateTest(tempPlane,this.vertices[z]);
            if( tempTest == 0 ) continue;
            if( test == 0 ) {
              test = tempTest;
            } else if( test != tempTest ) {
              face = false;
              break;
            }
          }
        }

        if( face ) {
          //var faceKey = planeToString( tempPlane ); 
          //if( goodPlanesIndex[ faceKey ] !== true ) {
          //  goodPlanes.push( tempPlane );
          //  goodPlanesIndex[ faceKey ] = true;
          //}
           
          for( var p = 0; p < goodPlanes.length; p++ ) {
            if( tempPlane.eql( goodPlanes[p] ) ) {
              // already found it.
              face = false;
              break;
            }
          }
          if( face ) {
            goodPlanes.push( tempPlane );
          }
        }
      }
    }
  }

  // OK - now we have an array of planes that define the convex hull of the vertices.
  // Now we have to group the vertices arrays by which plane they fall into.
  var result = []; // this is an array of faces.
  var verticesUsed = [];
  for( var u = 0; u < this.vertices.length; u++ ) {
    verticesUsed.push( false );
  }
  var tempFace = null;
  for( var f = 0; f < goodPlanes.length; f++ ) {
    tempFace = new Face( goodPlanes[f], [], [] );
    result.push( tempFace );
    for( var v = 0; v < this.vertices.length; v++ ) {
      if( goodPlanes[f].contains( this.vertices[v] ) ) {
        verticesUsed[v] = true;
        tempFace.idxArray.push( v );
        tempFace.vertices.push( this.vertices[v] );
      } 
    }

    tempFace.sortVertices();
  }

  // Alert if we have unused vertices, the render will fail.
  var concavity = false;
  var alertMsg = "Concavity detected - following vertices not used:\n";
  for( var uv = 0; uv < verticesUsed.length; uv++ ) {
    if( !verticesUsed[uv] ) {
      concavity = true;
      alertMsg += ("(" + this.vertices[uv].elements[0] + "," + this.vertices[uv].elements[1] +"," +this.vertices[uv].elements[2] + ")\n");
    }
  }

  alertMsg += "\nNote that the reported vertex count will include the ignored vertices, but they will be ignored while rendering."

  if( concavity ) alert( alertMsg );

  return result;
}

function getMidpoint( v1, v2 ) {
  var x = (v1.elements[0] + v2.elements[0])/2.0;
  var y = (v1.elements[1] + v2.elements[1])/2.0;
  var z = (v1.elements[2] + v2.elements[2])/2.0;
  return $V([x,y,z]);
}

function getOneThirdPoint( v1, v2 ) {
  var x = (2.0*v1.elements[0] + v2.elements[0])/3.0;
  var y = (2.0*v1.elements[1] + v2.elements[1])/3.0;
  var z = (2.0*v1.elements[2] + v2.elements[2])/3.0;
  return $V([x,y,z]);
}

function getTwoThirdsPoint( v1, v2 ) {
  var x = (v1.elements[0] + 2.0*v2.elements[0])/3.0;
  var y = (v1.elements[1] + 2.0*v2.elements[1])/3.0;
  var z = (v1.elements[2] + 2.0*v2.elements[2])/3.0;
  return $V([x,y,z]);
}

function precisionCompare( a, b ) {
  var equals = false;
  if( Math.abs( a - b ) < .001 ) {
    equals = true;
  }

  return equals;
}

function planeToString( plane ) {
  return "p"+vectorToString( plane.normal ) + plane.D;
}

function vectorToString( v ) {
 return "v"+Math.round(1000*v.elements[0])/1000+"_"+Math.round(1000*v.elements[1])/1000+"_"+Math.round(1000*v.elements[2])/1000;
}

