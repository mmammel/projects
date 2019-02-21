package org.mjm.sparse;

public class Row extends CellGroup {
  public Row(int id, Cell ...cells ) {
    this.setId(id);
    for( Cell c : cells ) {
      this.addCell(c);
      c.setRow(this);
    }
  }
}
