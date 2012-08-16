package org.mjm.sudoku.exclusions.rule;

import org.mjm.sudoku.*;

import java.util.List;
import java.util.EnumMap;

/**
  Rule 2:
    If a cell is the only cell in a row, box, or column to have a particular
    possible value, then that must be the value of the cell.
*/
public class SetLonePossibleValueRule implements Rule
{
  public boolean runRule( Board board )
  {
    boolean changesMade = false, changeTracker = false;
    
    for( Row row : board.getRows() )
    {
      changeTracker = this.runRuleInner(row);

      if( changeTracker ) {
        System.out.println( "Set lone rule success on row " + row.getId() + ":\n" + board );
      }

      changesMade = changesMade | changeTracker;
    }

    //if( changesMade ) continue;

    for( Column column : board.getColumns() )
    {
      changeTracker = this.runRuleInner(column);

      if( changeTracker ) {
         System.out.println( "Set lone rule success on column " + column.getId() + ":\n" + board );
      }
      changesMade = changesMade | changeTracker;
    }

    //if( changesMade ) continue;


    for( Box box : board.getBoxes() )
    {
      changeTracker = this.runRuleInner(box);

      if( changeTracker ) {
        System.out.println( "Set lone rule success on box " + box.getId() + ":\n" + board );
      }
      changesMade = changesMade | changeTracker;
    }
    
    return changesMade;
  }
  
  private boolean runRuleInner( CellCollection cells )
  {
    boolean retVal = false;
    Cell tempCell;
    List<Cell> tempList;
    EnumMap<Value, List<Cell>> valMap = cells.getValueMap();

    for( Value val : valMap.keySet() )
    {
      tempList = valMap.get(val);
      if( tempList.size() == 1 )
      {
        tempCell = tempList.get(0);
        retVal = retVal | tempCell.setValue(val);
      }
    }

    return retVal;
  }
}
