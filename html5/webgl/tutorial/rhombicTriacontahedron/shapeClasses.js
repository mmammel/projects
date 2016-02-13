function Vertex ( x, y, z, idx ) {
  this.x = x;
  this.y = y;
  this.z = z;
  this.idx = idx;
}

Vertex.prototype.dot = function( v ) {
  return this.x * v.x + this.y * v.y + this.z * v.z;
};

Vertex.prototype.mid = function( v ) {
  var retVal = new Vertex( (this.x + v.x) / 2.0, (this.y + v.y) / 2.0, (this.z + v.z) / 2.0 );
  return retVal;
};

Vertex.prototype.sum = function( v ) {
  var retVal = new Vertex( this.x + v.x, this.y + v.y, this.z + v.z );
  return retVal;
};

Vertex.prototype.magnitude = function() {
  return Math.sqrt( Math.pow( this.x, 2) + Math.pow( this.y, 2) + Math.pow( this.z, 2) );
}

/*
 * v0 and v2 are the "icosahedral" points.
 */
function RhombicFace ( v0, v1, v2, v3 ) {
  this.v0 = v0;
  this.v1 = v1;
  this.v2 = v2;
  this.v3 = v3;
  this.normal = v0.sum(v2);
}

RhombicFace.prototype.isOrthogonal = function( face ) {
  console.log( this.normal.dot( face.normal ) );
  return Math.abs(this.normal.dot( face.normal )) < 0.00001;
};

RhombicFace.prototype.isOpposite = function( face ) {
  console.log( this.normal.sum( face.normal ).magnitude() );
  return Math.abs(this.normal.sum( face.normal ).magnitude()) < 0.00001;
};

RhombicFace.prototype.getIndexArray = function() {
  return [ this.v0.idx, this.v1.idx, this.v2.idx, this.v3.idx ];
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
      new Vertex( vertices[3*faceMap[i]], vertices[3*faceMap[i]+1], vertices[3*faceMap[i]+2], faceMap[i] ),
      new Vertex( vertices[3*faceMap[i+1]], vertices[3*faceMap[i+1]+1], vertices[3*faceMap[i+1]+2], faceMap[i+1] ),
      new Vertex( vertices[3*faceMap[i+2]], vertices[3*faceMap[i+2]+1], vertices[3*faceMap[i+2]+2], faceMap[i+2] ),
      new Vertex( vertices[3*faceMap[i+3]], vertices[3*faceMap[i+3]+1], vertices[3*faceMap[i+3]+2], faceMap[i+3] )
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
      retVal = retVal.concat( sets[j][k].getIndexArray() );
    }
  }

  return retVal;
}

