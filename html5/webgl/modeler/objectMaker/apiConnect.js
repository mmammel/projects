/**
 * All the calls that deal with talking to the API
 */

function ajaxCall( callDetails ) { 
  $.ajax({
    url: '/api/shape/index.php',
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
  $('#shapeSelect').append( $('<option value="-1" selected>Loading...</option>') );
  ajaxCall({
    type: "GET",
    successHandler: function( result ) {
      $.shapeData = result.results;
      $('#shapeSelect').empty();
      $('#shapeSelect').append( $('<option value="-1" selected>Select One</option>') );
      for( var i = 0; i < $.shapeData.length; i++ ) {
        $('#shapeSelect').append( $('<option value="'+$.shapeData[i].id+'">'+$.shapeData[i].name+'</option>') );
      }
    }
  });
}

function loadShape() {
  var id = $('#shapeSelect').val();
  for( var i = 0; i < $.shapeData.length; i++ ) {
    if( id === ""+$.shapeData[i].id ) {
      $('#shapeId').val( id );
      $('#shapeName').val( $.shapeData[i].name );
      $('#shapeVerts').val( $.shapeData[i].vertices );
      $('#shapeDesc').val( $.shapeData[i].description );
      $('#shapeSave').prop("disabled",false);
      loadFaces();
    }
  }
}

function saveShape() {
  var shape = {};
  var id = $('#shapeId').val();
  if( id != null && id.length > 0 ) {
    shape.id = parseInt( id );
  }
  shape.name = $('#shapeName').val();
  shape.vertices = $('#shapeVerts').val();
  shape.description = $('#shapeDesc').val();

  if( shape.name == null || shape.name.trim().length == 0 ) {
    alert("You must supply a name!");
    return;
  } else if( shape.vertices == null || shape.vertices.trim().length == 0 ) {
    alert("You must supply vertices!  Write javascript that assigns an array of floats to a variable called \"vertices\"." );
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
  $('#shapeId').val("");
  $('#shapeSelect').val("-1");
  $('#shapeSave').prop("disabled",true);
}

function updateSaveButton() {
  var name = $('#shapeName').val();
  var vertices = $('#shapeVerts').val(); 
  if( name != null && name.trim().length > 0 &&
      vertices != null && vertices.trim().length > 0 ) {
    $('#shapeSave').prop('disabled',false);
  } else {
    $('#shapeSave').prop('disabled',true);
  }
}

