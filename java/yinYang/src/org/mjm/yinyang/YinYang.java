package org.mjm.yinyang;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class YinYang {
  private Board board;
  
  //private List<EnumSet<Direction>> branches;
  private Row [] allPossibleRows;
  private Map<Integer,Set<Integer>> rowRestrictions = null; // for solving incomplete boards
  private Map<Integer,List<RowNeighborDescriptor>> rowRelations;
  private Map<Integer,Row> possibleRowIndex;
  private int solutionCount = 0;
  // As we find solutions, rotate
  private Set<Integer> rotationAvoids = new HashSet<Integer>();
  private Set<BitSet> solutions = new HashSet<BitSet>();
  
  // build a board of blanks.
  public YinYang( int rows, int cols ) {
    //this.branches = this.buildBranches();
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
    
    // only do the first half to avoid the color flips
//    for( Row r : this.allPossibleRows ) {
//      allRowDescriptors.add( new RowNeighborDescriptor( r.getId() ));
//    }
    
    for( int i = 0; i < this.allPossibleRows.length / 2; i++ ) {
      allRowDescriptors.add( new RowNeighborDescriptor( this.allPossibleRows[i].getId() ) );
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
  
//  private List<EnumSet<Direction>> buildBranches() {
//    List<EnumSet<Direction>> retVal = new ArrayList<EnumSet<Direction>>();
//    
//    retVal.add(EnumSet.of(Direction.LEFT));
//    retVal.add(EnumSet.of(Direction.UP));
//    retVal.add(EnumSet.of(Direction.RIGHT));
//    retVal.add(EnumSet.of(Direction.DOWN));
//    retVal.add(EnumSet.of(Direction.LEFT,Direction.UP));
//    retVal.add(EnumSet.of(Direction.LEFT,Direction.RIGHT));
//    retVal.add(EnumSet.of(Direction.LEFT,Direction.DOWN));
//    retVal.add(EnumSet.of(Direction.UP,Direction.RIGHT));
//    retVal.add(EnumSet.of(Direction.UP,Direction.DOWN));
//    retVal.add(EnumSet.of(Direction.RIGHT,Direction.DOWN));
//    retVal.add(EnumSet.of(Direction.LEFT,Direction.UP,Direction.RIGHT));
//    retVal.add(EnumSet.of(Direction.UP,Direction.RIGHT,Direction.DOWN));
//    retVal.add(EnumSet.of(Direction.LEFT,Direction.RIGHT,Direction.DOWN));
//    retVal.add(EnumSet.of(Direction.LEFT,Direction.UP,Direction.DOWN));
//    retVal.add(EnumSet.allOf(Direction.class));
//    
//    return retVal;
//  }
  
//  private List<EnumSet<Direction>> getSubBranches( EnumSet<Direction> dirs ) {
//    List<EnumSet<Direction>> retVal = new ArrayList<EnumSet<Direction>>();
//    for( EnumSet<Direction> branch : this.branches ) {
//      if( dirs.containsAll(branch) ) {
//        retVal.add(branch);
//      }
//    }
//    
//    return retVal;
//  }
  
  private void checkSolution() {
    BitSet bs = this.board.getBitRepresentation();
    if( !this.solutions.contains(bs) ) {
      this.solutionCount++;
      System.out.println(this.board);
      this.solutions.add(bs);
    }
  }
  
  public void findRowSolutions() {
    this.rotationAvoids = new HashSet<Integer>();
    this.solutions = new HashSet<BitSet>();
    this.solutionCount = 0;
    this.findRowSolutionsInner(-1, 0);
    System.out.println( "Found " + this.solutionCount + " solutions");
  }
  
  public void findRowSolutionsInner( int prevRowId, int rowIdx ) {
    
    if( rowIdx == this.board.getRows() ) {
      if( this.board.valid() ) {
        this.checkSolution();
        if( this.rowRestrictions == null ) {
          // we aren't solving, so generate everything with rotations/inversions.
          this.board.invert();
          this.checkSolution();
          // Now do rotations.
          this.board.rotate();
          this.checkSolution();
          this.rotationAvoids.add(this.board.getRowArray()[0].getId());
          if( this.board.isSquare() ) {
            this.board.rotate();
            this.checkSolution();
            this.rotationAvoids.add(this.board.getRowArray()[0].getId());
            this.board.rotate();
            this.checkSolution();
            this.rotationAvoids.add(this.board.getRowArray()[0].getId());
          } 
          this.board.rotate(); // back to original, but inverted.
          
          this.board.invert(); // back to original
          // Now do rotations.
          this.board.rotate();
          this.checkSolution();
          this.rotationAvoids.add(this.board.getRowArray()[0].getId());
          if( this.board.isSquare() ) {
            this.board.rotate();
            this.checkSolution();
            this.rotationAvoids.add(this.board.getRowArray()[0].getId());
            this.board.rotate();
            this.checkSolution();
            this.rotationAvoids.add(this.board.getRowArray()[0].getId());
          } 
          this.board.rotate();
        }
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
      Set<Integer> restrictions = null;
      for( RowNeighborDescriptor rnd : rowsToTry ) {
        if( prevRowId == -1 && this.rotationAvoids.contains(rnd.getId() ) ) {
          continue;
        }
        
        if( rowIdx == 1 && !rnd.isCeilingNeighbor() ) {
          continue;
        } else if( rowIdx == this.board.getRows() - 1 && !rnd.isFloorNeighbor() ) {
          continue;
        }
        
        if( this.rowRestrictions != null ) {
          restrictions = this.rowRestrictions.get(rowIdx);
          if( !restrictions.contains(rnd.getId())) {
            continue;
          }
        }
        
        r = this.possibleRowIndex.get( rnd.getId() );
        this.board.setRowValues(r, rowIdx);
        this.findRowSolutionsInner(rnd.getId(), rowIdx + 1);
      }
      this.board.setRowValues(CellVal.BLANK, rowIdx);
    }
  }
  
//  public void findSolutions() {
//    Set<String> solutions = new HashSet<String>();
//    this.findSolutionsInner( this.board.getCellGrid()[0][0], solutions );
//    System.out.println( "Found " + solutions.size() + " solutions");
//  }
  
//  private void findSolutionsInner( Cell cell ) { 
//    System.out.println(this.board);
//    cell.setValue(CellVal.BLACK);
//    if( !cell.valid() ) {
//      cell.setValue(CellVal.BLANK);
//      return;
//    } else if( this.board.valid() ){
//      System.out.println(this.board);
//    }
//    
//    for( Cell c : cell.getAdjacentCells() ) {
//      if( c != null && c.value() == CellVal.BLANK ) {
//        this.findSolutionsInner( c );
//      }
//    }
//    
//    cell.setValue(CellVal.BLANK);
//  }
  
  /*
   * Use sets of branch directions
   */
//  private void findSolutionsInner( Cell cell, Set<String> solutions ) {
//    cell.setValue(CellVal.BLACK);
//    
//    if( !cell.valid() ) {
//      cell.setValue(CellVal.BLANK);
//      return;
//    } else if( this.board.valid() ) {
//      String solId = this.getSolutionID();
//      if( !solutions.contains(solId ) ) {
//        solutions.add(solId);
//        System.out.println( this.board );
//      }
//    }
//    Cell c = null;
//    
//    EnumSet<Direction> validDirections = EnumSet.noneOf(Direction.class);
//    EnumSet<Direction> openDirs = cell.getOpenDirections();
//    for( Direction d : openDirs ) {
//      c = cell.getAdjacentCell(d);
//      c.setValue(CellVal.BLACK);
//      if( c.valid() ) {
//        validDirections.add(d);
//      }
//      c.setValue(CellVal.BLANK);
//    }
//    
//    List<EnumSet<Direction>> subBranches = this.getSubBranches(validDirections);
//    
//    for( EnumSet<Direction> branch : subBranches ) {
//      for( Direction d : branch ) {
//        c = cell.getAdjacentCell(d);
//        c.setValue(CellVal.BLACK);
//      }
//      
//      for( Direction d : branch ) {
//        c = cell.getAdjacentCell(d);
//        this.findSolutionsInner(c, solutions);
//      }
//      
//      for( Direction d : branch ) {
//        c = cell.getAdjacentCell(d);
//        c.setValue(CellVal.BLANK);
//      }
//    }
//    
//    cell.setValue(CellVal.BLANK);
//  }
  
//  private String getSolutionID() {
//    StringBuilder sb = new StringBuilder();
//    boolean first = true;
//    for( int r = 0; r < this.board.getRows(); r++ ) {
//      for( int c = 0; c < this.board.getCols(); c++ ) {
//        if( this.board.getCellGrid()[r][c].value() == CellVal.BLACK ) {
//          if( first ) {
//            first = false;
//          } else {
//            sb.append("-");
//          }
//          
//          sb.append( this.board.getCellGrid()[r][c].getId() );
//        }
//      }
//    }
//    
//    return sb.toString();
//  }
  
  /**
   * 
   * @param rows
   * @param cols
   * @param descriptor for N : 0 -> (rows*cols - 1), wN,bN,bN,...,wN
   */
  public YinYang( int rows, int cols, String descriptor ) {
    // allow an instantiation from a descriptor that fills some cells
    this(rows,cols);
    if( descriptor != null && descriptor.matches("^(?:[wb][0-9]+,?)+$") ) {
      this.rowRestrictions = new HashMap<Integer,Set<Integer>>();
      String [] cellDescriptors = descriptor.split(",");
      int idx = 0;
      for( String cellDesc : cellDescriptors ) {
        idx = Integer.parseInt( cellDesc.substring(1) );
        if( cellDesc.charAt(0) == 'w' ) {
          this.board.getCellById(idx).setValue( CellVal.WHITE );
        } else if( cellDesc.charAt(0) == 'b' ) {
          this.board.getCellById(idx).setValue( CellVal.BLACK );
        }
      }
      
      Row [] rowArray = this.board.getRowArray();
      
      for( int i = 0; i < this.board.getRows(); i++ ) {
        this.rowRestrictions.put(i, Row.grep(rowArray[i], this.allPossibleRows) );
      }
      List<RowNeighborDescriptor> allRowDescriptors = new ArrayList<RowNeighborDescriptor>();
      
      for( Row r : this.allPossibleRows ) {
        allRowDescriptors.add( new RowNeighborDescriptor( r.getId() ));
      }
      
      // the -1 id is the id of the top of the board, i.e. the first row can 
      // be any of the possible rows.
      this.rowRelations.put(-1, allRowDescriptors);
    }
  }
  
  public static void main( String [] args ) {
    int r = 4;
    int c = 7;
    int temp = 0;
    String descriptor = null;
    
    try {
      if( args.length == 1 ) {
        // it's square
        temp = Integer.parseInt( args[0] );
        r = temp;
        c = temp;
      } else if( args.length >= 2 ) {
        temp = Integer.parseInt(args[0]);
        r = temp;
        temp = Integer.parseInt(args[1]);
        c = temp;
        if( args.length == 3 ) {
          descriptor = args[2];
        }
      }
    } catch( Exception e ) {
      System.out.println("Error parsing dimensions, using defaults (" + r + "," + c + ")");
    }
    
    YinYang YY = descriptor == null ? new YinYang(r,c) : new YinYang(r,c,descriptor);
    YY.findRowSolutions();
  }
  
}
