import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
  0   1   2   3   4   5   6
  +---+---+---+---+---+---+---+
0 | c | c | c | c | c | c | c |
  +---v---v---v---v---v---v---+
1 | c | c | c | c | c | c | c |
  +---v---v---v---v---v---v---+
2 | c | c | c | c | c | c | c |
  +---v---v---v---v---v---v---+
3 | c | c | c | c | c | c | c |
  +---v---v---v---v---v---v---+
4 | c | c | c | c | c | c | c |
  +---v---v---v---v---v---v---+
5 | c | c | c | c | c | c | c |
  +---v---v---v---v---v---v---+
6 | c | c | c | c | c | c | c |
  +---+---+---+---+---+---+---+

c = Cell
v = Vertex

cell -> vertices:

(0,0) -> [ -, -, (0,0), - ]
(0,1) -> [ -, -, (0,1), (0,0) ]
(0,2) -> [ -, -, (0,2), (0,1) ]
...
(1,0) -> [ -, (0,0), (1,0), - ]
(1,1) -> [ (0,0), (0,1), (1,1), (1,0) ]
...
(5,4) -> [ (4,3), (4,4), (5,4), (5,3) ]
...
(x,y) -> [ (x-1,y-1), (x-1,y), (x,y), (x,y-1) ]

vertex -> cells

(0,0) -> [ (0,0), (0,1), (1,1), (1,0) ]
...
(x,y) -> [ (x,y), (x,y+1), (x+1,y+1), (x+1,y) ]     


    
    +---+---+---+                                +---+---+---+
    | A - B - C |                                | A - B - C |
    +-|-+-|-+-|-+                                +-|-+---+-|-+
    | D - E - F |                                | D | E | F |
    +-|-+-|-+-|-+                                +-|-+-|-+-|-+
    | G - H - I |                                | G | H | I |
    +---+---+---+                                +---+---+---+

  
   +---+                                        +---+
A  |   |                                     A  |   |     
   +---+---+                                    +---+---+
B  | x |   |                                 B  | x |   |     
   +---+---+---+                                +---+---+---+
C  |   | x |   |                             C  |   | x |   |       
   +---+---+---+---+                            +---+---+---+---+
D  | x |   |   |   |                         D  | x |   |   |   |     
   +---+---+---+---+---+                        +---+---+---+---+---+
E  |   | x |   | x |   |                     E  |   |   |   |   |   |   
   +---+---+---+---+---+---+                    +---+---+---+---+---+---+
F  |   |   | x |   | x |   |                 F  |   |   | x |   |   |   |    
   +---+---+---+---+---+---+---+                +---+---+---+---+---+---+---+
G  |   |   |   | x |   |   |   |             G  |   |   |   | x |   |   |   |     
   +---+---+---+---+---+---+---+---+            +---+---+---+---+---+---+---+---+
H  |   |   |   |   | x |   | x |   |         H  |   |   |   |   | x |   |   |   |    
   +---+---+---+---+---+---+---+---+---+        +---+---+---+---+---+---+---+---+---+
I  |   |   |   |   |   | x |   | x |   |     I  |   |   |   |   |   | x |   |   |   |       
   +---+---+---+---+---+---+---+---+---+        +---+---+---+---+---+---+---+---+---+
     A   B   C   D   E   F   G   H   I            A   B   C   D   E   F   G   H   I

*/
public class Board {
  private int rows;
  private int cols;
  
  private Map<CellVal,Integer> valCounts;

  private Cell [][] cellGrid;
  private Vertex [][] vertices;
  
