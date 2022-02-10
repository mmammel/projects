function BitMask( size ) {
  this.mask = [];
  var arrayLen = Math.ceil( size / 32 );
  for( var i = 0; i < arrayLen; i++ ) {
    this.mask.push(0);
  }
}

BitMask.prototype.setBit = function( idx ) {
  var arrayOffset = Math.floor(idx / 32);
  var bitOffset = idx % 32;
  this.mask[ arrayOffset ] = this.mask[ arrayOffset ] | (1 << bitOffset);
};

BitMask.prototype.isSet = function( idx ) {
  var arrayOffset = Math.floor(idx / 32);
  var bitOffset = idx % 32;
  return (this.mask[ arrayOffset ] & (1 << bitOffset)) > 0;
};

BitMask.prototype.toString = function() {
  var retVal = "";
  var temp = "";
  for( var i = 0; i < this.mask.length; i++ ) {
    temp = "";
    for( var j = 0; j < 32; j++ ) {
      temp += ((1 << j) & this.mask[i]) >= 1 ? "1" : "0";
    }
    retVal += temp;
  }

  return retVal;
};
