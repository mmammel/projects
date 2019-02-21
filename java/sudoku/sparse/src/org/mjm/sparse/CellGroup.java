package org.mjm.sparse;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public abstract class CellGroup {
  protected int id;
  private EnumSet<Value> values = EnumSet.noneOf(Value.class);
  private List<Cell> cells = new ArrayList<Cell>();
  
  protected void addCell( Cell c ) {
    this.cells.add(c);
  }
  
  protected void addValue( Value v ) {
    this.values.add(v);
  }
  
  protected void removeValue( Value v ) {
    this.values.remove(v);
  }
  
  protected boolean hasValue( Value v ) {
    return this.values.contains(v);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Cell getCell( int idx ) {
    return this.cells.get(idx);
  }
  
  public List<Cell> getCells() {
    return cells;
  }

  public void setCells(List<Cell> cells) {
    this.cells = cells;
  }
}
