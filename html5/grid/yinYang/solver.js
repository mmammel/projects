class Row {
  constructor(id, c) {
    this.id = id;
    this.cols = c;
    this.cells = [];
    for( var i = 0; i < this.cols; i++ ) {
      this.cells.push( new Cell( c, c, i ))
    }
  }
  setVals( cells ) {
    // fill values from an existing cell array
    if( cells.length == this.cols ) {
      for( var c = 0; c < cells.length; c++ ) {
        this.cells[c].setValue( cells[c].value() );
      }
    }
  }
  getLength() {
    return this.cols;
  }
}

class RowNeighborDescriptor {
  constructor( id ) {
    this.id = id;
    this.ceilingNeighbor = false;
    this.floorNeighbor = false;
  }
  getId() {
    return this.id;
  }
}

const BLANK = -1;
const WHITE = 0;
const BLACK = 1;
const YY_LEFT = 0;
const YY_UP = 1;
const YY_RIGHT = 2;
const YY_DOWN = 3;

const COLORS = [ BLACK, WHITE ];
const DIRECTIONS = [ YY_LEFT, YY_UP, YY_RIGHT, YY_DOWN ];

class Cell {
  constructor(id, r, c) {
    this.id = id;
    this.row = r;
    this.col = c;
    this.val = BLANK;
  }
  setValue(color) {
    this.val = color;
  }
  value() {
    return this.val;
  }
}

