package org.mjm.sparse;

public class Cell {
  
  private int id;
  private Value val;
  private Row row;
  private Column col;
  private Box box;
  
  public Cell( int id ) {
    this.id = id;
    this.val = Value.EMPTY;
  }
  
  public SetResult setCellValue( Value val ) {
    SetResult retVal = SetResult.ERROR;
    Value currVal = this.val;
    
    if( val != null ) {
      // we have a value.
      if( val == Value.EMPTY ) {
        // we are just clearing the cell
        if( currVal != Value.EMPTY ) {
          this.row.removeValue(currVal);
          this.col.removeValue(currVal);
          this.box.removeValue(currVal);
          retVal = SetResult.REMOVE;
          this.val = val;
        } else {
          retVal = SetResult.NOCHANGE;
        }
      } else if( this.val == val ) {
        // we already have the value, just return no change.
        retVal = SetResult.NOCHANGE;
      } else {
        // see if we can set this value.
        if( !this.row.hasValue(val) &&
            !this.col.hasValue(val) &&
            !this.box.hasValue(val) ) {
          // all good. remove the current value, then set the new one.
          this.row.removeValue(currVal);
          this.col.removeValue(currVal);
          this.box.removeValue(currVal);
          this.val = val;
          this.row.addValue(val);
          this.col.addValue(val);
          this.box.addValue(val);
          retVal = currVal == Value.EMPTY ? SetResult.ADD : SetResult.SWAP;
        } else {
          retVal = SetResult.BLOCK;
        }
      }
    }
    
    return retVal;  
  }
  
  public int getId() {
    return id;
  }
  
  public void setId(int id) {
    this.id = id;
  }
  
  public Value getVal() {
    return val;
  }
  
  public void setVal(Value val) {
    this.val = val;
  }
  
  public Row getRow() {
    return row;
  }
  
  public void setRow(Row row) {
    this.row = row;
  }
  
  public Column getCol() {
    return col;
  }
  
  public void setCol(Column col) {
    this.col = col;
  }
  
  public Box getBox() {
    return box;
  }
  
  public void setBox(Box box) {
    this.box = box;
  }
}
