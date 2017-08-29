
function Polyhedron ( coords ) {
  this.vertexGroup = new VertexGroup( coords );
  this.faceGroup = new FaceGroup( this.vertexGroup.findFaces() );
  this.triangleVertices = this.getTriangleVertices();
}

Polyhedron.prototype.getEdgeCount = function() {
  var sideCount = this.faceGroup.getSideCount();
  return (sideCount / 2);
}

Polyhedron.prototype.getVertexIndexArray = function() {
  var retVal = [];
  for( var i = 0; i < this.faceGroup.faces.length; i++ ) {
    retVal = retVal.concat( this.faceGroup.faces[i].idxArray );
  }

  return retVal;
}

Polyhedron.prototype.getEdgeMidpointVertices = function() {
  var retVal = [];
  var faceMPVs = [];
  var vertexKey = null;
  var uniqueChecker = {};
  for( var i = 0; i < this.faceGroup.faces.length; i++ ) {
    faceMPVs = this.faceGroup.faces[i].getEdgeMidpointVertices();
    for( var j = 0; j < faceMPVs.length; j++ ) {
      vertexKey = "v"+faceMPVs[j].elements[0] + "_" + faceMPVs[j].elements[1] + "_" +faceMPVs[j].elements[2];
      if( !uniqueChecker[ vertexKey ] ) {
        uniqueChecker[ vertexKey ] = true;
        retVal.push(faceMPVs[j].elements[0], faceMPVs[j].elements[1], faceMPVs[j].elements[2]);
      }
    }
  }  
  return retVal;
}

Polyhedron.prototype.getEdgeTrisectionVertices = function() {
  var retVal = [];
  var faceTRVs = [];
  var vertexKey = null;
  var uniqueChecker = {};
  for( var i = 0; i < this.faceGroup.faces.length; i++ ) {
    faceTRVs = this.faceGroup.faces[i].getEdgeTrisectionVertices();
    for( var j = 0; j < faceTRVs.length; j++ ) {
      vertexKey = "v"+faceTRVs[j].elements[0] + "_" + faceTRVs[j].elements[1] + "_" +faceTRVs[j].elements[2];
      if( !uniqueChecker[ vertexKey ] ) {
        uniqueChecker[ vertexKey ] = true;
        retVal.push(faceTRVs[j].elements[0], faceTRVs[j].elements[1], faceTRVs[j].elements[2]);
      }
    }
  }  
  return retVal;
}

Polyhedron.prototype.getCentroidVertices = function() {
  var retVal = [];
  var tempVertex = null;
  for( var i = 0; i < this.faceGroup.faces.length; i++ ) {
    tempVertex = this.faceGroup.faces[i].getCentroid();
    retVal.push( tempVertex.elements[0],tempVertex.elements[1],tempVertex.elements[2] );
  }

  return retVal;
}

Polyhedron.prototype.getFaceAverageVertices = function() {
  var retVal = [];
  var tempVertex = null;
  for( var i = 0; i < this.faceGroup.faces.length; i++ ) {
    tempVertex = this.faceGroup.faces[i].getVertexAverage();
    retVal.push( tempVertex.elements[0],tempVertex.elements[1],tempVertex.elements[2] );
  }

  return retVal;
}

Polyhedron.prototype.getExplodedVertices = function( xfactor ) {
  var retVal = [];
  var faceExplodes = [];
  for( var i = 0; i < this.faceGroup.faces.length; i++ ) {
    faceExplodes = this.faceGroup.faces[i].getExplodedVertices( xfactor );
    for( var j = 0; j < faceExplodes.length; j++ ) {
      retVal.push( faceExplodes[j].elements[0], faceExplodes[j].elements[1], faceExplodes[j].elements[2] );
    }
  }

  return retVal;
}

