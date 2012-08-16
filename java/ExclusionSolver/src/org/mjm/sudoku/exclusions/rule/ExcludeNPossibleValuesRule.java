package org.mjm.sudoku.exclusions.rule;

import org.mjm.sudoku.*;

import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

/**
  Exclude N Rule:
    If N cells in a Row, Column, or Box share the exact same N possible Values,
    those values can be excluded in the other cells in the row, column, or box.
*/
public class ExcludeNPossibleValuesRule implements Rule
{
  public boolean runRule( Board board )
  {
    boolean changesMade = false, changeTracker = false;
    
    for( Row row : board.getRows() )
    {
      changeTracker = this.runRuleInner(row);
      if( changeTracker ) {
        System.out.println( "Exclude N Rule success on row " + row.getId() + ":\n" + board );
      }

      changesMade = changesMade | changeTracker;
    }

    //if( changesMade ) continue;

    for( Column column : board.getColumns() )
    {
      changeTracker = this.runRuleInner(column);
      if( changeTracker )  {
        System.out.println( "Exclude N Rule success on column " + column.getId() + ":\n" + board );
      }
      changesMade = changesMade | changeTracker;
    }

    //if( changesMade ) continue;

    for( Box box : board.getBoxes() )
    {
      changeTracker = this.runRuleInner(box);
      if( changeTracker )  {
        System.out.println( "Exclude N Rule success on box " + box.getId() + ":\n" + board );
      }
      changesMade = changesMade | changeTracker;
    }
    
    return changesMade;
  }
  
  private boolean runRuleInner( CellCollection cells )
  {
    boolean retVal = false;
    Set<Cell> tempSet;
    String tempStr;
    Cell tempCell;
    Cell [] tempArray;
    Map<String,Set<Cell>> cellMap = new HashMap<String,Set<Cell>>();

    for( Cell cell : cells.getCells() )
    {
      tempStr = cell.getIncludeString();
      if( (tempSet = cellMap.get(tempStr)) == null )
      {
        tempSet = new HashSet<Cell>();
      }

      tempSet.add( cell );
      cellMap.put( tempStr, tempSet );
    }

    for( String key : cellMap.keySet() )
    {
      tempArray = cellMap.get(key).toArray(new Cell[0]);
      if( tempArray.length > 1 && tempArray.length == tempArray[0].getPossibleVals().size() )
      {
        retVal = retVal | cells.exclude( tempArray[0].getPossibleVals().toArray(new Value[0]), tempArray );
      }
    }

    return retVal;
  }
}