  public Board( int rows, int cols ) {
    this.rows = rows;
    this.cols = cols;
    
    this.valCounts = new HashMap<CellVal,Integer>();
        
    this.cellGrid = new Cell [this.rows][];
    this.vertices = new Vertex[ this.rows - 1][];
    
    for( int r = 0; r < this.rows; r++ ) {
      this.cellGrid[r] = new Cell [ this.cols ];
      
      if( r < this.rows - 1 ) {
        this.vertices[r] = new Vertex [ this.cols - 1 ];
      }
      
      for( int c = 0; c < this.cols; c++ ) {
        if( r < this.rows - 1 && c < this.cols - 1 ) {
          this.vertices[r][c] = new Vertex();
        }
        
        this.cellGrid[r][c] = new Cell(r*this.cols+c, r, c);        
      }  
    }
    
    // the board is created, now create all of the relations.
    Cell cell = null;
    Vertex vertex = null;
    for( int r = 0; r < this.rows; r++ ) {
      for( int c = 0; c < this.cols; c++ ) {
        // cells -> vertices
        // (x,y) -> [ (x-1,y-1), (x-1,y), (x,y), (x,y-1) ]
        cell = this.cellGrid[r][c];
        if( r == 0 && c == 0 ) {
          cell.setBottomRightVertex(this.vertices[r][c]);
          cell.setRightCell(this.cellGrid[r][c+1]);
          cell.setDownCell(this.cellGrid[r+1][c]);
        } else if( r == 0 && c == this.cols - 1 ) { 
          cell.setBottomLeftVertex( this.vertices[r][c - 1]);
          cell.setLeftCell(this.cellGrid[r][c-1]);
          cell.setDownCell(this.cellGrid[r+1][c]);
        } else if( r == this.rows - 1 && c == this.cols - 1 ) {
          cell.setTopLeftVertex(this.vertices[r - 1][c - 1]);
          cell.setUpCell(this.cellGrid[r-1][c]);
          cell.setLeftCell(this.cellGrid[r][c-1]);
        } else if( r == this.rows - 1 && c == 0 ) {
          cell.setTopRightVertex(this.vertices[r - 1][c]);
          cell.setUpCell(this.cellGrid[r-1][c]);
          cell.setRightCell(this.cellGrid[r][c+1]);
        } else if( r == 0 ) {
          cell.setBottomRightVertex(this.vertices[r][c]);
          cell.setBottomLeftVertex(this.vertices[r][c - 1]);
          cell.setLeftCell(this.cellGrid[r][c-1]);
          cell.setRightCell(this.cellGrid[r][c+1]);
          cell.setDownCell(this.cellGrid[r+1][c]);
        } else if( r == this.rows - 1 ) {
          cell.setTopLeftVertex(this.vertices[r - 1][c - 1]);
          cell.setTopRightVertex(this.vertices[r - 1][c]);
          cell.setLeftCell(this.cellGrid[r][c-1]);
          cell.setRightCell(this.cellGrid[r][c+1]);
          cell.setUpCell(this.cellGrid[r-1][c]);
        } else if( c == 0 ) {
          cell.setTopRightVertex(this.vertices[r - 1][c]);
          cell.setBottomRightVertex(this.vertices[r][c]);
          cell.setUpCell(this.cellGrid[r-1][c]);
          cell.setRightCell(this.cellGrid[r][c+1]);
          cell.setDownCell(this.cellGrid[r+1][c]);
       } else if( c == this.cols - 1 ) {
          cell.setTopLeftVertex(this.vertices[r - 1][c - 1]);
          cell.setBottomLeftVertex(this.vertices[r][c - 1]);
          cell.setUpCell(this.cellGrid[r-1][c]);
          cell.setDownCell(this.cellGrid[r+1][c]);
          cell.setLeftCell(this.cellGrid[r][c-1]);
        } else {
          cell.setTopLeftVertex(this.vertices[r - 1][c - 1]);
          cell.setTopRightVertex(this.vertices[r - 1][c]);
          cell.setBottomRightVertex(this.vertices[r][c]);
          cell.setBottomLeftVertex(this.vertices[r][c - 1]);
          cell.setUpCell(this.cellGrid[r-1][c]);
          cell.setDownCell(this.cellGrid[r+1][c]);
          cell.setLeftCell(this.cellGrid[r][c-1]);
          cell.setRightCell(this.cellGrid[r][c+1]);
        }
        
        // vertex -> cells
        // (x,y) -> [ (x,y), (x,y+1), (x+1,y+1), (x+1,y) ]    
        if( r < this.rows - 1 && c < this.cols - 1 ) {
          vertex = this.vertices[r][c];
          vertex.setTopLeft(this.cellGrid[r][c]);
          vertex.setTopRight( this.cellGrid[r][c+1]);
          vertex.setBottomRight( this.cellGrid[r+1][c+1]);
          vertex.setBottomLeft(this.cellGrid[r+1][c]);
        }
        
      }      
    }
  }
  
  private int [] getNextCursorPos( int r, int c ) {
    int [] retVal = new int [2];
    retVal[0] = r;
    retVal[1] = c;
    if( c < this.cols - 1 ) {
      retVal[1]++;
    } else if( r < this.rows - 1 ) {
      retVal[0]++;
      retVal[1] = 0;
    } else {
      retVal = null;
    }
    
    return retVal;
  }
  
