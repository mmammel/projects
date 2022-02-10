/**
 *  There is a board with a starting space and an ending space.
 *  Each square on the board other than the start and end have a point value
 *  Get to the end from the start acumulating the fewest points possible
 */
public class OptimalPath {

  private Cell [][] board;
  private int [][] memo;
  private int rows = 0;
  private int cols = 0;
  private int [] startCoord = new int [2];
  private int [] endCoord = new int [2];

  private static final int UP = 0;
  private static final int RIGHT = 1;
  private static final int DOWN = 2;
  private static final int LEFT = 3;

  public static void main( String [] args ) {
    try {
      int r = Integer.parseInt(args[0]);
      int c = Integer.parseInt(args[1]);
      String d = args[2];

      OptimalPath OP = new OptimalPath( r, c, d );
      OP.find();
    } catch( Exception e ) {
      System.out.println( e.toString() );
      e.printStackTrace();
    }
  }


  /**
   * rows: number of rows > 2
   * cols: number of cols > 2
   * descriptor: a flat view of the board, comma separated, e.g. for 3x3:
   *   00,01,02,10,11,12,20,21,22
   * where each field is either an integer for points, "A" for start, or "B" for end, e.g.
   * B,1,4,3,3,2,4,1,A
   */
  public OptimalPath( int rows, int cols, String descriptor ) {
    String [] dsplit = descriptor.split(",");
    if( dsplit.length != rows * cols ) throw new RuntimeException( "Bogus input" );
    
    this.rows = rows;
    this.cols = cols;

    this.board = new Cell [ rows ][];
    for( int i = 0; i < this.board.length; i++ ) {
      this.board[i] = new Cell [ cols ];
      for( int j = 0; j < this.board[i].length; j++ ) {
        this.board[i][j] = new Cell(i, j);
      }
    }

    int r = 0, c = 0, tmpVal = 0;
    for( String d : dsplit ) {
      if( d.equals( "A" ) ) {
        this.board[r][c].points = 0;
        this.board[r][c].isStart = true;
        this.startCoord[0] = r;
        this.startCoord[1] = c;
      } else if( d.equals( "B" ) ) {
        this.board[r][c].points = 0;
        this.board[r][c].isEnd = true;
        this.endCoord[0] = r;
        this.endCoord[1] = c;
      } else {
        try {
          tmpVal = Integer.parseInt(d);
          this.board[r][c].points = tmpVal;
        } catch( Exception e ) {
          throw new RuntimeException( "Bogus point value: " + d );
        }
      }  

      c++;
      if( c >= cols ) {
        r++;
        c = 0;
      }
    }
  }

  /**
   * find the path.  use memoized recursion.
   */
  public void find() {
    this.findInner(0, this.startCoord[0], this.startCoord[1], null );
    
    System.out.println(this.toString());
    System.out.println( "Optimal points: " + this.board[this.endCoord[0]][this.endCoord[1]].memo);
    this.printSolution();
  }

  private void findInner( int total, int posR, int posC, Cell from ) {
    this.board[posR][posC].memo = total;
    total += this.board[posR][posC].points;
    if( from != null ) {
      this.board[posR][posC].prevR = from.row;
      this.board[posR][posC].prevC = from.col;
    }
    // try up, right, down, and left.
    if( posR == this.endCoord[0] && posC == this.endCoord[1] ) {
      // we found the end, since we were allowed to enter here it must be 
      // optimal to this point.
      return;
    } else {      
      this.board[posR][posC].visited = true;

      if( this.canMove( total, posR, posC, UP ) ) {
        this.findInner(total, posR - 1, posC, this.board[posR][posC] );
      }

      if( this.canMove( total, posR, posC, DOWN) ) {
        this.findInner(total, posR + 1, posC, this.board[posR][posC] );
      }

      if( this.canMove( total, posR, posC, RIGHT) ) {
        this.findInner(total, posR, posC + 1, this.board[posR][posC] );
      }

      if( this.canMove( total, posR, posC, LEFT) ) {
        this.findInner(total, posR, posC - 1, this.board[posR][posC] );
      }

      this.board[posR][posC].visited = false;
    }
  }

  private boolean canMove( int total, int posR, int posC, int dir ) {
    boolean retVal = false;

    switch(dir) {
      case UP:
        retVal = posR > 0 && !this.board[posR - 1][posC].visited && total < this.board[posR - 1][posC].memo;
        break;
      case DOWN:
        retVal = posR < (this.rows - 1) && !this.board[posR+1][posC].visited && total < this.board[posR+1][posC].memo;
        break;
      case RIGHT:
        retVal = posC < (this.cols - 1) && !this.board[posR][posC+1].visited && total < this.board[posR][posC+1].memo;
        break;
      case LEFT:
        retVal = posC > 0 && !this.board[posR][posC-1].visited && total < this.board[posR][posC-1].memo;
        break;
      default:
        retVal = false;
        break;
    }

    return retVal;
  }

  public void printSolution() {
    Cell [][] solved = new Cell [ rows ][];
    for( int i = 0; i < solved.length; i++ ) {
      solved[i] = new Cell [ cols ];
      for( int j = 0; j < solved[i].length; j++ ) {
        solved[i][j] = new Cell(i, j);
        solved[i][j].points = -1;
      }
    }

    solved[this.endCoord[0]][this.endCoord[1]].isEnd = true;
    solved[this.startCoord[0]][this.startCoord[1]].isStart = true;
    Cell tempCell = this.board[this.endCoord[0]][this.endCoord[1]];
    while( !tempCell.isStart  ) {
      if( !tempCell.isEnd ) {
        solved[tempCell.row][tempCell.col].points = tempCell.points;
      }
      tempCell = this.board[tempCell.prevR][tempCell.prevC];
    }

    System.out.println( this.toStringInner(solved));
  }

  public String toString() {
    return this.toStringInner(this.board);
  }

  public String toStringInner(Cell [][] grid) {
    StringBuilder sb = new StringBuilder();
    for( int i = 0; i < this.rows; i++ ) {
      this.printRow(grid, sb, i);
    }

    return sb.toString();
  }

  private void printRow(Cell [][] grid, StringBuilder sb, int row ) {
    if( row == 0 ) {
      for( int c = 0; c < this.cols; c++ ) {
        sb.append("+---");
      }
      sb.append("+\n");
    }
    for( int c = 0; c < this.cols; c++ ) {
      sb.append("| ");
      if( grid[row][c].isStart ) {
        sb.append("A");
      } else if( grid[row][c].isEnd ) {
        sb.append("B");
      } else if( grid[row][c].points == -1 ) {
        sb.append(" ");
      } else {
        sb.append(grid[row][c].points);
      }
      sb.append(" ");
    }
    sb.append("|\n");

    for( int c = 0; c < this.cols; c++ ) {
      sb.append("+---");
    }
    sb.append("+\n");
  }

  public static class Cell {
    int points = 0;
    int memo = Integer.MAX_VALUE;
    boolean visited = false;
    int prevR = 0, prevC = 0;
    int row = 0, col = 0;
    boolean isStart = false;
    boolean isEnd = false;

    public Cell( int r, int c ) {
      this.row = r;
      this.col = c;
    }
  }
}
