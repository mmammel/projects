package org.mjm.sudoku.exclusions.rule;

import org.mjm.sudoku.Board;

public interface Rule
{
  public boolean runRule( Board board );
}
