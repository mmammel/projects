import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Row {
  private int id;
  private int length = 0;
  private Cell [] cells;
  public Row( int id, int len ) {
    this.id = id;
    this.length = len;
    this.cells = new Cell [ len ];
    for( int i = 0; i < len; i++ ) {
      this.cells[i] = new Cell(this.id * 100 + i, 0,i);
    }
  }
  
  static Row [] generateAllRows( int len ) {
    int count = 1 << len;
    Row [] retVal = new Row [ count ];
    Cell c = null;
    int shift = 0;
    int bitcheck = 1;
    for( int i = 0; i < count; i++ ) {
      shift = 0;
      
      retVal[i] = new Row( i, len );
      while( shift < len ) {
        bitcheck = 1 << shift;
        if( (bitcheck & i) > 0 ) {
          retVal[i].cells[shift].setValue(CellVal.BLACK);
        } else {
          retVal[i].cells[shift].setValue(CellVal.WHITE);          
        }
        shift++;
      }
    }
    
    return retVal;
  }
  
  /*
   *  If these two rows can be neighbors (a on top, b on bottom)
   *  return a descriptor that also indicates if they can be paired 
   *  at the top or bottom of the board without trapping cells. 
   */
  static RowNeighborDescriptor canBeNeighbors( Row a, Row b ) {
    RowNeighborDescriptor retVal = null;
    boolean ok = true;
    
    System.out.println(" Checking: \n" + a + " and \n" + b + "..." );
    
    if( a != null && b != null && a.getLength() == b.getLength() ) {
      
      // first check to see if we will create any solid 2x2s
      for( int i = 0; i < a.getLength() - 1; i++ ) {
        if( a.getCells()[i].value() == a.getCells()[i+1].value() &&
            b.getCells()[i].value() == a.getCells()[i].value() &&
            b.getCells()[i].value() == b.getCells()[i+1].value() ) {
              ok = false;
              break;
        }
      }
      
      if( ok ) {
        retVal = new RowNeighborDescriptor( b.getId() );
        retVal.setCeilingNeighbor(isEdgeSafe(a,b));
        retVal.setFloorNeighbor(isEdgeSafe(b,a));
      }      
    }
    
    System.out.println( "...Result: " + (retVal != null ? "Yes! " + retVal  : "No!"));
    
    return retVal;
  }
  
  private static boolean isEdgeSafe( Row a, Row b ) {
    // now check to make sure that b wouldn't "trap" any cells.  
    CellVal currColor = CellVal.BLANK;
    boolean currEscapeStatus = false;
    int i = 0;
    while(i < a.getLength() ) {     
      if( currColor != CellVal.BLANK &&  currColor != a.cells[i].value() ) {
        if( !currEscapeStatus ) {
          // Fail!  We switched colors without finding the previous one an escape
          // ok = false;
          break;
        } else {
          currColor = a.cells[i].value();
        }
      } else {
        currColor = a.cells[i].value();
      }
      
      if( b.cells[i].value() == currColor ) {
        currEscapeStatus = true; // this color can escape, jump forward to the next color.
        // ok = true;
        while( i < a.getLength() && a.cells[i].value() == currColor ) i++;
      } else {
        currEscapeStatus = false;
        i++;
      }
    }
    
    return currEscapeStatus;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder("+");
    for( int i = 0; i < this.length; i++ ) {
      sb.append("---+");
    }
    sb.append("\n|");
    for( int i = 0; i < this.length; i++ ) {
      sb.append(" ").append(this.cells[i].value().toString()).append(" |");
    }
    sb.append("\n+");
    for( int i = 0; i < this.length; i++ ) {
      sb.append("---+");
    }
    
    return sb.toString();
  }
  
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public Cell[] getCells() {
    return cells;
  }

  public void setCells(Cell[] cells) {
    this.cells = cells;
  }
  
  public static void main( String [] args ) {
    Row a = new Row(0, 4);
    Row b = new Row(1, 4);
    
    a.cells[0].setValue(CellVal.WHITE);
    a.cells[1].setValue(CellVal.BLACK);
    a.cells[2].setValue(CellVal.WHITE);
    a.cells[3].setValue(CellVal.BLACK);
    
    b.cells[0].setValue(CellVal.WHITE);
    b.cells[1].setValue(CellVal.BLACK);
    b.cells[2].setValue(CellVal.BLACK);
    b.cells[3].setValue(CellVal.BLACK);
    
    RowNeighborDescriptor n = null;
    
    if( (n = Row.canBeNeighbors(a, b)) != null ) {
      System.out.println(n);
    } else {
      System.out.println("No!");
    }
    
  }
}