  /**
   * Set all of the cells in this row to the same given value.
   * @param value
   * @param idx
   */
  public void setRowValues( CellVal value, int idx ) {
    if( value != null && idx >= 0 && idx < this.rows ) {
      for( int c = 0; c < this.cols; c++ ) {
        this.cellGrid[idx][c].setValue(value);
      }
    }    
  }
  
  /**
   * Set all the row values to match the given Row
   * @param r
   * @param idx
   */
  public void setRowValues( Row r, int idx ) {
    if( r != null && r.getLength() == this.cols && idx >= 0 && idx < this.rows ) {
      for( int c = 0; c < r.getLength(); c++ ) {
        this.cellGrid[idx][c].setValue(r.getCells()[c].value());
      }
    }
  }
  
  public boolean valid() {
    boolean retVal = true;
    // check vertices for 4 colors.
    for( int r = 0; r < this.rows - 1; r++ ) {
      for( int c = 0; c < this.cols - 1; c++ ) {
        if(!(retVal = this.vertices[r][c].valid()) ) {
          return retVal;
        }
      }
    }
    
    int colors = this.regionColoringCheck(CellVal.BLACK, CellVal.WHITE, CellVal.BLANK );
    
    if( colors > 2 ) {
      retVal = false;
    }
    
    return retVal;
  }
  
  /**
   * Color the regions by "reachability" and make sure there are only two.
   * 
   * The first cellval parameter will be treated as the first kind of cell to color
   * The following cellvals will be combined.  Since we do the path finding in black, we will typically pass:
   *   black, white, blank
   * so the white and blank cells will be combined.
   * 
   * @return
   */
  private int regionColoringCheck(CellVal ...cellVals ) {
    //System.out.println("Checking coloring for: \n" + this );
    int retVal = 0;
    EnumSet<CellVal> primary = null;
    Set<Integer> tempColorSet = null;
    Set<Integer> seen = new HashSet<Integer>();
    Map<Integer,Set<Integer>> colorSets = new HashMap<Integer,Set<Integer>>();
    EnumSet<CellVal> secondary = null;
    
    if(cellVals != null && cellVals.length >= 1 ) {
      // we have some cellVals.
      primary = EnumSet.of(cellVals[0]);
      if( cellVals.length > 1 ) {
        secondary = EnumSet.noneOf(CellVal.class);
        for( int i = 1; i < cellVals.length; i++ ) {
          secondary.add(cellVals[i]);
        }
      }
      
      // let's check the primary.
      Cell start = this.findFirst(primary);
      if( start != null ) {
        tempColorSet = new HashSet<Integer>();
        this.colorInner(start, tempColorSet, primary);
        colorSets.put( ++retVal, tempColorSet );
        seen.addAll( tempColorSet );
        while((start = this.findFirstNotIn(primary, seen)) != null ) {
          tempColorSet = new HashSet<Integer>();
          this.colorInner(start, tempColorSet, primary );
          colorSets.put( ++retVal, tempColorSet );
          seen.addAll( tempColorSet );
        }
      }
      
      // now check secondary
      // let's check the primary.
      start = this.findFirst(secondary);
      if( start != null ) {
        tempColorSet = new HashSet<Integer>();
        this.colorInner(start, tempColorSet, secondary);
        colorSets.put( ++retVal, tempColorSet );
        seen.addAll( tempColorSet );
        while((start = this.findFirstNotIn(secondary, seen)) != null ) {
          tempColorSet = new HashSet<Integer>();
          this.colorInner(start, tempColorSet, secondary );
          colorSets.put( ++retVal, tempColorSet );
          seen.addAll( tempColorSet );
        }
      }      
      
    }    
    
    return retVal;
  }
    
