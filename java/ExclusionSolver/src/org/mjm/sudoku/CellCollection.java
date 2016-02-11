package org.mjm.sudoku;

import java.util.EnumMap;
import java.util.Set;
import java.util.SortedSet;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Comparator;

public abstract class CellCollection
{
  protected Cell [] mCells;

  public abstract int getId();

  public CellCollection( int [] indices, Cell [] board )
  {
    this.mCells = new Cell [9];
    for( int i = 0; i < 9; i++ )
    {
      this.mCells[i] = board[indices[i]];
    }
  }

  public boolean exclude( Value val )
  {
    boolean retVal = false;
    Cell tempCell;

    for( int i = 0; i < 9; i++ )
    {
      tempCell = this.mCells[i];
      retVal = retVal | tempCell.exclude(val);
    }

    return retVal;
  }

  public boolean exclude( Value val, Cell allbut )
  {
    boolean retVal = false;
    Cell tempCell;

    for( int i = 0; i < 9; i++ )
    {
      tempCell = this.mCells[i];
      if( !tempCell.equals(allbut) )
      {
        retVal = retVal | tempCell.exclude(val);
      }
    }

    return retVal;
  }

  public boolean exclude( Value [] vals, Cell allbut )
  {
    boolean retVal = false;
    Cell tempCell;

    for( int i = 0; i < 9; i++ )
    {
      tempCell = this.mCells[i];
      if( !tempCell.equals(allbut) )
      {
        retVal = retVal | tempCell.exclude(vals);
      }
    }

    return retVal;
  }

  public boolean exclude( Value val, Cell [] allbuts )
  {
    boolean retVal = false;
    Cell tempCell;
    Set<Cell> exceptionSet = this.arrayToSet( allbuts );

    for( int i = 0; i < 9; i++ )
    {
      tempCell = this.mCells[i];
      if( !exceptionSet.contains(tempCell) )
      {
        retVal = retVal | tempCell.exclude( val );
      }
    }

    return retVal;
  }

  public boolean exclude( Value [] vals, Cell [] allbuts )
  {
    boolean retVal = false;
    Cell tempCell;
    Set<Cell> exceptionSet = this.arrayToSet( allbuts );

    for( int i = 0; i < 9; i++ )
    {
      tempCell = this.mCells[i];
      if( !exceptionSet.contains(tempCell) )
      {
        retVal = retVal | tempCell.exclude( vals );
      }
    }

    return retVal;
  }

  protected Set<Cell> arrayToSet( Cell [] array )
  {
    Set<Cell> retVal = new HashSet<Cell>();

    for( int i = 0; i < array.length; i++ )
    {
      retVal.add( array[i] );
    }

    return retVal;
  }

  public EnumMap<Value,List<Cell>> getValueMap()
  {
    Cell tempCell;
    Value tempVal;
    List<Cell> tempList;
    EnumMap<Value,List<Cell>> retVal = new EnumMap<Value,List<Cell>>(Value.class);

    for( int i = 0; i < 9; i++ )
    {
      tempCell = this.mCells[i];
      for( Iterator<Value> iter = tempCell.getPossibleVals().iterator(); iter.hasNext(); )
      {
        tempVal = iter.next();
        if( (tempList = retVal.get(tempVal)) == null )
        {
          tempList = new ArrayList<Cell>();
          retVal.put( tempVal, tempList );
        }
        tempList.add(tempCell);
      }
    }

    return retVal;
  }

  public Cell [] getCells()
  {
    return this.mCells;
  }
  
  public Cell [] getExclusionCountOrderedCells()
  {
    return this.getOrderedCells( new CellExclusionCountComparator() );
  }
  
  public Cell [] getPossibleValCountOrderedCells()
  {
    Cell [] retVal = this.getOrderedCells( new CellPossibleValCountComparator() );
    System.out.println( "Got possible val count ordered cells: " + arrayToString(retVal));
    return retVal;
  }
  
  private Cell [] getOrderedCells( Comparator<Cell> c )
  {
    SortedSet<Cell> tempSet = new TreeSet<Cell>(c);
    
    for( int i = 0; i < this.mCells.length; i++ )
    {
      System.out.println( "Adding cell with " + this.mCells[i].getPossibleValCount() + " possible Vals." );
      tempSet.add( this.mCells[i] );
    }
    
    for( Cell cell : tempSet )
    {
      System.out.print("[" + cell.getPossibleValCount() + "]");
    }
    
    System.out.println("");
    
    return tempSet.toArray( new Cell[9] );
  }

  public static String arrayToString( Cell [] array ) {
    StringBuilder sb = new StringBuilder();
    for( Cell cell : array ) {
      sb.append("[").append(cell.getPossibleValCount()).append("]");
    }
    
    return sb.toString();
  }
  
}
