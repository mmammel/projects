
  const ERR = -1;
  const LEFT = 0;
  const UP = 1;
  const RIGHT = 2;
  const DOWN = 3;

  const cellBorderColor = "white";
  const cellBackgroundColor = "#D9D9D9";
  const highlightColor = "#03BCCE";
  const optimalHighlightColor = "#FFA700";

  const MOVERS = [ [0, -1], [-1, 0], [0, 1], [1, 0] ];

class Grid {
  constructor(containerId, rows, cols, startCoord, endCoord, pointArray ) {
    this.containerId = containerId;
    this.fillCount = 0;
    this.model = [];
    this.index = {};
    this.history = [];
    //this.shapes = ['+','bell','t','o'];
    this.shapes = ['padlock', 'heart', 'bell', 'clock'];
    shuffle(this.shapes);
    this.points = pointArray;
    this.pointMap = {};
    this.negativePoints = 0;

    //build pointMap
    for( var i = 0; i < this.shapes.length; i++ ) {
      this.pointMap[ this.shapes[i] ] = this.points[i];
    }

    this.pathPoints = 0;
    this.historyIdx = -1;
    this.pathTotalLimit = 0;
    this.rows = rows;
    this.cols = cols;
    this.currPos = [0, 0];
    this.startPos = startCoord;
    this.endPos = endCoord;
    this.viewBox = [0, 0, 19 * this.cols + 1, 19 * this.rows + 1];
    this.touchContext = {
      currId: "",
      dragging: false,
      //dragValue: 'clear',
      timestamp: 0
    };
    this.buildGrid();
  }

  getAdjacentCells( c ) {
    var retVal = [];

    if( c.r > 0 ) {
      // cell above
      retVal.push( this.model[c.r - 1][c.c]);
    }

    if( c.r < this.rows - 1 ) {
      // cell below
      retVal.push( this.model[c.r + 1][c.c] );
    }

    if( c.c > 0 ) {
      // cell to the left
      retVal.push( this.model[c.r][c.c - 1] );
    }

    if( c.c < this.cols - 1 ) {
      // cell to the right
      retVal.push( this.model[c.r][c.c + 1]);
    }
    
    return retVal;
  }

  /*
   * find a naive path from start to finish (that might be the best!)
   * so we can cap our recursive search.
   */
  setPathTotalLimit() {
    var total = 0;
    var rinc = 0, cinc = 0;
    rinc = this.startPos[0] - this.endPos[0] > 0 ? -1 : 1;
    cinc = this.startPos[1] - this.endPos[1] > 0 ? -1 : 1;

    var temp = this.startPos[0];
    while( temp != this.endPos[0] ) {
      if( this.model[temp][this.startPos[1]].points > 0 ) {
        total += this.model[temp][this.startPos[1]].points;
      }
      temp += rinc;
    }

    temp = this.startPos[1];
    while( temp != this.endPos[1] ) {
      if( total += this.model[this.endPos[0]][temp].points > 0 ) {
        total += this.model[this.endPos[0]][temp].points;
      }
      temp += cinc;
    }

    this.pathTotalLimit = total;
  }
  
  getIndexFromCoord( r, c ) {
    return r * this.cols + c;
  }

  computeOptimalBellmanFord() {
    // build edges
    var edges = [];
    var tempCell = null, tempAdjacentCell = null;
    var tempAdjacent = null;
    for( var i = 0; i < this.rows; i++ ) {
      for( var j = 0; j < this.cols; j++ ) {
        tempCell = this.model[i][j];
        if( tempCell.isEnd ) continue;
        tempAdjacent = this.getAdjacentCells(tempCell);
        for( var a = 0; a < tempAdjacent.length; a++ ) {
          tempAdjacentCell = tempAdjacent[a];
          if( tempAdjacentCell.isStart ) continue;
          edges.push( new Edge( tempCell, tempAdjacentCell ) );
        }
      }
    }

    var dist = [];

    for( var i = 0; i < this.rows * this.cols; i++ ) {
      dist.push( Number.MAX_VALUE);
    }

    dist[ this.getIndexFromCoord( this.startPos[0], this.startPos[1] ) ] = 0;

    // Now relax the paths
    var s = null;
    var d = null;
    var edge = null;
    var weight = 0, srcIdx = 0, destIdx = 0;

    for( var i = 1; i < dist.length; i++ ) {
      for( var e = 0; e < edges.length; e++ ) {
        edge = edges[e];
        s = edge.source;
        d = edge.destination;
        weight = edge.weight;
        srcIdx = this.getIndexFromCoord(s.r, s.c);
        destIdx = this.getIndexFromCoord(d.r, d.c );
        if (dist[srcIdx] != Number.MAX_VALUE && dist[srcIdx] + weight < dist[destIdx]) {
          dist[destIdx] = dist[srcIdx] + weight;
          d.memo = dist[destIdx];
          d.optimalPrevCell = s;
        }
      }
    }
  }

