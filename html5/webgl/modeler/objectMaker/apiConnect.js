/**
 * All the calls that deal with talking to the API
 */

function ajaxCall( callDetails ) { 
  $.ajax({
    url: '/api/shape',
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

function getShapes() {
  $('#shapeSelect').empty();
  $('#shapeSelect').append( $('<option value="" selected>Loading...</option>') );
  ajaxCall({
    type: "GET",
    successHandler: function( result ) {
      $.shapeData = result.results;
      $('#shapeSelect').empty();
      $('#shapeSelect').append( $('<option value="" selected>Select One</option>') );
      for( var i = 0; i < $.shapeData.length; i++ ) {
        $('#shapeSelect').append( $('<option value="'+$.shapeData[i].name+'">'+$.shapeData[i].name+'</option>') );
      }
    }
  });
}

function loadShape() {
  var name = $('#shapeSelect').val();
  for( var i = 0; i < $.shapeData.length; i++ ) {
    if( name === ""+$.shapeData[i].name ) {
      $('#shapeName').val( $.shapeData[i].name );
      $('#shapeVerts').val( $.shapeData[i].faces );
      $('#shapeDesc').val( $.shapeData[i].description );
      $('#shapeSave').prop("disabled",false);
      loadFaces();
    }
  }
}

function saveShape() {
  var shape = {};
  const now = new Date();
  shape.name = $('#shapeName').val();
  shape.faces = $('#shapeVerts').val();
  shape.description = $('#shapeDesc').val();
  shape.created_date = now.toISOString();

  if( shape.name == null || shape.name.trim().length == 0 ) {
    alert("You must supply a name!");
    return;
  } else if( shape.faces == null || shape.faces.trim().length == 0 ) {
    alert("You must supply face definitions!  Write javascript that assigns an array of face objects to a variable called \"faces\"." );
    return;
  }

  if( shape.description == null || shape.description.trim().length == 0 ) {
    shape.description = "N/A";
  }

  ajaxCall({
    type: "POST",
    data: shape,
    successHandler: function(result) {
      getShapes();
    }
  });
}

function clearForm() {
  $('#shapeName').val("");
  $('#shapeDesc').val("");
  $('#shapeVerts').val("");
  $('#shapeSelect').val("-1");
  $('#shapeSave').prop("disabled",true);
}

function updateSaveButton() {
  var name = $('#shapeName').val();
  var faces = $('#shapeVerts').val(); 
  if( name != null && name.trim().length > 0 &&
      faces != null && faces.trim().length > 0 ) {
    $('#shapeSave').prop('disabled',false);
  } else {
    $('#shapeSave').prop('disabled',true);
  }
}