class Board {
  constructor(rows, cols, descriptor ) {
    this.rows = rows;
    this.cols = cols;
    var colorAssignments = null;

    if( descriptor ) {
      // generate an map of ids to colors
      colorAssignments = {};
      var cellDescriptors = descriptor.split(",");
      // each one is like w23 or b12, where 23 and 12 are absolute ids
      for( var i = 0; i < cellDescriptors.length; i++ ) {
        colorAssignments[parseInt(cellDescriptors[i].substring(1))] = cellDescriptors[i].charAt(0) == 'w' ? WHITE : BLACK;
      }
    }

    this.cells = [];
    for( var r = 0; r < this.rows; r++ ) {
      this.cells.push( [] );
      for( var c = 0; c < this.cols; c++ ) {
        this.cells[r].push( new Cell( r*this.cols+c, r, c ) );
        if( colorAssignments && colorAssignments[r*this.cols+c] != null ) {
          this.cells[r][c].setValue(colorAssignments[r*this.cols+c]);
        }
      }
    }
  }
  toString() {
    var cell = null;
    var first = true;
    var retVal = "";
    for( var i = 0; i < this.rows * this.cols; i++ ) {
      cell = this.getCellById( i );
      if( cell.value() != BLANK ) {
        if( !first ) {
          retVal += ",";
        } else {
          first = false;
        }
        retVal += ((cell.value() == BLACK ? "b" : "w" ) + cell.id);
      }
    }
    return retVal;
  }
  printBoard() {
    var retVal = "";
    for( var r = 0; r < this.rows; r++ ) {
      for( var c = 0; c < this.cols; c++ ) {
        if( this.cells[r][c].value() == BLACK ) {
          retVal += "\u25cf ";
        } else if( this.cells[r][c].value() == WHITE ) {
          retVal += "\u25cb ";
        } else {
          retVal += "- ";
        }
      }
      retVal = retVal.trim();
      retVal += "\n";
    }
    //console.log(retVal);
    return retVal.trim();
  }
  getRowArray() {
    var retVal = [];
    var tempRow = null;
    for( var r = 0; r < this.rows; r++ ) {
      tempRow = new Row( r, this.cols );
      tempRow.setVals( this.cells[r] );
      retVal.push( tempRow );
    }
    return retVal;
  }
  getCellById( id ) {
    return this.cells[Math.floor(id / this.cols)][id % this.cols];
  }
  getCell( r, c ) {
    var retVal = null;
    if( r >= 0 && r < this.rows && c >= 0 && c < this.cols ) {
      retVal = this.cells[r][c];
    }
    return retVal;
  }
  isSquare() {
    return this.rows == this.cols;
  }
  empty() {
    for( var r = 0; r < this.rows; r++ ) {
      for( var c = 0; c < this.cols; c++ ) {
        this.cells[r][c].setValue(BLANK);
      }
    }
  }
  setRowValues( r, idx ) {
    if( r != null && r.getLength() == this.cols && idx >= 0 && idx < this.rows ) {
      for( var c = 0; c < r.getLength(); c++ ) {
        this.cells[idx][c].setValue(r.cells[c].value());
      }
    }
  }
  setAllRowValues( color, idx ) {
    for( var c = 0; c < this.cols; c++ ) {
      this.cells[idx][c].setValue( color );
    }
  }
  getCellNeighbor( cell, dir ) {
    var retVal = null;
    var r = cell.row;
    var c = cell.col;
    if( dir == YY_LEFT ) {
      c = c - 1;
    } else if( dir == YY_UP ) {
      r = r - 1;
    } else if( dir == YY_RIGHT ) {
      c = c + 1;
    } else if( dir == YY_DOWN ) {
      r = r + 1;
    }

    return this.getCell(r,c);
  }
  valid() {
    var retVal = true;
    // check for 2x2 solid blocks.
    // do we need this??
    // for( var r = 0; r < this.rows; r++ ) {
    //   for( var c = 0; c < this.cols; c++ ) {
        
    //   }
    // }
    
    var colors = this.regionColoringCheck();
    
    if( colors > 2 ) {
      retVal = false;
    }
    
    return retVal;
  }
  regionColoringCheck() {
    var start = null;
    var seen = new Set();
    var colorSets = [];
    var tempColorSet;
    // check the black region

    for( var c = 0; c < COLORS.length; c++ ) {
      while( (start = this.findFirstNotIn(COLORS[c], seen )) != null ) {
        tempColorSet = new Set();
        this.colorInner( start, tempColorSet, COLORS[c] );
        colorSets.push(tempColorSet);
        for( let item of tempColorSet ) seen.add(item);
      }
    }
    return colorSets.length;
  }
  colorInner( cell, colorSet, color ) {
    var tempNeighbor;
    colorSet.add( cell.id );
    for( var d = 0; d < DIRECTIONS.length; d++ ) {
      if( (tempNeighbor = this.getCellNeighbor(cell,DIRECTIONS[d])) != null &&
          tempNeighbor.value() == color && !colorSet.has( tempNeighbor.id ) ) {
        this.colorInner( tempNeighbor, colorSet, color );
      }
    }
  }
  checkEscapability() {
    var retVal = true;
    var start = null;
    var seen = new Set();
    for( var c = 0; c < COLORS.length; c++ ) {
      while( (start = this.findFirstNotIn(COLORS[c], seen )) != null ) {
        retVal &= this.checkEscapabilityInner( start, seen );
      }
    }
    return retVal;
  }
  checkEscapabilityInner( cell, seen ) {
    var retVal = false;
    var tempNeighbor = null;
    seen.add( cell.id );
    for( var d = 0; d < DIRECTIONS.length; d++ ) {
      if( (tempNeighbor = this.getCellNeighbor(cell,DIRECTIONS[d])) != null &&
          !seen.has( tempNeighbor.id ) ) {
        if( tempNeighbor.value() == BLANK ) {
          retVal = true;
        } else if( tempNeighbor.value() == cell.value() ) {
          retVal |= this.checkEscapabilityInner( tempNeighbor, seen );
        }
      }
    }
    return retVal;
  }
  findFirstNotIn( cellVal, cellSet ) {
    var cell = null
    for( var r = 0; r < this.rows; r++ ) {
      for( var c = 0; c < this.cols; c++ ) {
        cell = this.cells[r][c];
        if( cell.value() == cellVal && !cellSet.has( cell.id ) ) {
          return cell;
        }
      }
    }
    return null;
  }
}