  computeOptimalPath() {
    this.setPathTotalLimit();
    this.computeOptimalPathInternal(0, this.startPos[0], this.startPos[1], null );

    // Optimal points are in: this.board[this.endPos[0]][this.endPos[1]].memo
    // Optimal path can be traced using "optimalPrevCell" back from the endPos.
  }

  computeOptimalPathInternal(total, row, col, from) {
    var deadEnd = true;
    var currCell = this.model[row][col];
    var origMemo = currCell.memo;
    var origPrev = currCell.optimalPrevCell;

    if( total > (this.pathTotalLimit + this.negativePoints) ) {
      return true;
    }

    currCell.memo = total;
    total += currCell.points;

    if( from ) {
      currCell.optimalPrevCell = from;
    }

    if( currCell.isEnd ) {
      return false;
    } else {
      currCell.optimalVisited = true;
    
      if( this.computeOptimalCanMove( total, currCell, UP) ) {
        deadEnd &= this.computeOptimalPathInternal(total, row - 1, col, currCell);
      }

      if( this.computeOptimalCanMove( total, currCell, RIGHT) ) {
        deadEnd &= this.computeOptimalPathInternal(total, row, col + 1, currCell);
      }

      if( this.computeOptimalCanMove( total, currCell, DOWN) ) {
        deadEnd &= this.computeOptimalPathInternal(total, row + 1, col, currCell);
      }

      if( this.computeOptimalCanMove( total, currCell, LEFT) ) {
        deadEnd &= this.computeOptimalPathInternal(total, row, col - 1, currCell);
      }

      currCell.optimalVisited = false;

      if( deadEnd ) {
        currCell.memo = origMemo;
        currCell.optimalPrevCell = origPrev;
      }
    }

    return deadEnd;
  }

  computeOptimalCanMove( total, cell, direction ) {
    var retVal = false;
    switch(direction) {
      case UP:
        retVal = cell.r > 0 && !this.model[cell.r - 1][cell.c].optimalVisited && total < this.model[cell.r - 1][cell.c].memo;
        break;
      case RIGHT:
        retVal = cell.c < this.cols - 1 && !this.model[cell.r][cell.c + 1].optimalVisited && total < this.model[cell.r][cell.c + 1].memo;
        break;
      case DOWN:
        retVal = cell.r < this.rows - 1 && !this.model[cell.r + 1][cell.c].optimalVisited && total < this.model[cell.r + 1][cell.c].memo;
        break;
      case LEFT:
        retVal = cell.c > 0 && !this.model[cell.r][cell.c - 1].optimalVisited && total < this.model[cell.r][cell.c - 1].memo;
        break;
      default:
        retVal = false;
        break;
    }

    return retVal;
  }

  updateFillCount(amt) {
    this.fillCount += amt;
    if( this.fillCount == this.rows * this.cols ) {
      // it's full, throw an event
      $( document ).trigger('gridFull');
    }
  }

  buildGrid() {
    this.model = [];
    this.index = {};
    this.currPos = [0, 0];
    var tempCols;
    var tempGridCell;
    for (var r = 0; r < this.rows; r++) {
      tempCols = [];
      this.model.push(tempCols);
      for (var c = 0; c < this.cols; c++) {
        tempGridCell = new GridCell(this,r,c);
        tempCols.push(tempGridCell);
        this.index[ tempGridCell.id ] = tempGridCell;
      }
    }
  }

  doFinish() {
    var optimalSolution = this.model[this.endPos[0]][this.endPos[1]].memo;
    if( this.pathPoints > optimalSolution ) {
      // spit out a positive message, show the optimal path
      var tempCell = this.model[this.endPos[0]][this.endPos[1]];

      while( !tempCell.isStart ) {
        tempCell.newOptimalPathHighlight(tempCell.optimalPrevCell);
        tempCell = tempCell.optimalPrevCell;
      }

      tempCell.newOptimalPathHighlight(tempCell.optimalPrevCell);

      $('#resultModalMessage').html('Your path totaled '+this.pathPoints+' and was '+(this.pathPoints - optimalSolution)+' points off the mark.  The optimal path will be highlighted.');
      
      $('#yourPath').text('Your Path = ' + this.pathPoints + ' points');
      $('#optimalPath').text('Optimal Path = ' + optimalSolution + ' points');
      $('#results').show();
    } else if( this.pathPoints == optimalSolution ) {
      $('#resultModalMessage').html("Perfection!  You found the optimal path, accumulating " + optimalSolution + " points!");
    }

    var modalEl = document.getElementById('resultsModal');
    var modal = bootstrap.Modal.getOrCreateInstance(modalEl);
    modal.show();
  }

