var tableWidth = 500;
var tableHeight = 500;
var controlPressed = false;
var shiftPressed = false;

// FIGURE OUT THE FIREFOX BLUE BOX STYLING

$(function() {
  $(document).bind('keydown',function(e){
          if( e.keyCode == 17 ) controlPressed = true;
             });
   $(document).bind('keyup',function(e){
          if( e.keyCode == 17) controlPressed = false;
   });
     $(document).bind('keydown',function(e){
             if( e.keyCode == 16 ) shiftPressed = true;
                });
      $(document).bind('keyup',function(e){
             if( e.keyCode == 16) shiftPressed = false;
   });
});


var PageTable = function( id, descriptor ) {

  this.id = id;
  this.rowcount = 0;
  this.colcount = 0;
  this.colsizes = [];


  this.mergedCells = {};
  this.nonCells = {};
  this.itemMap = {};

  var tableEle = $("#"+id);

  //if( tableEle.prop("localName") != "table" )
  //{
  //  alert( "Error: id: " + id + " is not a table element");
  //  return;
  //}

  if( descriptor != null )
  {
    var darray = descriptor.split(";");
    for( var i = 0; i < darray.length; i++ )
    {
      if( darray[i].length == 0 ) continue;

      var tempArray = darray[i].split( ":" );

      switch(i) {
        case 0:
         //rows:cols
         this.rowcount = parseInt(tempArray[0]);
         this.colcount = parseInt(tempArray[1]);
         break;
        case 1:
          //colsizes
          var sizes = [];

          for( var j = 0; j < tempArray.length; j++ )
          {
            if( tempArray[j].length == 0 ) continue;
            tempSize = parseFloat( tempArray[j] );
            sizes.push( tempSize );
          }

          this.normalizeColSizes( sizes );

          break;
        default:
          // merged cells
          var tempCol = parseInt(tempArray[0]);
          var tempRow = parseInt(tempArray[1]);
          var tempRowspan = parseInt(tempArray[2]);
          var tempColspan = parseInt(tempArray[3]);
          var key = tempCol+"_"+tempRow;
          this.mergedCells[ key ] = { id:key, row:tempRow, col:tempCol, rowspan: tempRowspan, colspan: tempColspan };
          var noncellsForKey = this.getNonCellsForCell( key );

          for( var k = 0; k < noncellsForKey.length; k++ )
          {
            this.nonCells[noncellsForKey[k]] = key;
          }

          break;
      }
    }
  }
  else
  {
    this.rowcount = 3;
    this.colcount = 1;
    this.colsizes.push( 100 );
  }
}

PageTable.prototype.normalizeColSizes = function( sizes ) {
  // takes an array of floats, and normalizes them to percentages.
  var total = 0.0;
  var tempSize = 0.0;
  for( var i = 0; i < sizes.length; i++ )
  {
    total+=sizes[i];
  }

  for( var y = 0; y < sizes.length; y++ )
  {
    tempSize = sizes[y];
    this.colsizes[y] = parseFloat(((tempSize/total)*100).toFixed());
  }
}

PageTable.prototype.render = function()
{
  var tableEle = $("#"+this.id);
  tableEle.empty();
  tableEle.attr("width",tableWidth);
  tableEle.attr("height",tableHeight);
  var tHead = $('<thead></thead>');
  tableEle.append( tHead );
  var tBody = $('<tbody></tbody>');
  tableEle.append( tBody );

  var rowHeight = parseFloat((100.0/this.rowcount).toFixed());

  for( var r = 0; r < this.rowcount; r++ )
  {

    var tempRow = $('<tr height="' + rowHeight + '%"></tr>');
    tempRow.attr("id","row"+r );
    for( var c = 0; c < this.colcount; c++ )
    {
      var tempMerge = "";
      var key = c+"_"+r;
      if( this.nonCells[key] == null ) {

        var tempCol = $('<td id="' + key + '" width="' + this.colsizes[c] + '%"></td>' );
        if( (tempMerge = this.mergedCells[key]) != null )
        {
          tempCol.attr("rowspan",tempMerge.rowspan);
          tempCol.attr("colspan",tempMerge.colspan);
          tempCol.attr("width",this.getWidthOfMergedCell(tempMerge));
        }
        else
        {
          tempCol.attr("width",this.colsizes[c]+"%");
        }

        tempRow.append( tempCol );
      }
    }

    tBody.append( tempRow );
  }

  this.addHandlers();
  var descriptorHolder = $("#"+this.id+"_descriptor");
  if( descriptorHolder != null )
  {
    descriptorHolder.html(this.generateDescriptor());
  }

  var sizeTable = $("#"+this.id+"_colsizes");
  sizeTable.empty();
  tempRow = $('<tr></tr>');
  var tempSizeTd = null;
  for( var i = 0; i < this.colsizes.length; i++ )
  {
    tempSizeTd = $('<td style="text-align:center" width="' + this.colsizes[i] + '%"><input size="1" id="colsize'+i+'" value="'+this.colsizes[i]+'"></input></td>');
    tempRow.append(tempSizeTd);
  }

  sizeTable.append( tempRow );
  sizeTable.attr("width",tableWidth+"px");
}

