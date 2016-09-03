// Globals
var testTableTemplate = null;
var sectionTableTemplate = null;
var sectionItemTemplate = null;

$.testModel = {
  name: "My First Test",
  id: "ENFIRST",
  sections: [
    {
      name: "Section 0",
      id: "section00",
      items: [
        { name: "Intro 0", id: "item00" },
        { name: "Item 1", id: "item01" },
        { name: "Item 2", id: "item02" },
        { name: "Item 3", id: "item03" },
        { name: "Item 4", id: "item04" },
        { name: "Item 5", id: "item05" },
        { name: "Item 10", id: "item0100" },
        { name: "Item 20", id: "item0200" },
        { name: "Item 30", id: "item0300" },
        { name: "Item 40", id: "item0400" },
        { name: "Item 50", id: "item05000" },
        { name: "Item 100", id: "item010" },
        { name: "Item 200", id: "item020" },
        { name: "Item 300", id: "item030" },
        { name: "Item 400", id: "item040" },
        { name: "Item 500", id: "item050" },
        { name: "Item 600", id: "item06" }
      ]
    },
    {
      name: "Section 1",
      id: "section0",
      items: [
        { name: "Intro 1", id: "item0" },
        { name: "Item 1", id: "item1" },
        { name: "Item 2", id: "item2" },
        { name: "Item 3", id: "item3" },
        { name: "Item 4", id: "item4" },
        { name: "Item 5", id: "item5" },
        { name: "Item 6", id: "item6" }
      ]
    },
    {
      name: "Section 2",
      id: "section1",
      items: [
        { name: "Intro 2", id: "item7" },
        { name: "Item 1", id: "item8" },
        { name: "Item 2", id: "item9" }
      ]
    }
  ]
};

function normalizeString( str ) {
  var result = str.toLowerCase();
  result = result.replace(/- ,'"/,'');
  return result;
}

function loadTemplates() {
  var source = $('#sectionTableTemplate').html();
  sectionTableTemplate = Handlebars.compile( source );
  source = $('#sectionItemTemplate').html();
  sectionItemTemplate = Handlebars.compile( source );
  source = $('#testTableTemplate').html();
  testTableTemplate = Handlebars.compile( source );
}

function createSectionPopup() {
  $( "#addSectionPopup" ).dialog({
    autoOpen: false,
    height: 300,
    width: 500,
    modal: true,
    buttons: {
      "Add Section": function() {
        var name = $("input[type='text']", this).val();
        var pos = $("input[type='radio']", this).val();
        alert("Add section: Name: " + name + ", Position: " + pos);
      },

      Cancel: function() {
       $(this).dialog( "close" );
      }
    }
  });
}

function loadTest( test ) {
  var content = testTableTemplate({
     name: test.name, 
     id: test.id,
     testBody: loadSections( test.sections )
  }); 
  
  $('#testContainer').empty();
  $('#testContainer').append( $( content ) );

  $( ".sectionList" ).sortable({
    tolerance: "pointer",
    axis: "y",
    scroll: true,
    scrollSensitivity: 100
  });

  $( ".sectionItemList" ).sortable({ 
    tolerance: "pointer",
    axis: "y",
    connectWith: ".sectionItemList",
    scroll: true,
    scrollSensitivity: 100
  });

}

function loadSections( sectionList ) {
  var content = "";
  for( var i = 0; i < sectionList.length; i++ ) {
    content += makeSection( sectionList[i] );
  }

  return content;
}

function makeSection( section ) {
  return sectionTableTemplate({
    name: section.name,
    id: section.id,
    sectionBody: makeSectionItems( section.items )
  });
}

function makeSectionItems( itemList ) {
  // Right now just an array of objects with a single "name" property.
  var allItemsHtml = "";
  for( var i = 0; i < itemList.length; i++ ) {
    allItemsHtml += sectionItemTemplate( itemList[i] );
  }

  return allItemsHtml;
}

