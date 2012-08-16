import java.util.List;
import java.util.EnumMap;

/**
   Rule Four:
     Given some value, if the only cells in a row or column that can have that value
     are contained within an intersecting box, the value can be excluded from the
     non-intersecting cells of the box.
 */
public class IntersectingBoxRule implements Rule
{
  public boolean runRule( Board board )
  {
    boolean changesMade = false, changeTracker = false;
    
    for( Row row : board.getRows() )
    {
      changeTracker = this.runRuleInner(row, board);
      if( changeTracker ) {
        System.out.println( "Intersecting Box rule success on row " + row.getId() + ":\n" + board );
      }

      changesMade = changesMade | changeTracker;
    }

    //if( changesMade ) continue;

    for( Column column : board.getColumns() )
    {
      changeTracker = this.runRuleInner(column, board);
      if( changeTracker )  {
        System.out.println( "Intersecting Box rule success on column " + column.getId() + ":\n" + board );
      }
      changesMade = changesMade | changeTracker;
    }
    
    return changesMade;
  }
  
  private boolean runRuleInner( CellSpan cells, Board board )
  {
    boolean retVal = false;
    Cell tempCell;
    Box tempBox;
    List<Cell> tempList;
    EnumMap<Value, List<Cell>> valMap = cells.getValueMap();

    for( Value val : valMap.keySet() )
    {
      tempList = valMap.get(val);
      if( tempList.size() == 2 || tempList.size() == 3 )
      {
        if( (tempBox = board.getBoxForCells(tempList)) != null )
        {
          retVal = retVal | tempBox.exclude(val,tempList.toArray(new Cell[0]));
        }
      }
    }

    return retVal;
  }
}