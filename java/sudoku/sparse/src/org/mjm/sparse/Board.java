package org.mjm.sparse;

import org.mjm.sparse.Cell;
import org.mjm.sparse.Row;

public class Board {
  
  private static final int [][] ROW_INDICES = {
      {0,1,2,3,4,5,6,7,8},
      {9,10,11,12,13,14,15,16,17 },
      {18,19,20,21,22,23,24,25,26 },
      {27,28,29,30,31,32,33,34,35 },
      {36,37,38,39,40,41,42,43,44 },
      {45,46,47,48,49,50,51,52,53 },
      {54,55,56,57,58,59,60,61,62 },
      {63,64,65,66,67,68,69,70,71 },
      {72,73,74,75,76,77,78,79,80 }
  };
  
  private static final int [][] COLUMN_INDICES = {
      {0,9,18,27,36,45,54,63,72},
      {1,10,19,28,37,46,55,64,73},
      {2,11,20,29,38,47,56,65,74},
      {3,12,21,30,39,48,57,66,75},
      {4,13,22,31,40,49,58,67,76},
      {5,14,23,32,41,50,59,68,77},
      {6,15,24,33,42,51,60,69,78},
      {7,16,25,34,43,52,61,70,79},
      {8,17,26,35,44,53,62,71,80}
  };
  
  private static final int [][] BOX_INDICES = {
      {0,1,2,9,10,11,18,19,20},  
      {3,4,5,12,13,14,21,22,23},  
      {6,7,8,15,16,17,24,25,26},  
      {27,28,29,36,37,38,45,46,47},  
      {30,31,32,39,40,41,48,49,50},  
      {33,34,35,42,43,44,51,52,53},  
      {54,55,56,63,64,65,72,73,74},  
      {57,58,59,66,67,68,75,76,77},  
      {60,61,62,69,70,71,78,79,80}  
  };
  
  private Cell [] cells = new Cell [81];
  private Row [] rows = new Row [9];
  private Column [] columns = new Column [9];
  private Box [] boxes = new Box [9];
  private int numCellsSet = 0;
  
  public Board() {
    for( int i = 0; i < 81; i++ ) {
      this.cells[i] = new Cell(i);
    }
    
    Cell [] tempCells = new Cell[9];
    int [] ridxs = null,cidxs = null, bidxs = null;
    
    for( int i = 0; i < 9; i++ ) {
      ridxs = ROW_INDICES[i];
      cidxs = COLUMN_INDICES[i];
      bidxs = BOX_INDICES[i];
      
      for( int j = 0; j < ridxs.length; j++ ) {
        tempCells[j] = this.cells[ridxs[j]];
      }
      
      this.rows[i] = new Row(i, tempCells);

      for( int j = 0; j < cidxs.length; j++ ) {
        tempCells[j] = this.cells[cidxs[j]];
      }
      
      this.columns[i] = new Column(i, tempCells);
      
      for( int j = 0; j < bidxs.length; j++ ) {
        tempCells[j] = this.cells[bidxs[j]];
      }
      
      this.boxes[i] = new Box(i, tempCells);
    }
  }
  
  /**
   * "safely" set the cell value at index cellIdx, and update the state of the board.
   * @param cellIdx
   * @param val
   * @return
   */
  public boolean setCellValue( int cellIdx, Value val ) {
    boolean retVal = false;
    SetResult result = null;
    if( cellIdx >= 0 && cellIdx < 81 && val != null ) {
      Cell c = this.cells[cellIdx];
      result = c.setCellValue(val);
      // System.out.println( "[ " + cellIdx + " -> " + val + " : " + result + " ]");
      retVal = this.successfulSet(result);
      this.numCellsSet += this.numSetChange(result);
    }
    
    return retVal;    
  }
  
  private boolean successfulSet( SetResult status ) {
    return status == SetResult.ADD || status == SetResult.SWAP || status == SetResult.REMOVE;
  }
  
  private int numSetChange( SetResult status ) {
    int retVal = 0;
    
    switch( status ) {
    case ADD:
      retVal = 1;
      break;
    case REMOVE:
      retVal = -1;
      break;
    default:
      retVal = 0;
      break;
    }
    
    return retVal;
  }
  
  public int getNumCellsSet() {
    return numCellsSet;
  }

  public void setNumCellsSet(int numCellsSet) {
    this.numCellsSet = numCellsSet;
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    Row tempRow;
    Cell tempCell;

    sb.append(" +-------+-------+-------+\n");

    for( int i = 0; i < 9; i++ )
    {
      tempRow = this.rows[i];
      for( int j = 0; j < 9; j++ )
      {
        tempCell = tempRow.getCell(j);
        sb.append( ((j%3 == 0) ? " | " : " ") + (tempCell.getVal() == Value.EMPTY ? " " : tempCell.getVal().ordinal() ) );
      }

      sb.append( " |\n" );
      if( i%3 == 2 ) sb.append(" +-------+-------+-------+\n");
    }

    return sb.toString();
  }
}
