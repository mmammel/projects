package org.mjm.sudoku;

public class Row extends CellSpan
{
  public Row( int [] indices, Cell [] board )
  {
    super(indices,board);
  }

  public int getId()
  {
    return this.mCells[0].getId() / 9;
  }
}
