package org.mjm.sudoku.exclusions.rule;

import org.mjm.sudoku.Board;
import org.mjm.sudoku.Cell;
import org.mjm.sudoku.Value;

/**
  Rule 1:
    If a cell has only 1 possible value, that value can be
    excluded from every other cell in the cell's row, column, and box.
*/
public class SimpleExcludeRule implements Rule
{
  public boolean runRule( Board board )
  {
    boolean changesMade = false, changeTracker = false;
    
    for( Cell cell : board.getCells() )
    {
      changeTracker = this.runRuleInner( cell, board );
      if( changeTracker )
      {
        System.out.println( "Simple Exclude Rule success on cell " + cell.getId() + ":\n" + board );
      }

      changesMade = changesMade | changeTracker;
    }
    
    return changesMade;
  }
  
  private boolean runRuleInner( Cell cell, Board board )
  {
    Value tempVal;
    boolean retVal = false;
    boolean result = false;

    if( cell.getPossibleVals().size() == 1 )
    {
      result = board.getRowForCell(cell).exclude(cell.getPossibleVals().toArray(new Value[0])[0],cell);
      retVal = retVal | result;
      result = board.getColumnForCell(cell).exclude(cell.getPossibleVals().toArray(new Value[0])[0],cell);
      retVal = retVal | result;
      result = board.getBoxForCell(cell).exclude(cell.getPossibleVals().toArray(new Value[0])[0],cell);
      retVal = retVal | result;
    }

    return retVal;
  }
}
