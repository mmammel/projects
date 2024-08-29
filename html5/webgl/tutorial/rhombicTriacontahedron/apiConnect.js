/**
 * All the calls that deal with talking to the API
 */

function ajaxCall( callDetails ) { 
  $.ajax({
    url: '/api/polyhedron',
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

function getPolyhedra() {
  $('#polySelect').empty();
  $('#polySelect').append( $('<option value="" selected>Loading...</option>') );
  ajaxCall({
    type: "GET",
    successHandler: function( result ) {
      $.polyData = result.results;
      $('#polySelect').empty();
      $('#polySelect').append( $('<option value="" selected>Select One</option>') );
      for( var i = 0; i < $.polyData.length; i++ ) {
        $('#polySelect').append( $('<option value="'+$.polyData[i].name+'">'+$.polyData[i].name+'</option>') );
      }
    }
  });
}

function loadPolyhedron() {
  var name = $('#polySelect').val();
  for( var i = 0; i < $.polyData.length; i++ ) {
    if( name === ""+$.polyData[i].name ) {
      $('#polyName').val( $.polyData[i].name );
      $('#polyVerts').val( $.polyData[i].vertices );
      $('#polyDesc').val( $.polyData[i].description );
      $('#polySave').prop("disabled",false);
      loadVertices();
    }
  }
}

function savePolyhedron() {
  const now = new Date();
  var poly = {};
  poly.name = $('#polyName').val();
  poly.vertices = $('#polyVerts').val();
  poly.description = $('#polyDesc').val();
  poly.created_date = now.toISOString();

  if( poly.name == null || poly.name.trim().length == 0 ) {
    alert("You must supply a name!");
    return;
  } else if( poly.vertices == null || poly.vertices.trim().length == 0 ) {
    alert("You must supply vertices!  Write javascript that assigns an array of floats to a variable called \"vertices\"." );
    return;
  }

  if( poly.description == null || poly.description.trim().length == 0 ) {
    poly.description = "N/A";
  }

  ajaxCall({
    type: "POST",
    data: poly,
    successHandler: function(result) {
      getPolyhedra();
    }
  });
}

function clearForm() {
  $('#polyName').val("");
  $('#polyDesc').val("");
  $('#polyVerts').val("");
  $('#polyId').val("");
  $('#polySelect').val("-1");
  $('#polySave').prop("disabled",true);
}

function updateSaveButton() {
  var name = $('#polyName').val();
  var vertices = $('#polyVerts').val(); 
  if( name != null && name.trim().length > 0 &&
      vertices != null && vertices.trim().length > 0 ) {
    $('#polySave').prop('disabled',false);
  } else {
    $('#polySave').prop('disabled',true);
  }
}