PageTable.prototype.updateSizes = function() {
  var sizes = [];
  for( var i = 0; i < this.colsizes.length; i++ )
  {
    sizes.push(parseFloat($("#colsize"+i).attr("value")));
  }

  this.normalizeColSizes( sizes );

  this.render();
}

PageTable.prototype.getWidthOfMergedCell = function(merged) {
  var col = merged.col;
  var colspan = merged.colspan;
  var retVal = 0.0;

  for( var i = col; i < (col+colspan); i++ )
  {
    retVal += this.colsizes[i];
  }

  return retVal+"%";
}

PageTable.prototype.addHandlers = function() {
    $("#"+this.id+" td").click(function() {
      var myId = $(this).attr("id");

      if( !controlPressed && !shiftPressed ) $(this).parent().parent().find("td").each( function() { if( $(this).attr("id") != myId) $(this).removeClass( "slctd" ); } );


      if( shiftPressed )
      {
        var firstId = $(this).parent().parent().find("td.slctd:first").attr("id");
        if( firstId != null )
        {

          var firstCoords = firstId.split("_");
          var myCoords = myId.split("_");
          var firstC = parseInt( firstCoords[0] );
          var firstR = parseInt( firstCoords[1] );
          var myC = parseInt( myCoords[0] );
          var myR = parseInt( myCoords[1] );

          if( firstC <= myC && firstR <= myR )
          {
            for( var c = firstC; c <= myC; c++ )
            {
              for( var r = firstR; r <= myR; r++ )
              {
                $("#"+c+"_"+r).addClass( "slctd" );
              }
            }
          }
        }
      }
      else
      if( $(this).hasClass( "slctd" ) )
      {
        $(this).removeClass("slctd");
       }
       else
       {
         $(this).addClass("slctd");
        }
    });
}

//TODO: Make it handle multidimensional merges
PageTable.prototype.getNonCellsForCell = function( cellid ) {
  // cell id is c_r
  var tempMerge = null;
  var retVal = [];

  if( (tempMerge = this.mergedCells[cellid]) != null ) {

    for( var r = 0; r < tempMerge.rowspan; r++ )
    {
      for( var c = 0; c < tempMerge.colspan; c++ )
      {
        if( r != 0 || c != 0 )
        {
          retVal.push( (tempMerge.col+c)+"_"+(tempMerge.row + r) );
        }
      }
    }
  }

  return retVal;
}

PageTable.prototype.getCellData = function( cellid ) {
  var splits = cellid.split("_");
  var retVal = { id:cellid, col:parseInt(splits[0]), row:parseInt(splits[1]) };
  if( this.nonCells[cellid] != null  ) {
    retVal.noncell = true;
    retVal.rowspan = 0;
    retVal.colspan = 0;
  }
  else
  {
    retVal.noncell = false;
    var tempMerge = null;
    if( (tempMerge = this.mergedCells[cellid]) != null )
    {
      retVal.rowspan = tempMerge.rowspan;
      retVal.colspan = tempMerge.colspan;
    }
    else
    {
      retVal.rowspan = 1;
      retVal.colspan = 1;
    }
  }

  return retVal;
}

PageTable.prototype.mergeCells = function( cellids ) {
  if( this.mergeMultiInner( cellids ) )
  {
    this.render();
  }
  else
  {
    alert( "Not mergeable");
  }
}