  draw() {
    // build the root element
    this.svgRoot = makeSVG("svg", { id: 'svgRoot', viewBox: '' + this.viewBox[0] + ' ' + this.viewBox[1] + ' ' + this.viewBox[2] + ' ' + this.viewBox[3] });
    // make the gridCells
    for (var i = 0; i < this.rows; i++) {
      for (var j = 0; j < this.cols; j++) {
        $(this.svgRoot).append(this.model[i][j].getElement());
      }
    }
    $('#' + this.containerId).prepend(this.svgRoot);

    // randomly fill the grid with characters, except for the A and B
    var symbols = [ 'padlock', 'bell', 'heart', 'clock' ];
    for( var i = 0; i < this.rows; i++ ) {
      for( var j = 0; j < this.cols; j++ ) {
        // get random action
        var rand = Math.floor(Math.random() * 4);
        this.model[i][j].doAction(symbols[rand]);
        if( this.model[i][j].points < 0 ) {
          this.negativePoints += (-1 * this.model[i][j].points );
        }
      }
    }

    this.model[this.endPos[0]][this.endPos[1]].doAction('b');
    this.model[this.startPos[0]][this.startPos[1]].doAction('a');
    this.model[this.startPos[0]][this.startPos[1]].highlight();
    this.currPos = [this.startPos[0],this.startPos[1]];

    this.setupHandlers();
  }

  incrementCursor(dir) {
    var newX = this.currPos[0];
    var newY = this.currPos[1];
    var mover = MOVERS[dir];
    newX += mover[0];
    newY += mover[1];
    if (newX >= this.rows || newX < 0 || newY >= this.cols || newY < 0) {
      return;
    } else {
      this.moveCursorTo(newX,newY);
    }
  }

  areAdjacent( cell1, cell2 ) {
    var retVal = false;
    if( cell1.r == cell2.r ) {
      retVal = cell1.c == (cell2.c + 1) || cell1.c == (cell2.c - 1);
    } else if( cell1.c == cell2.c ) {
      retVal = cell1.r == (cell2.r + 1) || cell1.r == (cell2.r - 1);
    }

    return retVal;
  } 

