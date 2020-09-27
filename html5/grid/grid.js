
  var LEFT = 0;
  var UP = 1;
  var RIGHT = 2;
  var DOWN = 3;

  var MOVERS = [ [0, -1], [-1, 0], [0, 1], [1, 0] ];

class Grid {
  constructor(containerId, rows, cols) {
    this.containerId = containerId;
    this.model = [];
    this.index = {};
    this.history = [];
    this.historyIdx = -1;
    this.rows = rows;
    this.cols = cols;
    this.currPos = [0, 0];
    this.viewBox = [0, 0, 19 * this.cols + 1, 19 * this.rows + 1];
    this.buildGrid();
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
        tempGridCell = new GridCell(r,c);
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
    this.model[0][0].highlight();
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
  handleClick(e) {
    var id = $(e.target).attr('id').replace(/^([0-9]+?_[0-9]+?)_.*$/, "$1");
    var cell = this.index[id];
    if( cell ) {
      this.moveCursorTo(cell.r,cell.c);
    }
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
    } else if (e.which == 88) {
      this.doCellAction('x');
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
    $('#'+this.containerId).click( function(e) {
      that.handleClick(e);
    })
  }
}

class GridCell {
  constructor(r, c) {
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
      'o' : {
        // open circle
        action: () => {
          var retVal = false;
          if( this.content != 'o' ) {
            this.clearDecorator();
            // open circle
            this.decorator = makeSVG('g', { id: this.id + '_decorator' });
            var outerCircle = makeSVG('circle', {
            id: this.id + '_decorator_outer', cx: (this.c * 19) + 10, cy: (this.r * 19) + 10,
              r: 8, fill: 'black', 'stroke-width': 0
            });
            var innerCircle = makeSVG('circle', {
            id: this.id + '_decorator_inner', cx: (this.c * 19) + 10, cy: (this.r * 19) + 10,
              r: 7, fill: 'white', 'stroke-width': 0
            });
            $(this.decorator).append(outerCircle);
            $(this.decorator).append(innerCircle);
            $(this.element).append(this.decorator);
            retVal = true;
          }
          return retVal;
        },
        content: true
      },
      'x' : {
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
  doAction(action) {
    var retVal = false;
    var actionObj = this.actions[ action ];
    if( actionObj && (!this.locked || actionObj.lockResistant) ) {
      if( actionObj.action() ) {
        if( actionObj.content ) this.content = action;
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