PageTable.prototype.mergeMultiInner = function( cellids ) {
  var retVal = false;
  var cellData = [];
  var allIds = [];
  var currWidth = 0;
  var currHeight = 0;
  if( cellids.length > 1 )
  {
    for( var i = 0; i < cellids.length; i++ )
    {
      allIds.push( cellids[i] );
      allIds = allIds.concat( this.getNonCellsForCell( cellids[i] ) );
    }

    allIds.sort( function(a,b){
      var asplit = a.split("_");
      var bsplit = b.split("_");
      var ac = parseInt(asplit[0]);
      var ar = parseInt(asplit[1]);
      var bc = parseInt(bsplit[0]);
      var br = parseInt(bsplit[1]);

      return ( ((100 * ar) + ac) - ((100 * br) + bc) );
    });

    // allIds is now sorted, upper-left most cell to lower-right most;  Make sure it makes a box.
    var first = allIds[0].split("_");
    var last = allIds[ allIds.length - 1 ].split("_");

    var fc = parseInt(first[0]);
    var fr = parseInt(first[1]);
    var lc = parseInt(last[0]);
    var lr = parseInt(last[1]);

    var count = 0;
    for( var r = fr; r <= lr; r++ )
    {
      for( var c = fc; c <= lc; c++ )
      {
        if( ""+c+"_"+r != allIds[count++] )
        {
          return retVal; // alternative is ugly flagging.
        }
      }
    }

    // If we made it here we're all set to merge.
    this.mergedCells[ allIds[0] ] = {id:allIds[0], row:fr, col:fc, colspan:(lc - fc + 1), rowspan:(lr - fr + 1)};
    for( var k = 1; k < allIds.length; k++ )
    {
      this.mergedCells[ allIds[k] ] = null;
      this.nonCells[ allIds[k] ] = fc+"_"+fr;
    }

    retVal = true;
  }

  return retVal;
}

PageTable.prototype.deleteColumn = function( cellid ) {
  //delete the column that this cell is in.
  if( this.colcount == 1 )
  {
    alert( "Must have at least one column!" );
    return;
  }

  var lastMaster = null;
  var tempMerge = null;
  var currId = null;
  var tempNonCells = null;
  var tempCellData = null;

  var cellInfo = this.getCellData( cellid );

  for( var i = 0; i < this.rowcount; i++ )
  {
    currId = cellInfo.col + "_" + i;

    if( this.nonCells[ currId ] != null )
    {
      // it's a noncell, adjust the master only if we already haven't
      if( lastMaster != this.nonCells[ currId ] )
      {
        lastMaster = this.nonCells[ currId ];
        if( (tempMerge = this.mergedCells[ lastMaster ]) != null ) // maybe we already deleted the master.
        {
          // adjust the master.
          tempMerge.colspan = tempMerge.colspan - 1;
          this.mergedCells[ lastMaster ] = tempMerge;
        }
      }
    }
    else
    {
      // it's a real cell, see if its merged, handle accordingly.
      if( (tempMerge = this.mergedCells[ currId ]) != null )
      {
        // It's merged, create a new merged cell one cell to the right *if* it is a slave cell.
        // in any case, delete the merged cell.
        var newMaster = (tempMerge.col+1)+"_"+tempMerge.row;
        if( this.nonCells[ newMaster ] == currId )
        {
          //this is the new merged Cell, set it.
          this.mergedCells[ newMaster ] = {id:newMaster, row:tempMerge.row, col:(tempMerge.col+1), rowspan:tempMerge.rowspan, colspan:(tempMerge.colspan - 1)};
        }

        this.mergedCells[ currId ] = null;
      }
      // else do nothing.
    }
  }

  // Now adjust the merged cells to the right of the column.
  for( var c = cellInfo.col+1; c < this.colcount; c++ )
  {
    for( var r = 0; r < this.rowcount; r++ )
    {
      var tempId = c+"_"+r;
      var newMergeId = (c-1)+"_"+r;
      if( (tempMerge = this.mergedCells[tempId]) != null )
      {
        // now update the merged cell info
        tempMerge.col = tempMerge.col - 1;
        this.mergedCells[ newMergeId ] = tempMerge;
        this.mergedCells[ tempId ] = null;
      }
    }
  }

  this.recalculateNonCells();

  // OK...now reduce the colcount and adjust the sizes of the columns.
  var removedSize = this.colsizes[ cellInfo.col ];
  var dist = parseFloat( (removedSize / (this.colsizes.length - 1)).toFixed() );
  var newSizes = [];
  var tempSize = 0.0;
  var total = 0.0;
  for( var k = 0; k < this.colsizes.length; k++ )
  {
    if( k != cellInfo.col )
    {
      tempSize = this.colsizes[k] + dist;
      total += tempSize;
      newSizes.push( tempSize );
    }
  }

  newSizes[ newSizes.length - 1 ] += (100.0 - total );
  this.colsizes = newSizes;
  this.colcount--;

  // cross fingers, and redraw :)
  this.render();
}