  moveCursorTo( newX, newY ) {
    var from = this.model[ this.currPos[0] ][ this.currPos[1] ];
    var to = this.model[ newX ][ newY ];
    
    if( from && to ) {
      if( !to.visited && this.areAdjacent(to,from) ) {
        to.prevCell = from;
        to.visited = true;
        to.newHighlight(from);
        this.currPos[0] = newX;
        this.currPos[1] = newY;
        this.pathPoints += to.points;

        if( to.isEnd ) {
          // they finished the path - shut 'er down
          this.doFinish();
        }
      } else if( from.prevCell && to.id === from.prevCell.id ) {
        // we are moving back
        this.pathPoints -= from.points;
        from.newUnhighlight(to);
        from.visited = false;
        from.prevCell = null;
        //to.highlight();
        this.currPos[0] = newX;
        this.currPos[1] = newY;
      }

      //$('#pathPoints').text(''+this.pathPoints);
    }

  }
  applyHistory(historyObj) {
    var retVal = false;
    var targetCell = this.index[ historyObj.id ];
    if( targetCell ) {
      retVal = targetCell.applyHistory(historyObj);
    }

    return retVal;
  }
  doRedo() {
    var retVal = false;
    if( this.historyIdx < this.history.length - 1 ) {
      this.historyIdx++;
      this.applyHistory( this.history[this.historyIdx].post );
      retVal = true;
    }
    return retVal;
  }
  doUndo() {
    var retVal = false;
    if( this.historyIdx > -1 ) {
      this.applyHistory( this.history[this.historyIdx--].pre );
      retVal = true;
    }
    return retVal;
  }
  doCellAction( action ) {
    var cell = this.getCurrentCell();
    var preHistoryObject = cell.getHistoryObject();
    if( cell && cell.doAction( action ) ) {
      var postHistoryObj = cell.getHistoryObject();
      var summaryObj = {
        pre: preHistoryObject,
        post: postHistoryObj
      };
      if( this.historyIdx < this.history.length - 1 ) {
        // lop off the history stack, we've done something new.
        this.history = this.history.slice(0, this.historyIdx + 1 );
      }
      this.history.push(summaryObj);
      this.historyIdx++;
    }
  }
  handleTouchStart(e) {
    e.preventDefault();
    this.touchContext.currId = this.getIdFromTarget($(e.target));
    var cell = this.index[this.touchContext.currId];
    //this.touchContext.dragValue = cell.content;
    this.touchContext.dragging = true;
    var d = new Date();
    this.touchContext.touchStartTime = d.getTime();
  }
  handleTouchEnd(e) {
    e.preventDefault();
    this.touchContext.currId = "";
    this.touchContext.dragging = false;
    var d = new Date();
    if( d.getTime() - this.touchContext.touchStartTime < 300 ) {
      this.handleClick(e);
    }
  }
  handleMouseDown(e) {
    this.touchContext.currId = this.getIdFromTarget($(e.target));
    var cell = this.index[this.touchContext.currId];
    //this.touchContext.dragValue = cell.content;
    this.touchContext.dragging = true;
  }
  handleMouseUp(e) {
    e.preventDefault();
    this.touchContext.currId = "";
    this.touchContext.dragging = false;
  }
  handleMouseMove(e) {
    if( this.touchContext.dragging ) {
      e.preventDefault();
      var id = this.getIdFromTarget($(e.target));
      if( id != this.touchContext.currId ) {
        var cell = this.index[id];
        if( cell ) {
          this.touchContext.currId = id;
          this.moveCursorTo(cell.r,cell.c);
          //this.doCellAction(this.touchContext.dragValue);
        }
      }
    }
  }
  handleTouchMove(e) {
    if( this.touchContext.dragging ) {
      e.preventDefault();
      var latestTouch = e.originalEvent.changedTouches[0];
      var latestTarget = document.elementFromPoint(latestTouch.clientX, latestTouch.clientY);
      var id = this.getIdFromTarget($(latestTarget));
      if( id != null && id != this.touchContext.currId ) {
        var cell = this.index[id];
        if( cell ) {
          this.touchContext.currId = id;
          this.moveCursorTo(cell.r,cell.c);
          //this.doCellAction(this.touchContext.dragValue);
        }
      }
    }
  }
  handleClick(e) {
    this.touchContext.currId = "";
    this.touchContext.dragging = false;
    var id = this.getIdFromTarget($(e.target));
    var cell = this.index[id];
    if( cell ) {
      if( cell.r == this.currPos[0] && cell.c == this.currPos[1] ) {
        // we didn't move...cycle the values
        // if( cell.content === 'clear' ) {
        //   this.doCellAction('o');
        // } else if( cell.content === 'o' ) {
        //   this.doCellAction('x');
        // } else if( cell.content === 'x' ) {
        //   this.doCellAction('clear');
        // }
      } else {
        this.moveCursorTo(cell.r,cell.c);
      }
    }
  }
  getIdFromTarget(target) {
    return target.attr('id') ? target.attr('id').replace(/^([0-9]+?_[0-9]+?)_.*$/, "$1") : null;
  }
  handleKeyDown(e) {
    // First see if this is an "undo" or a "redo"
    if( e.which === 87 ) {
      this.doRedo();
    } else if( e.which === 81 ) {
      this.doUndo();
    } else if (e.which == 37) {
      this.incrementCursor(LEFT);
      e.preventDefault();
    } else if (e.which == 38) {
      this.incrementCursor(UP);
      e.preventDefault();
    } else if (e.which == 39) {
      this.incrementCursor(RIGHT);
      e.preventDefault();
    } else if (e.which == 40) {
      this.incrementCursor(DOWN);
      e.preventDefault();
    } else if (e.which == 65) {
      this.doCellAction('a');
    } else if (e.which == 66) {
      this.doCellAction('b');
    } else if (e.which == 80) {
      this.doCellAction('p');
    } else if (e.which == 84) {
      this.doCellAction('t');
    } else if (e.which == 187) {
      this.doCellAction('+');
    } else if (e.which == 32) {
      this.doCellAction('clear');
      e.preventDefault();
    } else if (e.which == 79) {
      this.doCellAction('o');
    } else if (e.which == 76) {
      this.doCellAction('lock');
    } else if (e.which == 85) {
      this.doCellAction('unlock');
    }
  }
  getCurrentCell() {
    return this.model[ this.currPos[0] ][ this.currPos[1] ];
  }
  setupHandlers() {
    var that = this;
    $(document).off('keydown');
    $(document).keydown(function (e) {
      that.handleKeyDown(e);
    });
   
    $('#'+this.containerId).on('touchstart', e => {
      this.handleTouchStart(e);
    }).on('touchend', e => {
      this.handleTouchEnd(e);
    }).on('mousedown', e => {
      this.handleMouseDown(e);
    }).on('mouseup', e => {
      this.handleMouseUp(e);
    }).on('mousemove', e => {
      this.handleMouseMove(e);
    }).on('touchmove', e => {
      this.handleTouchMove(e);
    });


    $('#'+this.containerId).click( function(e) {
      that.handleClick(e);
    });
    $('#'+this.containerId).on("tap", function(e) {
      that.handleClick(e);
    });

  }
}

class Edge {
  constructor(s,d) {
    this.source = s;
    this.destination = d;
    this.weight = d.points;
  }
}