  // cell is a cell with a value in vals.  Search its neighbors for others.
  private void colorInner( Cell cell, Set<Integer> colorSet, EnumSet<CellVal> vals ) {
    Cell tempNeighbor = null;
    colorSet.add(cell.getId());
    
    if( (tempNeighbor = cell.getLeftCell()) != null && vals.contains(tempNeighbor.value()) && !colorSet.contains(tempNeighbor.getId()) ) {
      this.colorInner( tempNeighbor, colorSet, vals);
    }
    
    if( (tempNeighbor = cell.getUpCell()) != null && vals.contains(tempNeighbor.value()) && !colorSet.contains(tempNeighbor.getId()) ) {
      this.colorInner( tempNeighbor, colorSet, vals);
    }
    
    if( (tempNeighbor = cell.getRightCell()) != null && vals.contains(tempNeighbor.value()) && !colorSet.contains(tempNeighbor.getId()) ) {
      this.colorInner( tempNeighbor, colorSet, vals);
    }
    
    if( (tempNeighbor = cell.getDownCell()) != null && vals.contains(tempNeighbor.value()) && !colorSet.contains(tempNeighbor.getId()) ) {
      this.colorInner( tempNeighbor, colorSet, vals);
    }
  }
  
  /**
   * Assumes we have an incomplete board - make sure that all colored cells have a route to a blank cell.
   * @return
   */
  public boolean checkEscapability() {
    boolean retVal = true;
    Cell start = null;
    Set<Integer> seen = new HashSet<Integer>();
        
    for( CellVal color : EnumSet.of(CellVal.BLACK, CellVal.WHITE ) ) {
      while( (start = this.findFirstNotIn(EnumSet.of(color), seen)) != null ) {
        retVal &= this.checkEscapabilityInner(start, seen);
      }
    }
    
    return retVal;
  }
  
  private boolean checkEscapabilityInner( Cell cell, Set<Integer> seen ) {
    boolean retVal = false;
    seen.add(cell.getId());
    
    for( Cell c : cell.getAdjacentCells() ) {
      if( c != null && !seen.contains(c.getId() )) {
        if( c.value() == CellVal.BLANK ) {
          retVal = true;
        } else if( c.value() == cell.value() ) {
          retVal |= this.checkEscapabilityInner(c, seen);
        }
      }
    }
    
    return retVal;
  }
  
  /**
   * Find the first cell from (0,0) with val "val", that does NOT have an ID in the ids set.
   * If ids is null or empty, just find the first one.
   * @param val
   * @param ids
   * @return
   */
  private Cell findFirstNotIn( EnumSet<CellVal> vals, Set<Integer> ids ) {
    Cell retVal = null;
    
    for( int r = 0; r < this.rows; r++ ) {
      for( int c = 0; c < this.cols; c++ ) {
        if( (ids == null || !ids.contains((r*this.cols+c))) && vals.contains(this.cellGrid[r][c].value()) ) {
          return this.cellGrid[r][c];
        }
      }
    }
    
    return retVal;
  }
  
  public String toString( boolean bad ) {
    String b = this.toString();
    if( bad ) {
      b = b.replaceAll("\\+", "X");
    }
    
    return b;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    
    for( int r = 0; r < this.rows; r++ ) {
      this.printRow(sb, r);
    }
    
    return sb.toString();
  }
  
  private void printRow(StringBuilder sb, int row ) {
    if( row == 0 ) {
      for( int c = 0; c < this.cols; c++ ) {
        sb.append("+---");
      }
      sb.append("+\n");
    }
    for( int c = 0; c < this.cols; c++ ) {
      sb.append("| ").append(this.cellGrid[row][c].value().toString()).append(" ");
    }
    sb.append("|\n");
    
    for( int c = 0; c < this.cols; c++ ) {
      sb.append("+---");
    }
    sb.append("+\n");
  }
  
  private Cell findFirst( EnumSet<CellVal> vals ) {
    return this.findFirstNotIn(vals, null);
  }
  
  private Cell findFirst( CellVal val ) {
    return this.findFirstNotIn(EnumSet.of(val), null);
  }
  
  public int getRows() {
    return rows;
  }
  public void setRows(int rows) {
    this.rows = rows;
  }
  public int getCols() {
    return cols;
  }
  public void setCols(int cols) {
    this.cols = cols;
  }
  public Map<CellVal, Integer> getValCounts() {
    return valCounts;
  }
  public void setValCounts(Map<CellVal, Integer> valCounts) {
    this.valCounts = valCounts;
  }
  public Cell[][] getCellGrid() {
    return cellGrid;
  }
  public void setCellGrid(Cell[][] cellGrid) {
    this.cellGrid = cellGrid;
  }
  public Vertex[][] getVertices() {
    return vertices;
  }
  public void setVertices(Vertex[][] vertices) {
    this.vertices = vertices;
  }
  
  
}