PageTable.prototype.deleteRow = function( cellid ) {
  //delete the row that this cell is in.
  if( this.rowcount == 1 )
  {
    alert( "Must have at least one row!" );
    return;
  }

  var lastMaster = null;
  var tempMerge = null;
  var currId = null;
  var tempNonCells = null;
  var tempCellData = null;

  var cellInfo = this.getCellData( cellid );

  for( var i = 0; i < this.colcount; i++ )
  {
    currId = i + "_" + cellInfo.row;

    if( this.nonCells[ currId ] != null )
    {
      // it's a noncell, adjust the master only if we already haven't
      if( lastMaster != this.nonCells[ currId ] )
      {
        lastMaster = this.nonCells[ currId ];
        if( (tempMerge = this.mergedCells[ lastMaster ]) != null ) // maybe we already deleted the master.
        {
          // adjust the master.
          tempMerge.rowspan = tempMerge.rowspan - 1;
          this.mergedCells[ lastMaster ] = tempMerge;
        }
      }
    }
    else
    {
      // it's a real cell, see if its merged, handle accordingly.
      if( (tempMerge = this.mergedCells[ currId ]) != null )
      {
        // It's merged, create a new merged cell one cell down *if* it is a slave cell.
        // in any case, delete the merged cell.
        var newMaster = tempMerge.col+"_"+(tempMerge.row+1);
        if( this.nonCells[ newMaster ] == currId )
        {
          //this is the new merged Cell, set it.
          this.mergedCells[ newMaster ] = {id:newMaster, row:(tempMerge.row+1), col:tempMerge.col, rowspan:(tempMerge.rowspan-1), colspan:tempMerge.colspan};
        }

        this.mergedCells[ currId ] = null;
      }
      // else do nothing.
    }
  }

  // Now adjust the merged cells below the row.
  for( var c = 0; c < this.colcount; c++ )
  {
    for( var r = cellInfo.row+1; r < this.rowcount; r++ )
    {
      var tempId = c+"_"+r;
      var newMergeId = c+"_"+(r-1);
      if( (tempMerge = this.mergedCells[tempId]) != null )
      {
        // now update the merged cell info
        tempMerge.row = tempMerge.row - 1;
        this.mergedCells[ newMergeId ] = tempMerge;
        this.mergedCells[ tempId ] = null;
      }
    }
  }

  this.recalculateNonCells();

  this.rowcount--;

  // cross fingers, and redraw :)
  this.render();
}

PageTable.prototype.recalculateNonCells = function() {

  var tempNonCells = null;
  // clear out the nonCells.
  this.nonCells = [];

  // and recalculate.
  for( var c = 0; c < this.colcount; c++ )
  {
    for( var r = 0; r < this.rowcount; r++ )
    {
      var tempId = c+"_"+r;
      // now add the new noncells.
      tempNonCells = this.getNonCellsForCell( tempId );
      for( var j = 0; j < tempNonCells.length; j++ )
      {
        this.nonCells[tempNonCells[j]] = tempId;
      }
    }
  }
}

PageTable.prototype.splitCells = function( cellids ) {
  var tempMerge = null;

  for( var idx = 0; idx < cellids.length; idx++ )
  {
    cellid = cellids[idx];

    if( (tempMerge = this.mergedCells[ cellid ]) != null )
    {
      var noncells = this.getNonCellsForCell( cellid );
      for( var i = 0; i < noncells.length; i++ )
      {
        this.nonCells[ noncells[i] ] = null;
      }

      this.mergedCells[cellid] = null;
    }
    else
    {
      // add a new column
      this.addColumn( cellid );
    }
  }

  this.render();
}

