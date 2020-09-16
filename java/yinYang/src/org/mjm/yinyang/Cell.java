package org.mjm.yinyang;

import java.util.EnumSet;

/*
  +---+---+---+
  |   | 1 |   |
  +---0---1---+
  | 0 | C | 2 |
  +---3---2---+
  |   | 3 |   |
  +---+---+---+
*/
public class Cell {
  private int id; // unique id for this cell.
  private int row;
  private int col;
  private CellVal value;
  private Cell [] adjacentCells;  // [ LEFT, UP, RIGHT, DOWN ], null if at edge
  private Vertex [] adjacentVertices; // [ TOPLEFT, TOPRIGHT, BOTTOMRIGHT, BOTTOMLEFT ], null if on on edges
  
  public Cell(int id, int r, int c) {
    this.id = id;
    this.row = r;
    this.col = c;
    this.value = CellVal.BLANK;
    this.adjacentCells = new Cell [4];
    this.adjacentVertices = new Vertex [4];
  }
  
  public CellVal value() {
    return this.value;
  }
  
  public void setValue( CellVal val ) {
    this.value = val;
  }
  
  public boolean valid() {
    boolean retVal = false;
    for( int i = 0; i < this.adjacentCells.length; i++ ) {
      if(this.adjacentCells[i] != null && 
          (this.adjacentCells[i].value() == this.value || this.adjacentCells[i].value() == CellVal.BLANK) ) {
        retVal = true;
        break;
      }
    }
    
    if( retVal ) {
      retVal = this.verticesValid();
    }
    
    return retVal;
  }
  
  /**
   * get an enum set of all directions that are not null and blank
   * @return
   */
  public EnumSet<Direction> getOpenDirections() {
    EnumSet<Direction> retVal = EnumSet.noneOf(Direction.class);
    Cell c;
    for( Direction d : Direction.values() ) {
      c = this.adjacentCells[d.ordinal()];
      if( c != null && c.value() == CellVal.BLANK ) {
        retVal.add(d);
      }
    }
    
    return retVal;
  }
  
  public int getRow() {
    return this.row;
  }

  public void setRow(int r) {
    this.row = r;
  }

  public int getCol() {
    return this.col;
  }

  public void setCol(int c) {
    this.col = c;
  }

  private boolean verticesValid() {
    boolean retVal = true;
    for( int i = 0; i < this.adjacentVertices.length; i++ ) {
      if( this.adjacentVertices[i] != null && !this.adjacentVertices[i].valid() ) {
        retVal = false;
        break;
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
  
  public Cell getAdjacentCell( Direction dir ) {
    return this.adjacentCells[dir.ordinal() ];
  }
  
  public Cell [] getAdjacentCells( EnumSet<Direction> dirs ) {
    Cell [] retVal = null;
    if( dirs != null ) {
      retVal = new Cell[ dirs.size() ];
      int idx = 0;
      for( Direction d : dirs ) {
        retVal[idx++] = this.adjacentCells[ d.ordinal() ];
      }
    }
    
    return retVal;
  }
  
  public Cell [] getAdjacentCells() {
    return this.adjacentCells;
  }
  
  public Vertex [] getAdjacentVertices() {
    return this.adjacentVertices;
  }
  
  public void setLeftCell( Cell c ) {
    this.setAdjacentCell(c, 0);
  }
  public void setUpCell( Cell c ) {
    this.setAdjacentCell(c, 1);
  }
  public void setRightCell( Cell c ) {
    this.setAdjacentCell(c, 2);
  }
  public void setDownCell( Cell c ) {
    this.setAdjacentCell(c, 3);
  }
  public void setTopLeftVertex( Vertex v ) {
    this.setAdjacentVertex(v, 0);
  }
  public void setTopRightVertex( Vertex v ) {
    this.setAdjacentVertex(v, 1);
  }
  public void setBottomRightVertex( Vertex v ) {
    this.setAdjacentVertex(v, 2);
  }
  public void setBottomLeftVertex( Vertex v ) {
    this.setAdjacentVertex(v, 3);
  }
  
  public Cell getLeftCell() {
    return this.getAdjacentCell(0);
  }
  public Cell getUpCell() {
    return this.getAdjacentCell(1);
  }
  public Cell getRightCell() {
    return this.getAdjacentCell(2);
  }
  public Cell getDownCell() {
    return this.getAdjacentCell(3);
  }
  public Vertex getTopLeftVertex() {
    return this.getAdjacentVertex(0);
  }
  public Vertex getTopRightVertex() {
    return this.getAdjacentVertex(1);
  }
  public Vertex getBottomRightVertex() {
    return this.getAdjacentVertex(2);
  }
  public Vertex getBottomLeftVertex() {
    return this.getAdjacentVertex(3);
  }  
  
  private void setAdjacentVertex( Vertex v, int idx ) {
    this.adjacentVertices[idx] = v;
  }
  
  private void setAdjacentCell( Cell c, int idx ) {
    this.adjacentCells[idx] = c;
  }
  
  private Vertex getAdjacentVertex( int idx ) {
    return this.adjacentVertices[idx];
  }
  
  private Cell getAdjacentCell( int idx ) {
    return this.adjacentCells[idx];
  }
}
