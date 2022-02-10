
  var LEFT = 0;
  var UP = 1;
  var RIGHT = 2;
  var DOWN = 3;

  var MOVERS = [ [0, -1], [-1, 0], [0, 1], [1, 0] ];

class Grid {
  constructor(containerId, rows, cols) {
    this.containerId = containerId;
    this.fillCount = 0;
    this.model = [];
    this.index = {};
    this.history = [];
    this.historyIdx = -1;
    this.rows = rows;
    this.cols = cols;
    this.currPos = [0, 0];
    this.viewBox = [0, 0, 19 * this.cols + 1, 19 * this.rows + 1];
    this.touchContext = {
      currId: "",
      dragging: false,
      dragValue: 'clear',
      timestamp: 0
    };
    this.buildGrid();
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
  draw() {
    // build the root element
    this.svgRoot = makeSVG("svg", { id: 'svgRoot', viewBox: '' + this.viewBox[0] + ' ' + this.viewBox[1] + ' ' + this.viewBox[2] + ' ' + this.viewBox[3] });
    // make the gridCells
    for (var i = 0; i < this.rows; i++) {
      for (var j = 0; j < this.cols; j++) {
        $(this.svgRoot).append(this.model[i][j].getElement());
      }
    }
    $('#' + this.containerId).append(this.svgRoot);

    // randomly fill the grid with characters, except for the A and B
    var symbols = [ '+', 'p', 'o', 't' ];
    for( var i = 0; i < this.rows; i++ ) {
      for( var j = 0; j < this.cols; j++ ) {
        // get random action
        var rand = Math.floor(Math.random() * 4);
        this.model[i][j].doAction(symbols[rand]);
      }
    }

    this.model[0][0].doAction('b');
    this.model[this.rows - 1][this.cols - 1].doAction('a');
    this.model[this.rows - 1][this.cols - 1].highlight();
    this.currPos = [this.rows - 1,this.cols - 1];

    this.setupHandlers();
  }
  incrementCursor(dir) {
    var newX = this.currPos[0];
    var newY = this.currPos[1];
    var mover = MOVERS[dir];
    newX += mover[0];
    newY += mover[1];
    if (newX >= this.rows) {
      newX = 0;
    }
    else if (newX < 0) {
      newX = this.rows - 1;
    }
    else if (newY >= this.cols) {
      newY = 0;
    }
    else if (newY < 0) {
      newY = this.cols - 1;
    }
    this.moveCursorTo(newX,newY);
  }
  moveCursorTo( newX, newY ) {
    var from = this.model[ this.currPos[0] ][ this.currPos[1] ];
    var to = this.model[ newX ][ newY ];
    
    if( from && to ) {
      from.unhighlight();
      to.highlight();
      this.currPos[0] = newX;
      this.currPos[1] = newY;
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
    this.touchContext.dragValue = cell.content;
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
    this.touchContext.dragValue = cell.content;
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
          this.doCellAction(this.touchContext.dragValue);
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
      if( id != this.touchContext.currId ) {
        var cell = this.index[id];
        if( cell ) {
          this.touchContext.currId = id;
          this.moveCursorTo(cell.r,cell.c);
          this.doCellAction(this.touchContext.dragValue);
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
        if( cell.content === 'clear' ) {
          this.doCellAction('o');
        } else if( cell.content === 'o' ) {
          this.doCellAction('x');
        } else if( cell.content === 'x' ) {
          this.doCellAction('clear');
        }
      } else {
        this.moveCursorTo(cell.r,cell.c);
      }
    }
  }
  getIdFromTarget(target) {
    return target.attr('id').replace(/^([0-9]+?_[0-9]+?)_.*$/, "$1");
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
    }else if (e.which == 66) {
      this.doCellAction('b');
    }else if (e.which == 80) {
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

class GridCell {
  constructor(grid, r, c) {
    this.parentGrid = grid;
    this.r = r;
    this.c = c;
    this.id = '' + r + '_' + c;
    this.locked = false;
    this.content = 'clear';
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
          if( this.content != 'a' ) {
            this.clearDecorator();
            this.decorator = makeSVG('g', { id: this.id + '_decorator' });
            var theLetter = makeSVG('text', {
            id: this.id + '_decorator_inner', x: (this.c * 19) + 10, y: (this.r * 19) + 16,
              'text-anchor': 'middle', textLength: 20, 'font-family' : 'arial', 'font-weight' : 'bold',
              fill: 'black', 'stroke-width': 0
            });
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
          var retVal = false;
          if( this.content != 'b' ) {
            this.clearDecorator();
            this.decorator = makeSVG('g', { id: this.id + '_decorator' });
            var theLetter = makeSVG('text', {
            id: this.id + '_decorator_inner', x: (this.c * 19) + 10, y: (this.r * 19) + 16,
              'text-anchor': 'middle', textLength: 20, 'font-family' : 'arial', 'font-weight' : 'bold',
              fill: 'black', 'stroke-width': 0
            });
            theLetter.innerHTML = 'B';
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
      }
    };
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
      fill: "black", "stroke-width": 0
    });
    this.inner = makeSVG("rect", {
    id: this.id + '_inner', x: (this.c * 19) + 1, y: (this.r * 19) + 1, width: 18, height: 18,
      fill: "white", "stroke-width": 0
    });
    $(this.element).append(this.outer);
    $(this.element).append(this.inner);
    return this.element;
  }
  highlight() {
    $('#' + this.id + '_inner').attr("fill", "lightblue");
  }
  unhighlight() {
    $('#' + this.id + "_inner").attr("fill", "white");
  }
}

  function makeSVG(tag, attrs) {
    var el= document.createElementNS('http://www.w3.org/2000/svg', tag);
    for (var k in attrs)
      el.setAttribute(k, attrs[k]);
    return el;
  }

