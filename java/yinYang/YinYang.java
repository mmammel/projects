import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class YinYang {
  private Board board;
  
  private List<EnumSet<Direction>> branches;
  private Row [] allPossibleRows;
  private Map<Integer,List<RowNeighborDescriptor>> rowRelations;
  private Map<Integer,Row> possibleRowIndex;
  private int solutionCount = 0;
  
  // build a board of blanks.
  public YinYang( int rows, int cols ) {
    this.branches = this.buildBranches();
    this.board = new Board(rows, cols);
    
    this.allPossibleRows = Row.generateAllRows(cols);
    Row currRow;
    Row checkRow;
    List<RowNeighborDescriptor> tempList = null;
    RowNeighborDescriptor neighborDescriptor = null;
    this.rowRelations = new HashMap<Integer,List<RowNeighborDescriptor>>();
    
    for( int i = 0; i < (1 << this.board.getCols()); i++ ) {
      currRow = this.allPossibleRows[i];
      tempList = new ArrayList<RowNeighborDescriptor>();
      for( int j = 0; j < (1 << this.board.getCols()); j++ ) {
        checkRow = this.allPossibleRows[j];
        if( (neighborDescriptor = Row.canBeNeighbors(currRow, checkRow)) != null ) {
          tempList.add(neighborDescriptor);
        }
      }
      this.rowRelations.put(currRow.getId(), tempList);
    }
    
    List<RowNeighborDescriptor> allRowDescriptors = new ArrayList<RowNeighborDescriptor>();
    
    for( Row r : this.allPossibleRows ) {
      allRowDescriptors.add( new RowNeighborDescriptor( r.getId() ));
    }
    
    // the -1 id is the id of the top of the board, i.e. the first row can 
    // be any of the possible rows.
    this.rowRelations.put(-1, allRowDescriptors);
    
    // build an index of rows based on id.
    this.possibleRowIndex = new HashMap<Integer,Row>();
    for( Row r : this.allPossibleRows ) {
      possibleRowIndex.put( r.getId(), r);
    }    
  }
  
  private List<EnumSet<Direction>> buildBranches() {
    List<EnumSet<Direction>> retVal = new ArrayList<EnumSet<Direction>>();
    
    retVal.add(EnumSet.of(Direction.LEFT));
    retVal.add(EnumSet.of(Direction.UP));
    retVal.add(EnumSet.of(Direction.RIGHT));
    retVal.add(EnumSet.of(Direction.DOWN));
    retVal.add(EnumSet.of(Direction.LEFT,Direction.UP));
    retVal.add(EnumSet.of(Direction.LEFT,Direction.RIGHT));
    retVal.add(EnumSet.of(Direction.LEFT,Direction.DOWN));
    retVal.add(EnumSet.of(Direction.UP,Direction.RIGHT));
    retVal.add(EnumSet.of(Direction.UP,Direction.DOWN));
    retVal.add(EnumSet.of(Direction.RIGHT,Direction.DOWN));
    retVal.add(EnumSet.of(Direction.LEFT,Direction.UP,Direction.RIGHT));
    retVal.add(EnumSet.of(Direction.UP,Direction.RIGHT,Direction.DOWN));
    retVal.add(EnumSet.of(Direction.LEFT,Direction.RIGHT,Direction.DOWN));
    retVal.add(EnumSet.of(Direction.LEFT,Direction.UP,Direction.DOWN));
    retVal.add(EnumSet.allOf(Direction.class));
    
    return retVal;
  }
  
  private List<EnumSet<Direction>> getSubBranches( EnumSet<Direction> dirs ) {
    List<EnumSet<Direction>> retVal = new ArrayList<EnumSet<Direction>>();
    for( EnumSet<Direction> branch : this.branches ) {
      if( dirs.containsAll(branch) ) {
        retVal.add(branch);
      }
    }
    
    return retVal;
  }
  
  public void findRowSolutions() {
    this.solutionCount = 0;
    this.findRowSolutionsInner(-1, 0);
    System.out.println( "Found " + this.solutionCount + " solutions");
  }
  
  public void findRowSolutionsInner( int prevRowId, int rowIdx ) {
    if( rowIdx == this.board.getRows() ) {
      if( this.board.valid() ) {
        System.out.println( this.board );
        this.solutionCount++;
      } else {
        // System.out.println( this.board.toString(true) );
      }
      return;
    } else {
      if( rowIdx > 2 && rowIdx < this.board.getRows() ) {
        if( !this.board.checkEscapability() ) {
          // bail out, we're bogus!
          return;
        }
      }
      List<RowNeighborDescriptor> rowsToTry = this.rowRelations.get(prevRowId);
      Row r = null;
      for( RowNeighborDescriptor rnd : rowsToTry ) {
        if( rowIdx == 1 && !rnd.isCeilingNeighbor() ) {
          continue;
        } else if( rowIdx == this.board.getRows() - 1 && !rnd.isFloorNeighbor() ) {
          continue;
        }
        
        r = this.possibleRowIndex.get( rnd.getId() );
        this.board.setRowValues(r, rowIdx);
        this.findRowSolutionsInner(rnd.getId(), rowIdx + 1);
      }
      this.board.setRowValues(CellVal.BLANK, rowIdx);
    }
  }
  
  public void findSolutions() {
    Set<String> solutions = new HashSet<String>();
    this.findSolutionsInner( this.board.getCellGrid()[0][0], solutions );
    System.out.println( "Found " + solutions.size() + " solutions");
  }
  
  private void findSolutionsInner( Cell cell ) { 
    System.out.println(this.board);
    cell.setValue(CellVal.BLACK);
    if( !cell.valid() ) {
      cell.setValue(CellVal.BLANK);
      return;
    } else if( this.board.valid() ){
      System.out.println(this.board);
    }
    
    for( Cell c : cell.getAdjacentCells() ) {
      if( c != null && c.value() == CellVal.BLANK ) {
        this.findSolutionsInner( c );
      }
    }
    
    cell.setValue(CellVal.BLANK);
  }
  
  /*
   * Use sets of branch directions
   */
  private void findSolutionsInner( Cell cell, Set<String> solutions ) {
    cell.setValue(CellVal.BLACK);
    
    if( !cell.valid() ) {
      cell.setValue(CellVal.BLANK);
      return;
    } else if( this.board.valid() ) {
      String solId = this.getSolutionID();
      if( !solutions.contains(solId ) ) {
        solutions.add(solId);
        System.out.println( this.board );
      }
    }
    Cell c = null;
    
    EnumSet<Direction> validDirections = EnumSet.noneOf(Direction.class);
    EnumSet<Direction> openDirs = cell.getOpenDirections();
    for( Direction d : openDirs ) {
      c = cell.getAdjacentCell(d);
      c.setValue(CellVal.BLACK);
      if( c.valid() ) {
        validDirections.add(d);
      }
      c.setValue(CellVal.BLANK);
    }
    
    List<EnumSet<Direction>> subBranches = this.getSubBranches(validDirections);
    
    for( EnumSet<Direction> branch : subBranches ) {
      for( Direction d : branch ) {
        c = cell.getAdjacentCell(d);
        c.setValue(CellVal.BLACK);
      }
      
      for( Direction d : branch ) {
        c = cell.getAdjacentCell(d);
        this.findSolutionsInner(c, solutions);
      }
      
      for( Direction d : branch ) {
        c = cell.getAdjacentCell(d);
        c.setValue(CellVal.BLANK);
      }
    }
    
    cell.setValue(CellVal.BLANK);
  }
  
  private String getSolutionID() {
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for( int r = 0; r < this.board.getRows(); r++ ) {
      for( int c = 0; c < this.board.getCols(); c++ ) {
        if( this.board.getCellGrid()[r][c].value() == CellVal.BLACK ) {
          if( first ) {
            first = false;
          } else {
            sb.append("-");
          }
          
          sb.append( this.board.getCellGrid()[r][c].getId() );
        }
      }
    }
    
    return sb.toString();
  }
  
  public YinYang( String fileName ) {
    // allow an instantiation from a file with filled in cells.
  }
  
  public static void main( String [] args ) {
    System.out.println( "+---+---+" );
    System.out.println( "| \u25cb | \u25cf |" );
    System.out.println( "+---+---+" );
    
    YinYang YY = new YinYang(7,7);
    YY.findRowSolutions();
  }
  
}
