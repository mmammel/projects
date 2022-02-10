/**
 * All the calls that deal with talking to the API
 */

function ajaxCall( callDetails ) {
  $.ajax({
    url: '/api/yinyang/index.php',
    type: callDetails.type,
    contentType: callDetails.contentType ? callDetails.contentType : "application/json",
    dataType: callDetails.dataType ? callDetails.dataType : "json",
    data: callDetails.data ? JSON.stringify( callDetails.data ) : "",
    success: function( result ) {
      callDetails.successHandler( result );
    },
    error: function( result ) {
      alert("Got an error response: " + result.responseText );
    }
  });
}

function getPuzzles(callback) {
  ajaxCall({
    type: "GET",
    successHandler: function( result ) {
      if( result.results && result.results.length > 0 ) {
        callback(result.results);
      }
    }
  });
}

function saveOrUpdate(puzzleData, callback) {
  ajaxCall({
    type: "POST",
    data: puzzleData,
    successHandler: function( result ) {
      callback(result);
    }
  });
}
