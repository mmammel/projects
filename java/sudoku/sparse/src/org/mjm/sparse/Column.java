package org.mjm.sparse;

public class Column extends CellGroup {
  public Column(int id, Cell ...cells ) {
    this.setId(id);
    for( Cell c : cells ) {
      this.addCell(c);
      c.setCol(this);
    }
  }
}