class Solver {
  constructor(rows, cols, descriptor ) {
    this.rows = rows;
    this.cols = cols;
    this.board = new Board( rows, cols );
    this.allRows = generateAllRows( cols );
    this.rowRelations = {};
    this.solutions = null;
    this.solutionCount = 0;
    var tempList = null;
    var currRow = null;
    var checkRow = null;
    var neighborDescriptor = null;
    for( var i = 0; i < (1 << cols); i++ ) {
      currRow = this.allRows[i];
      tempList = [];
      for( var j = 0; j < (1 << cols); j++ ) {
        checkRow = this.allRows[j];
        if( (neighborDescriptor = canBeNeighbors(currRow, checkRow)) != null ) {
          tempList.push(neighborDescriptor);
        }
      }
      this.rowRelations[currRow.id] = tempList;
    }

    this.rowIndex = {}
    this.allNeighborDescriptors = [];
    for( var i = 0; i < this.allRows.length; i++ ) {
      this.rowIndex[ this.allRows[i].id] = this.allRows[i];
      this.allNeighborDescriptors.push( new RowNeighborDescriptor(this.allRows[i].id) );
    }

    this.rowRelations[-1] = this.allNeighborDescriptors;

    // do the row restrictions
    if( descriptor != null && /^(?:[wb][0-9]+,?)+$/.test(descriptor) ) {
      this.rowRestrictions = {};
      var cellDescriptors = descriptor.split(",");
      var idx = 0;
      var cellDesc = "";
      for( var c = 0; c < cellDescriptors.length; c++ ) {
        cellDesc = cellDescriptors[c];
        idx = parseInt( cellDesc.substring(1) );
        if( cellDesc.charAt(0) == 'w' ) {
          this.board.getCellById(idx).setValue( WHITE );
        } else if( cellDesc.charAt(0) == 'b' ) {
          this.board.getCellById(idx).setValue( BLACK );
        }
      }
      
      var rowArray = this.board.getRowArray();
      
      for( var i = 0; i < this.board.rows; i++ ) {
        this.rowRestrictions[i] = grep(rowArray[i], this.allRows);
      }
    }

  }
  boardFullness() {
    var cellCount = this.board.rows * this.board.cols;
    var filledCount = 0;
    for( var r = 0; r < this.board.rows; r++ ) {
      for( var c = 0; c < this.board.cols; c++ ) {
        if( this.board.cells[r][c].value() != BLANK ) {
          filledCount++;
        }
      }
    }

    return Math.floor( (100*filledCount) / cellCount );
  }
  isBoardFull() {
    return this.boardFullness() == 100;
  }
  checkSolution() {
    var sol = this.board.toString();
    if( this.solutions && !this.solutions.has(sol)) {
      this.solutionCount++;
      this.solutions.add(sol);
      this.board.printBoard();
    }
  }
  solvePromise() {
    return new Promise( (resolve, reject ) => {
      if( this.board.rows * this.board.cols > 30 && this.boardFullness() < 10 ) {
        reject( "Board must be 10% full to solve. (It takes too long!!)");
      } else {
        setTimeout( () => {
          var solutionSet = this.findRowSolutions();
          resolve( solutionSet );
        },0);
      }
    });
  }

  findRowSolutions() {
    this.solutions = new Set();
    this.solutionCount = 0;
    //if( !this.isBoardFull() ) {
      // we've done all of our row analysis - clear 
      // the board and begin.
      this.board.empty();
      this.findRowSolutionsInner(-1, 0);
    //} else {
    //  this.findRowSolutionsInner(-1, this.board.rows );
    //}
    return this.solutions;
  }
  findRowSolutionsInner( prevRowId, rowIdx ) {
    if( rowIdx == this.board.rows ) {
      if( this.board.valid() ) {
        this.checkSolution();
      }
    } else {
      if( rowIdx > 2 && rowIdx < this.board.rows ) {
        if( !this.board.checkEscapability() ) {
          // bail out, we're bogus!
          return;
        }
      }
      var rowsToTry = this.rowRelations[ prevRowId ];
      var r = null;
      var rnd = null;
      var restrictions = null;
      for( var i = 0; i < rowsToTry.length; i++ ) {
        rnd = rowsToTry[i];
        
        if( rowIdx == 1 && this.board.rows > 2 && !rnd.ceilingNeighbor ) {
          continue;
        } else if( rowIdx == this.board.rows - 1 && this.board.rows > 2 && !rnd.floorNeighbor ) {
          continue;
        }
        
        if( this.rowRestrictions != null ) {
          restrictions = this.rowRestrictions[rowIdx];
          if( !restrictions.has(rnd.getId())) {
            continue;
          }
        }
        
        r = this.rowIndex[ rnd.getId() ];
        this.board.setRowValues(r, rowIdx);
        this.findRowSolutionsInner(rnd.getId(), rowIdx + 1);
      }
      this.board.setAllRowValues(BLANK, rowIdx);
    }
  }
}

