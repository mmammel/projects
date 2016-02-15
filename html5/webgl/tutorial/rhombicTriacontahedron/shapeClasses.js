/*
 * v0 and v2 are the "icosahedral" points.
 * Uses the "Sylvester" vectors.
 */
function RhombicFace ( v0, v1, v2, v3, idxArray ) {
  this.v0 = v0;
  this.v1 = v1;
  this.v2 = v2;
  this.v3 = v3;
  this.normal = v0.add(v2);
  this.idxArray = idxArray;
}

RhombicFace.prototype.isOrthogonal = function( face ) {
  return this.normal.isPerpendicularTo( face.normal );
};

RhombicFace.prototype.isOpposite = function( face ) {
  return this.normal.isAntiparallelTo( face.normal );
};

function Face ( plane, vertices, idxArray ) {
  this.vertices = vertices;
  this.idxArray = idxArray;
  this.normal = plane.normal;
}

Face.prototype.isOrthogonal = function( face ) {
  return this.normal.isPerpendicularTo( face.normal );
};

Face.prototype.isOpposite = function( face ) {
  return this.normal.isAntiparallelTo( face.normal );
};

/*
 * vertices: one-dimensional array of floats defining all of the vertices.
 *           Starting at 0, 3 consecutive elements define a vertex.
 * faceMap: one-dimensional array of ints that are indices into the vertices array.
 *          Starting at 0, 4 consecutive elements point to 4 vertices that define a face.
 *
 *  So, 3*faceMap[n] points you to the x coordinate of some vertex that is in the n/4-th face.
 */
function FaceGroup ( vertices, faceMap ) {
  this.faces = [];
  var x = 0.0, y = 0.0, z = 0.0;
  var vIdx = 0;
  var tempFace = null;
  for( var i = 0; i < faceMap.length; i+=4 ) {
    tempFace = new RhombicFace(
      $V( [ vertices[3*faceMap[i]], vertices[3*faceMap[i]+1], vertices[3*faceMap[i]+2] ] ),
      $V( [ vertices[3*faceMap[i+1]], vertices[3*faceMap[i+1]+1], vertices[3*faceMap[i+1]+2] ] ),
      $V( [ vertices[3*faceMap[i+2]], vertices[3*faceMap[i+2]+1], vertices[3*faceMap[i+2]+2] ] ),
      $V( [ vertices[3*faceMap[i+3]], vertices[3*faceMap[i+3]+1], vertices[3*faceMap[i+3]+2] ] ),
      [ faceMap[i], faceMap[i+1], faceMap[i+2], faceMap[i+3] ]
    );

    this.faces.push(tempFace);
  }
}

FaceGroup.prototype.buildColorOrderedArray = function() {
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
      if( face.isOpposite( tempFace ) || face.isOrthogonal( tempFace ) ) {
        tempSet.push( tempFace );
        indexArray.splice( i, 1 ); // remove this index.
      }
    }

    if( tempSet.length > 0 ) {
      sets.push( tempSet );
    }
  }

  var retVal = [];
  for( var j = 0; j < sets.length; j++ ) {
    for( var k = 0; k < sets[j].length; k++ ) {
      retVal = retVal.concat( sets[j][k].idxArray );
    }
  }

  return retVal;
}

function VertexGroup ( coords ) {
  this.coords = coords;
  this.vertices = [];
  for( var i = 0; i < coords.length; i+=3 ) {
    this.vertices.push( $V([coords[i], coords[i+1], coords[i+2]]) );
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
  }

  return result;
}