PageTable.prototype.getSelectedCells = function() {
  var selectedCells = new Array();
  var idx = 0;
  $("#"+this.id + " .slctd").each(function() {
    selectedCells.push(this.id);
  });

  return selectedCells;
}

PageTable.prototype.addRow = function() {
  this.rowcount++;
  this.render();
}


PageTable.prototype.addColumn = function(cellid) {

  // If no cell ID is provided just add the column on the right side.
  if( cellid == null ) {
    this.colcount++;
    //re-adjust the column sizes, scale down the existing by a factor of 1/n, where n is the new column size, then give the rest to the new column.
    var multiplier = (this.colcount - 1)/this.colcount;
    var subTotal = 0;
    for( var i = 0; i < this.colcount - 1; i++ )
    {
      this.colsizes[i] = parseFloat((this.colsizes[i] * multiplier).toFixed());
      subTotal += this.colsizes[i];
    }

    this.colsizes[this.colcount - 1] = 100.0 - subTotal;
  }
  else
  {
    // otherwise, this is a new column in the middle of the table.  deal with it :)
    var cellData = this.getCellData( cellid );
    var tempId = "";
    var tempParent = "";
    var tempMerge = null;
    var lastMaster = null;
    for( var i = 0; i < this.rowcount; i++ )
    {
      // Walk down all of the cells in the selected cell's column.
      tempId = cellData.col+"_"+i;
      if( tempId == cellData.id )
      {
        // it's the cell we're splitting - we assume that it is not merged, otherwise it would have
        // simply been split from its previous merge.  aka don't do anything.
      }
      else if( (tempMerge = this.mergedCells[tempId]) != null )
      {
        // the cell is already merged, update it's dimensions.
        tempMerge.colspan++;
        lastMaster = tempMerge.id;
      }
      else if((tempParent = this.nonCells[tempId]) != null )
      {
        // it's a non-cell, update its parent.
        if( lastMaster != tempParent )
        {
          lastMaster = tempParent;
          tempMerge = this.mergedCells[ lastMaster ];
          // adjust the master.
          tempMerge.colspan = tempMerge.colspan + 1;
          this.mergedCells[ lastMaster ] = tempMerge;
        }
      }
      else
      {
        // it's a normal cell, but now we have to make it span the new column.
        this.mergedCells[tempId] = {id:tempId, row:i,col:cellData.col,rowspan:1,colspan:2};
      }
    }

    // Now adjust the merged cells to the right of the column.
    for( var c = this.colcount - 1; c > cellData.col; c-- )
    {
      for( var r = 0; r < this.rowcount; r++ )
      {
        var tempId = c+"_"+r;
        var newMergeId = (c+1)+"_"+r;
        if( (tempMerge = this.mergedCells[tempId]) != null )
        {
          // now update the merged cell info
          tempMerge.col = tempMerge.col + 1;
          this.mergedCells[ newMergeId ] = tempMerge;
          this.mergedCells[ tempId ] = null;
        }
      }
    }

    // and recalculate the non-cells.
    this.colcount++;
    this.recalculateNonCells();
    var newSizes = [];
    var halving = 0.0;
    for( var j = 0; j < this.colsizes.length; j++ )
    {
      if( j == cellData.col )
      {
        halving = parseFloat((this.colsizes[j] / 2).toFixed());
        newSizes.push( halving );
        newSizes.push( halving - (halving%2));
      }
      else
      {
        newSizes.push( this.colsizes[j] );
      }
    }

    this.colsizes = newSizes;
  }

  this.render();
}

PageTable.prototype.generateDescriptor = function() {
  var descriptor = this.rowcount+":"+this.colcount+";";
  for( var i = 0; i < this.colsizes.length; i++ )
  {
    descriptor += (this.colsizes[i] + ":");
  }
  descriptor += ";";

  var tempMerge = null;
  var tempId = "";

  for( var r = 0; r < this.rowcount; r++ ) {
    for( var c = 0; c < this.colcount; c++ ) {
      tempId = c+"_"+r;
      if( (tempMerge = this.mergedCells[ tempId ]) != null )
      {
        descriptor += tempMerge.col + ":" + tempMerge.row + ":" + tempMerge.rowspan + ":" + tempMerge.colspan + ";";
      }
    }
  }

  return descriptor;

}
