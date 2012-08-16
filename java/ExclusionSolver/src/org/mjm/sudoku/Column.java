package org.mjm.sudoku;

public class Column extends CellSpan
{
  public Column( int [] indices, Cell [] board )
  {
    super(indices,board);
  }

  public int getId()
  {
    return this.mCells[0].getId();
  }
}
