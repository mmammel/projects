
function Polyhedron ( coords ) {
  this.vertexGroup = new VertexGroup( coords );
  this.faceGroup = new FaceGroup( this.vertexGroup.findFaces() );
  this.triangleVertices = this.getTriangleVertices();
}

Polyhedron.prototype.getVertexIndexArray = function() {
  var retVal = [];
  for( var i = 0; i < this.faceGroup.faces.length; i++ ) {
    retVal = retVal.concat( this.faceGroup.faces[i].idxArray );
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

/*
 * faces: an array of generic faces, might have any number of sides.
 */
function FaceGroup ( faces ) {
  this.faces = faces;
  this.coloringSets = this.sortForColoring();
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
      if( face.isOpposite( tempFace ) || face.isOrthogonal( tempFace ) || face.isParallel( tempFace) ) {
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
  for( var i = 0; i < coords.length; i+=3 ) {
    this.vertices.push( $V([coords[i], coords[i+1], coords[i+2]]) );
  }
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
  var face = false;
  for( var i = 0; i < this.vertices.length - 2; i++ ) {
    for( var j = i+1; j < this.vertices.length - 1; j++ ) {
      for( var k = j+1; k < this.vertices.length; k++ ) {
        //tempPlane = $P( this.vertices[i].elements, this.vertices[j].subtract(this.vertices[i]), this.vertices[k].subtract(this.vertices[i]) );
        tempPlane = $P( this.vertices[i], this.vertices[j], this.vertices[k] );
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

        if( face ) {
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
  var tempFace = null;
  for( var f = 0; f < goodPlanes.length; f++ ) {
    tempFace = new Face( goodPlanes[f], [], [] );
    result.push( tempFace );
    for( var v = 0; v < this.vertices.length; v++ ) {
      if( goodPlanes[f].contains( this.vertices[v] ) ) {
        tempFace.idxArray.push( v );
        tempFace.vertices.push( this.vertices[v] );
      } 
    }

    tempFace.sortVertices();
  }

  return result;
}