class GridCell {
  constructor(grid, r, c) {
    this.parentGrid = grid;
    this.r = r;
    this.c = c;
    this.id = '' + r + '_' + c;
    this.locked = false;
    this.content = 'clear';
    this.points = 0;
    this.memo = Number.MAX_VALUE;
    this.visited = false;
    this.optimalVisited = false;
    this.prevCell = null;
    this.optimalPrevCell = null;
    this.isStart = false;
    this.isEnd = false;
    this.actions = {
      'lock' : {
        action: () => {
          var retVal = false;
          if( !this.locked ) {
            this.locked = true;
            retVal = true;
          }
          return retVal;
        }
      },
      'unlock' : {
        action: () => {
          var retVal = false;
          if( this.locked ) {
            this.locked = false;
            retVal = true;
          }
          return retVal;
        },
        lockResistant: true
      },
      'clear' : {
        // clear it
        action: () => {
          var retVal = false;
          if( this.content != 'clear' ) {
            this.clearDecorator();
            retVal = true;
          }
          return retVal;
        },
        content: true
      },
      't' : {
        // triangle  
        action: () => {
          this.points = this.parentGrid.pointMap['t'];
          var retVal = false;
          if( this.content != 't' ) {
            this.clearDecorator();
            /// closed triangle.
            var cx = (this.c * 19) + 10;
            var cy = (this.r * 19) + 10;
            this.decorator = makeSVG('g', { id: this.id + '_decorator' });
            var triangle = makeSVG('polygon', {
            id: this.id + '_decorator_inner', 
               points: ''+ cx + ',' + (cy - 7) + ' ' + 
                           (cx + (14 / Math.sqrt(3))) + ',' + (cy + (14 / Math.sqrt(3))) + ' ' +
                           (cx - (14 / Math.sqrt(3))) + ',' + (cy + (14 / Math.sqrt(3))), 
               fill: 'black', 'stroke-width': 0
            });
            
            $(this.decorator).append(triangle);
            $(this.element).append(this.decorator);
            retVal = true;
          }
          return retVal;
        },
        content: true
      },
      'p' : {
        // pentagon  
        action: () => {
          this.points = this.parentGrid.pointMap['p'];
          var retVal = false;
          if( this.content != 'p' ) {
            this.clearDecorator();
            /// closed triangle.
            var cx = (this.c * 19) + 10;
            var cy = (this.r * 19) + 10;
            var rad18 = 18 * (Math.PI / 180);
            var rad54 = 54 * (Math.PI / 180);
            this.decorator = makeSVG('g', { id: this.id + '_decorator' });
            var pentagon = makeSVG('polygon', {
            id: this.id + '_decorator_inner', 
               points: ''+ cx + ',' + (cy - 8) + ' ' + 
                           (cx + (8 * Math.cos(rad18))) + ',' + (cy - (8 * Math.sin(rad18))) + ' ' +
                           (cx + (8 * Math.cos(rad54))) + ',' + (cy + (8 * Math.sin(rad54))) + ' ' +
                           (cx - (8 * Math.cos(rad54))) + ',' + (cy + (8 * Math.sin(rad54))) + ' ' +
                           (cx - (8 * Math.cos(rad18))) + ',' + (cy - (8 * Math.sin(rad18))),
               fill: 'black', 'stroke-width': 0
            });
            
            $(this.decorator).append(pentagon);
            $(this.element).append(this.decorator);
            retVal = true;
          }
          return retVal;
        },
        content: true
      },
      '+' : {
        // plus sign 
        action: () => {
          this.points = this.parentGrid.pointMap['+'];
          var retVal = false;
          if( this.content != '+' ) {
            this.clearDecorator();
            // plus sign - two rectangles
            var cx = (this.c * 19) + 10;
            var cy = (this.r * 19) + 10;
            this.decorator = makeSVG('g', { id: this.id + '_decorator' });
            var vertRect = makeSVG('rect', {
            id: this.id + '_decorator_vert', x: ''+(cx - 3), y: ''+(cy - 8), width: ''+6, height: ''+16, 
            fill: 'black', 'stroke-width': 0
            });
            var horizRect = makeSVG('rect', {
            id: this.id + '_decorator_horiz', x: ''+(cx - 8), y: ''+(cy - 3), width: ''+16, height: ''+6, 
            fill: 'black', 'stroke-width': 0
            });
            $(this.decorator).append(vertRect);
            $(this.decorator).append(horizRect);
            $(this.element).append(this.decorator);
            retVal = true;
          }
          return retVal;
        },
        content: true
      },
      'a' : {
        // the letter a
        action: () => {
          var retVal = false;
          this.points = 0;
          this.isStart = true;
          this.visited = true;
          if( this.content != 'a' ) {
            this.clearDecorator();
            this.decorator = makeSVG('g', { id: this.id + '_decorator' });
            var theLetter = makeSVG('text', {
            id: this.id + '_decorator_inner', x: (this.c * 19) + 10, y: (this.r * 19) + 14.5,
              'text-anchor': 'middle', textLength: 20, 'font-family' : 'neuzeit-grotesk', 'font-weight' : 'bold',
              fill: 'black', 'stroke-width': 0
            });
            $(theLetter).css('font-size','75%');
            theLetter.innerHTML = 'A';
            $(this.decorator).append(theLetter);
            $(this.element).append(this.decorator);
            retVal = true;
          }
          return retVal;
        },
        content: true
      },
      'b' : {
        // the letter b
        action: () => {
          this.isEnd = true;
          this.points = 0;
          var retVal = false;
          if( this.content != 'b' ) {
            this.clearDecorator();
            this.decorator = makeSVG('g', { id: this.id + '_decorator' });
            var theLetter = makeSVG('text', {
            id: this.id + '_decorator_inner', x: (this.c * 19) + 10, y: (this.r * 19) + 14.5,
              'text-anchor': 'middle', textLength: 20, 'font-family' : 'neuzeit-grotesk', 'font-weight' : 'bold',
              fill: 'black', 'stroke-width': 0
            });
            theLetter.innerHTML = 'B';
            $(theLetter).css('font-size','75%');
            $(this.decorator).append(theLetter);
            $(this.element).append(this.decorator);
            retVal = true;
          }
          return retVal;
        },
        content: true
      },
      'o' : {
        // closed circle
        action: () => {
          this.points = this.parentGrid.pointMap['o'];
          var retVal = false;
          if( this.content != 'x' ) {
            this.clearDecorator();
            this.decorator = makeSVG('g', { id: this.id + '_decorator' });
            var outerCircle = makeSVG('circle', {
            id: this.id + '_decorator_outer', cx: (this.c * 19) + 10, cy: (this.r * 19) + 10,
              r: 8, fill: 'black', 'stroke-width': 0
            });
            $(this.decorator).append(outerCircle);
            $(this.element).append(this.decorator);
            retVal = true;
          }
          return retVal;
        },
        content: true
      },
      'clock' : {
        // plus sign 
        action: () => {
          this.points = this.parentGrid.pointMap['clock'];
          var retVal = false;
          if( this.content != 'clock' ) {
            this.clearDecorator();
            var cx = (this.c * 19);
            var cy = (this.r * 19);
            this.decorator = makeSVG('g', { id: this.id + '_decorator' });
            $(this.decorator).append(this.makeIconSVG(cx,cy,'clock',this.id+'_decorator_icon'));
            $(this.element).append(this.decorator);
            retVal = true;
          }
          return retVal;
        },
        content: true
      },
      'heart' : {
        // plus sign 
        action: () => {
          this.points = this.parentGrid.pointMap['heart'];
          var retVal = false;
          if( this.content != 'heart' ) {
            this.clearDecorator();
            var cx = (this.c * 19);
            var cy = (this.r * 19);
            this.decorator = makeSVG('g', { id: this.id + '_decorator' });
            $(this.decorator).append(this.makeIconSVG(cx,cy,'heart',this.id+'_decorator_icon'));
            $(this.element).append(this.decorator);
            retVal = true;
          }
          return retVal;
        },
        content: true
      },
      'padlock' : {
        // plus sign 
        action: () => {
          this.points = this.parentGrid.pointMap['padlock'];
          var retVal = false;
          if( this.content != 'padlock' ) {
            this.clearDecorator();
            var cx = (this.c * 19);
            var cy = (this.r * 19);
            this.decorator = makeSVG('g', { id: this.id + '_decorator' });
            $(this.decorator).append(this.makeIconSVG(cx,cy,'padlock',this.id+'_decorator_icon'));
            $(this.element).append(this.decorator);
            retVal = true;
          }
          return retVal;
        },
        content: true
      },
      'bell' : {
        // plus sign 
        action: () => {
          this.points = this.parentGrid.pointMap['bell'];
          var retVal = false;
          if( this.content != 'bell' ) {
            this.clearDecorator();
            var cx = (this.c * 19);
            var cy = (this.r * 19);
            this.decorator = makeSVG('g', { id: this.id + '_decorator' });
            $(this.decorator).append(this.makeIconSVG(cx,cy,'bell',this.id+'_decorator_icon'));
            $(this.element).append(this.decorator);
            retVal = true;
          }
          return retVal;
        },
        content: true
      }
    };
  }
  makeIconSVG( x, y, name, id ) {
    return makeSVG('image', {
      id: id, x: ''+(x+5.5), y: ''+(y+5.5), width: ''+9, height: ''+9, 
        href: 'images/icons-'+name+'.svg'
      });
  }
  clearDecorator() {
    if (this.decorator != null) {
      $(this.decorator).remove();
      this.decorator = null;
    }
  }
  updateContent( val ) {
    var inc = 0;
    if( val == 'clear' ) {
      if( this.content != 'clear' ) {
        inc = -1;
      }
    } else {
      // val is o or x
      if( this.content == 'clear' ) {
        inc = 1;
      } else if( this.content != val ) {
        inc = 0;
      }
    }
    this.content = val;
    this.parentGrid.updateFillCount(inc);
  }
  doAction(action) {
    var retVal = false;
    var actionObj = this.actions[ action ];
    if( actionObj && (!this.locked || actionObj.lockResistant) ) {
      if( actionObj.action() ) {
        if( actionObj.content ) this.updateContent(action);
        retVal = true;
      }
    }
    return retVal;
  }
  getHistoryObject() {
    return {
      content: this.content,
      locked: this.locked,
      id: this.id
    }
  }
  applyHistory( hist ) {
    var retVal = false;
    if( this.id === hist.id ) {
      if( this.content !== hist.content ) {
        this.doAction(hist.content);
      }
      this.locked = hist.locked;
      retVal = true;
    }
    return retVal;
  }
  getElement() {
    this.element = makeSVG("g", { id: this.id });
    // draw the outer box
    this.outer = makeSVG("rect", {
    id: this.id + '_outer', x: (this.c * 19), y: (this.r * 19), width: 20, height: 20,
      fill: cellBorderColor, "stroke-width": 0
    });
    this.inner = makeSVG("rect", {
    id: this.id + '_inner', x: (this.c * 19 + 1), y: (this.r * 19 + 1), width: 18, height: 18,
      fill: cellBackgroundColor, "stroke-width": 0
    });

    this.highlightInner = makeSVG("rect", {
    id: this.id + '_hlinner', x: (this.c * 19 + 1), y: (this.r * 19 + 1), width: 18, height: 18, rx: 9,
      fill: "transparent", "stroke-width": 0
    });

    this.highlightInnerLeft = makeSVG("rect", {
      id: this.id + '_hlleft', x: (this.c * 19 - 1), y: (this.r * 19 + 1), width: 11, height: 18,
      fill: "transparent", "stroke-width": 0
    });

    this.highlightInnerRight = makeSVG("rect", {
      id: this.id + '_hlright', x: (this.c * 19 + 10), y: (this.r * 19 + 1), width: 11, height: 18,
      fill: "transparent", "stroke-width": 0
    });

    this.highlightInnerUp = makeSVG("rect", {
      id: this.id + '_hlup', x: (this.c * 19 + 1), y: (this.r * 19 - 1), width: 18, height: 11,
      fill: "transparent", "stroke-width": 0
    });

    this.highlightInnerDown = makeSVG("rect", {
      id: this.id + '_hldown', x: (this.c * 19 + 1), y: (this.r * 19 + 10), width: 18, height: 11,
      fill: "transparent", "stroke-width": 0
    });

    this.optimalHighlightInner = makeSVG("rect", {
    id: this.id + '_ohlinner', x: (this.c * 19 + 3.5), y: (this.r * 19 + 3.5), width: 13, height: 13, rx: 6.5,
      fill: "transparent", "stroke-width": 0
    });

    this.optimalHighlightInnerLeft = makeSVG("rect", {
      id: this.id + '_ohlleft', x: (this.c * 19 - 1.5), y: (this.r * 19 + 3.5), width: 11.5, height: 13,
      fill: "transparent", "stroke-width": 0
    });

    this.optimalHighlightInnerRight = makeSVG("rect", {
      id: this.id + '_ohlright', x: (this.c * 19 + 10), y: (this.r * 19 + 3.5), width: 11.5, height: 13,
      fill: "transparent", "stroke-width": 0
    });

    this.optimalHighlightInnerUp = makeSVG("rect", {
      id: this.id + '_ohlup', x: (this.c * 19 + 3.5), y: (this.r * 19 - 1.5), width: 13, height: 11.5,
      fill: "transparent", "stroke-width": 0
    });

    this.optimalHighlightInnerDown = makeSVG("rect", {
      id: this.id + '_ohldown', x: (this.c * 19 + 3.5), y: (this.r * 19 + 10), width: 13, height: 11.5,
      fill: "transparent", "stroke-width": 0
    });

    $(this.element).append(this.outer);
    $(this.element).append(this.inner);
    $(this.element).append(this.highlightInner);
    $(this.element).append(this.highlightInnerLeft);
    $(this.element).append(this.highlightInnerRight);
    $(this.element).append(this.highlightInnerUp);
    $(this.element).append(this.highlightInnerDown);
    $(this.element).append(this.optimalHighlightInner);
    $(this.element).append(this.optimalHighlightInnerLeft);
    $(this.element).append(this.optimalHighlightInnerRight);
    $(this.element).append(this.optimalHighlightInnerUp);
    $(this.element).append(this.optimalHighlightInnerDown);

    return this.element;
  }
  getRelationship( from ) {
    var retVal = ERR;
    // we are "to" - where is "from"?  UP, RIGHT, DOWN, or LEFT?
    if( from ) {
      if( from.r == this.r && from.c == this.c ) {
        // it's the same one...
        retVal = ERR;
      } else if( from.r == this.r ) {
        // right or left
        if( from.c == (this.c - 1) ) {
          retVal = LEFT;
        } else if( from.c == (this.c + 1) ) {
          retVal = RIGHT;
        }
      } else if( from.c == this.c ) {
        // up or down
        if( from.r == (this.r - 1) ) {
          retVal = UP;
        } else if( from.r == (this.r + 1) ) {
          retVal = DOWN;
        }
      }
    }

    return retVal;
  }
  newHighlight(from) {
    
    $('#' + this.id + '_hlinner').attr("fill", highlightColor);
    if( this.isEnd || this.isStart ) {
      $('#' + this.id + '_hlinner').attr("rx",'0');
    }
    var dirFrom = this.getRelationship(from);
    switch( dirFrom ) {
      case UP:
        $('#' + from.id + '_hldown').attr("fill", highlightColor);
        $('#' + this.id + '_hlup').attr("fill", highlightColor);
        break;
      case RIGHT:
        $('#' + from.id + '_hlleft').attr("fill", highlightColor);
        $('#' + this.id + '_hlright').attr("fill", highlightColor);
        break;
      case DOWN:
        $('#' + from.id + '_hlup').attr("fill", highlightColor);
        $('#' + this.id + '_hldown').attr("fill", highlightColor);
        break;
      case LEFT:
        $('#' + from.id + '_hlright').attr("fill", highlightColor);
        $('#' + this.id + '_hlleft').attr("fill", highlightColor);
        break;
      default:
        break;
    }
  }
  newUnhighlight(to) {
    $('#' + this.id + '_hlinner').attr("fill", "transparent");
    var dirFrom = this.getRelationship(to);
    switch( dirFrom ) {
      case UP:
        $('#' + to.id + '_hldown').attr("fill", "transparent");
        $('#' + this.id + '_hlup').attr("fill", "transparent");
        break;
      case RIGHT:
        $('#' + to.id + '_hlleft').attr("fill", "transparent");
        $('#' + this.id + '_hlright').attr("fill", "transparent");
        break;
      case DOWN:
        $('#' + to.id + '_hlup').attr("fill", "transparent");
        $('#' + this.id + '_hldown').attr("fill", "transparent");
        break;
      case LEFT:
        $('#' + to.id + '_hlright').attr("fill", "transparent");
        $('#' + this.id + '_hlleft').attr("fill", "transparent");
        break;
      default:
        break;
    }
  }
  newOptimalPathHighlight(from) {
    $('#' + this.id + '_ohlinner').attr("fill", optimalHighlightColor);
    if( this.isEnd || this.isStart ) {
      $('#' + this.id + '_ohlinner').attr("rx",'0');
    }
    var dirFrom = this.getRelationship(from);
    switch( dirFrom ) {
      case UP:
        $('#' + from.id + '_ohldown').attr("fill", optimalHighlightColor);
        $('#' + this.id + '_ohlup').attr("fill", optimalHighlightColor);
        break;
      case RIGHT:
        $('#' + from.id + '_ohlleft').attr("fill", optimalHighlightColor);
        $('#' + this.id + '_ohlright').attr("fill", optimalHighlightColor);
        break;
      case DOWN:
        $('#' + from.id + '_ohlup').attr("fill", optimalHighlightColor);
        $('#' + this.id + '_ohldown').attr("fill", optimalHighlightColor);
        break;
      case LEFT:
        $('#' + from.id + '_ohlright').attr("fill", optimalHighlightColor);
        $('#' + this.id + '_ohlleft').attr("fill", optimalHighlightColor);
        break;
      default:
        break;
    }
  }
  highlight() {
    $('#' + this.id + '_inner').attr("fill", highlightColor);
  }
  unhighlight() {
    $('#' + this.id + "_inner").attr("fill", cellBackgroundColor);
  }
  optimalPathHighlight() {
    if( $('#' + this.id + "_inner").attr("fill") === "cyan" ) {
      $('#' + this.id + '_inner').attr("fill", "purple");
    } else {
      $('#' + this.id + '_inner').attr("fill", "red");
    }
  }
}

function makeSVG(tag, attrs) {
  var el= document.createElementNS('http://www.w3.org/2000/svg', tag);
  for (var k in attrs)
    el.setAttribute(k, attrs[k]);
  return el;
}