function generateAllRows(len) {
  var count = 1 << len;
  var retVal = [];
  var c = null;
  var shift = 0;
  var bitcheck = 1;
  var tempRow;
  for (var i = 0; i < count; i++) {
    shift = 0;

    tempRow = new Row(i, len);
    retVal.push( tempRow );
    while (shift < len) {
      bitcheck = 1 << shift;
      if ((bitcheck & i) > 0) {
        retVal[i].cells[shift].setValue(BLACK);
      } else {
        retVal[i].cells[shift].setValue(WHITE);
      }
      shift++;
    }
  }
  return retVal;
}

/**
 * get a set of row ids from rowArray that 
 * match the template row
 * @param {*} row 
 * @param {*} rowArray 
 */
function grep( row, rowArray ) {
  var retVal = new Set();
  var tempRow = null;
  var tempCell = null;
  var matches = true;
  for( var r = 0; r < rowArray.length; r++ ) {
    tempRow = rowArray[r];
    matches = true;
    for( var c = 0; c < tempRow.cells.length; c++ ) {
      if( row.cells[c].value() != BLANK && 
          row.cells[c].value() != tempRow.cells[c].value() ) {
            matches = false;
            break;
      } 
    }
    if( matches ) {
      retVal.add(tempRow.id);
    }
  }
  return retVal;
}

function isEdgeSafe( a, b ) {
  // now check to make sure that b wouldn't "trap" any cells.  
  var currColor = BLANK;
  var currEscapeStatus = false;
  var i = 0;
  while(i < a.getLength() ) {     
    if( currColor != BLANK &&  currColor != a.cells[i].value() ) {
      if( !currEscapeStatus ) {
        // Fail!  We switched colors without finding the previous one an escape
        // ok = false;
        break;
      } else {
        currColor = a.cells[i].value();
      }
    } else {
      currColor = a.cells[i].value();
    }
    
    if( b.cells[i].value() == currColor ) {
      currEscapeStatus = true; // this color can escape, jump forward to the next color.
      // ok = true;
      while( i < a.getLength() && a.cells[i].value() == currColor ) i++;
    } else {
      currEscapeStatus = false;
      i++;
    }
  }
  
  return currEscapeStatus;
}

function canBeNeighbors( row0, row1 ) {
  var retVal = null;
  var ok = true;
  
  // System.out.println(" Checking: \n" + a + " and \n" + b + "..." );
  
  if( row0 != null && row1 != null && row0.getLength() == row1.getLength() ) {
    
    // first check to see if we will create any solid 2x2s
    for( var i = 0; i < row0.getLength() - 1; i++ ) {
      if( row0.cells[i].value() == row0.cells[i+1].value() &&
          row1.cells[i].value() == row0.cells[i].value() &&
          row1.cells[i].value() == row1.cells[i+1].value() ) {
            ok = false;
            break;
      }
    }
    
    if( ok ) {
      retVal = new RowNeighborDescriptor( row1.id );
      retVal.ceilingNeighbor = isEdgeSafe(row0,row1);
      retVal.floorNeighbor = isEdgeSafe(row1,row0);
    }      
  }
  
  return retVal;
}


