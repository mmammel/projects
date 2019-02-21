package org.mjm.sparse;

public class Box extends CellGroup {
  public Box(int id, Cell ...cells ) {
    this.setId(id);
    for( Cell c : cells ) {
      this.addCell(c);
      c.setBox(this);
    }
  }
}