Polyhedron.prototype.getFaceOrderedVertices = function() {
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

Polyhedron.prototype.getTriangleVertices = function() {
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

Face.prototype.getEdgeMidpointVertices = function() {
  var retVal = [];
  var tempMidpoint = null;
  for( var i = 0; i < this.vertices.length; i++ ) {
    if( i == (this.vertices.length - 1) ) {
      retVal.push( getMidpoint( this.vertices[i], this.vertices[0] ) );
    } else {
      retVal.push( getMidpoint( this.vertices[i], this.vertices[i+1] ) );
    }
  }
  return retVal;
}

Face.prototype.getEdgeTrisectionVertices = function() {
  var retVal = [];
  var tempMidpoint = null;
  for( var i = 0; i < this.vertices.length; i++ ) {
    if( i == (this.vertices.length - 1) ) {
      retVal.push( getOneThirdPoint( this.vertices[i], this.vertices[0] ) );
      retVal.push( getTwoThirdsPoint( this.vertices[i], this.vertices[0] ) );
    } else {
      retVal.push( getOneThirdPoint( this.vertices[i], this.vertices[i+1] ) );
      retVal.push( getTwoThirdsPoint( this.vertices[i], this.vertices[i+1] ) );
    }
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

var planeCoords = {
  xy : [ 0, 1 ],
  xz : [ 0, 2 ],
  yz : [ 1, 2 ],
}

Face.prototype.getCentroid = function() {
  var retVal = null;
  var area = this.getArea();
  var newX = 0.0, newY = 0.0, newZ = 0.0;
  var projectionPlanes = [planeCoords.xy,planeCoords.yz,planeCoords.xz];
  var averageCenter = this.getVertexAverage();
  var signArray = [];
  signArray.push( averageCenter.elements[0] < 0 ? -1 : 1 );
  signArray.push( averageCenter.elements[1] < 0 ? -1 : 1 );
  signArray.push( averageCenter.elements[2] < 0 ? -1 : 1 );

  if( precisionCompare( this.vertices[0].elements[0], this.vertices[1].elements[0] ) &&
      precisionCompare( this.vertices[0].elements[0], this.vertices[2].elements[0] ) ) {
    // same X, we are perpendicular to XY and XZ
    newX = this.vertices[0].elements[0];
    projectionPlanes = [ null, planeCoords.yz, planeCoords.yz ];
  } else if( precisionCompare( this.vertices[0].elements[1], this.vertices[1].elements[1] ) &&
             precisionCompare( this.vertices[0].elements[1], this.vertices[2].elements[1] ) ) {
    // same Y, we are perpendicular to XY and YZ
    newY = this.vertices[0].elements[1];
    projectionPlanes = [ planeCoords.xz, null, planeCoords.xz ];
  } else if( precisionCompare( this.vertices[0].elements[2], this.vertices[1].elements[2] ) &&
             precisionCompare( this.vertices[0].elements[2], this.vertices[2].elements[2] ) ) {
    // same Z, we are perpendicular to YZ and XZ
    newZ = this.vertices[0].elements[2];
    projectionPlanes = [ planeCoords.xy, planeCoords.xy, null ];
  } else if( this.plane.isPerpendicularTo(Plane.XY) ) {
    projectionPlanes = [planeCoords.xz,planeCoords.yz,planeCoords.xz];
  } else if( this.plane.isPerpendicularTo(Plane.YZ) ) {
    projectionPlanes = [planeCoords.xy,planeCoords.xy,planeCoords.xz];
  } else if( this.plane.isPerpendicularTo(Plane.XZ) ) {
    projectionPlanes = [planeCoords.xy,planeCoords.xy,planeCoords.yz];
  }
  
  if( projectionPlanes[0] != null ) newX = signArray[0] * Math.abs(this.getCentroidCoord( 0, area, projectionPlanes[0] ));
  if( projectionPlanes[1] != null ) newY = signArray[1] * Math.abs(this.getCentroidCoord( 1, area, projectionPlanes[1] ));
  if( projectionPlanes[2] != null ) newZ = signArray[2] * Math.abs(this.getCentroidCoord( 2, area, projectionPlanes[2] ));

  return $V([newX,newY,newZ]);
}

/**
 * Get one of the coordinates of the centroid
 * area: the area of the face
 * c: which coords of each vertex to use to calculate in order to avoid perpendicular coordinate planes.
 *
 *  TODO:  HAVE TO USE THE AREA OF THE *PROJECTION* OF THE FACE!!!! Dumbass.
 */
Face.prototype.getCentroidCoord = function( idx, area, c ) {
  var retVal = 0.0;
  var v1 = null, v2 = null;
  var projectionVertices = [];
  for( var i = 0; i < this.vertices.length; i++ ) {
    if( i == this.vertices.length - 1 ) {
      v1 = this.vertices[i];
      projectionVertices.push( $V([v1.elements[c[0]],v1.elements[c[1]]]) );
      v2 = this.vertices[0];
    } else {
      v1 = this.vertices[i];
      projectionVertices.push( $V([v1.elements[c[0]],v1.elements[c[1]]]) );
      v2 = this.vertices[i+1];
    }

    retVal += ((v1.elements[idx] + v2.elements[idx])*(v1.elements[c[0]]*v2.elements[c[1]] - v2.elements[c[0]]*v1.elements[c[1]]));
  }

  var projectionArea = getArea( projectionVertices );

  return (retVal/(6.0 * projectionArea));
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

/**
  Explode the face out by a factor of xfactor along its 
  normal vector.
 */
Face.prototype.getExplodedVertices = function( xfactor ) {
  var retVal = [];
  var unitNormal = this.normal.toUnitVector();
  for( var i = 0; i < this.vertices.length; i++ ) {
    if( unitNormal.dot( this.vertices[i] ) < 0 ) {
      retVal.push( this.vertices[i].subtract( unitNormal.x( xfactor ) ) );
    } else {
      retVal.push( this.vertices[i].add( unitNormal.x( xfactor ) ) );
    }
  }

  return retVal;
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
      //if( face.isOpposite( tempFace ) || face.isOrthogonal( tempFace ) || face.isParallel( tempFace) ) {
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


function VertexGroup ( coords ) {
  this.coords = coords;
  this.vertices = [];
  var xsum = 0.0, ysum = 0.0, zsum = 0.0;
  for( var i = 0; i < coords.length; i+=3 ) {
    xsum += coords[i];
    ysum += coords[i+1];
    zsum += coords[i+2];
    this.vertices.push( $V([coords[i], coords[i+1], coords[i+2]]) );
  }

  this.origin = $V([ xsum, ysum, zsum ]).x( 1.0 / (1.0*this.vertices.length) ).snapTo(0);
  //alert( "Origin: " + this.origin.inspect() );
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

/*
 * So this could DEFINITELY be optimized, but here's the brute force method:
 * 1. loop through every triple of vertices
 * 2. For each plane defined by each triple do a test:
 *   a. see if all other vertices are on the same "side" (or are on) the plane
 *   b. if the test fails (it has points on either side) move on.
 *   c. if the test succeeds, compare this plane to the previously found planes, if new, keep it.
 * 3. Once we have found all of the good planes, loop through all of the vertices again and see which 
 *    plane they fall into.
 * 4. Now we have our faces!  Returns an array of Face objects (Face[])
 */
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

