import java.util.ArrayList;
import java.util.List;

/**
 *  There is a board with a starting space and an ending space.
 *  Each square on the board other than the start and end have a point value
 *  Get to the end from the start acumulating the fewest points possible
 */
public class OptimalPathBellmanFord {

  private List<Edge> edges;
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

  private static final String [] POSSIBLE_VALS = {
    "1", "2", "3", "32"
  };

  public static void main( String [] args ) {
    try {
      int r = Integer.parseInt(args[0]);
      int c = Integer.parseInt(args[1]);
      String d = args.length == 3 ? args[2] : null ;

      OptimalPathBellmanFord OP = new OptimalPathBellmanFord( r, c, d );
      OP.find();
    } catch( Exception e ) {
      System.out.println( e.toString() );
      e.printStackTrace();
    }
  }

  private String [] generateVals(int r, int c) {
    String [] retVal = new String [ r * c ];

    int valIdx = 0;
    for( int i = 0; i < retVal.length; i++ ) {
      valIdx = Double.valueOf(Math.floor(Math.random() * POSSIBLE_VALS.length)).intValue();
      retVal[i] = POSSIBLE_VALS[valIdx];
    }

    retVal[0] = "B";
    retVal[ retVal.length - 1 ] = "A";

    return retVal;
  }

  private List<Cell> getAdjacentCells( Cell c ) {
    List<Cell> retVal = new ArrayList<Cell>();

    if( c.row > 0 ) {
      // cell above
      retVal.add( this.board[c.row - 1][c.col]);
    }

    if( c.row < this.rows - 1 ) {
      // cell below
      retVal.add( this.board[c.row + 1][c.col] );
    }

    if( c.col > 0 ) {
      // cell to the left
      retVal.add( this.board[c.row][c.col - 1] );
    }

    if( c.col < this.cols - 1 ) {
      // cell to the right
      retVal.add( this.board[c.row][c.col + 1]);
    }
    
    return retVal;
  }

  /**
   * rows: number of rows > 2
   * cols: number of cols > 2
   * descriptor: a flat view of the board, comma separated, e.g. for 3x3:
   *   00,01,02,10,11,12,20,21,22
   * where each field is either an integer for points, "A" for start, or "B" for end, e.g.
   * B,1,4,3,3,2,4,1,A
   */
  public OptimalPathBellmanFord( int rows, int cols, String descriptor ) {
    String [] dsplit = null;
    this.edges = new ArrayList<Edge>();

    if( descriptor != null ) {
      dsplit = descriptor.split(",");
      if( dsplit.length != rows * cols ) throw new RuntimeException( "Bogus input" );
    } else {
      dsplit = this.generateVals( rows, cols );
    }

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

    Cell tempCell = null;
    for( int i = 0; i < this.rows; i++ ) {
      for( int j = 0; j < this.cols; j++ ) {
        tempCell = this.board[i][j];
        if( tempCell.isEnd ) continue;
        for( Cell cell : this.getAdjacentCells(tempCell) ) {
          if( cell.isStart ) continue;
          this.edges.add( new Edge( tempCell, cell ) );
        }
      }
    }
  }

  private int getIndexFromCoord( int r, int c ) {
    return r * this.cols + c;
  }

  /**
   * find the path.  use bellman ford.
   */
  public void find() {
    int [] dist = new int [ this.rows * this.cols ];
    for( int i = 0; i < dist.length; i++ ) {
      dist[i] = Integer.MAX_VALUE;
    }

    dist[ this.getIndexFromCoord(this.startCoord[0], this.startCoord[1]) ] = 0;

    Cell s = null;
    Cell d = null;
    int weight = 0, srcIdx = 0, destIdx = 0;

    for( int i = 1; i < dist.length; i++ ) {
if( i % 1000 == 0 ) System.out.println( "..." + i );
      for( Edge e : this.edges ) {
        s = e.src;
        d = e.dest;
        weight = e.cost;
        srcIdx = this.getIndexFromCoord(s.row, s.col);
        destIdx = this.getIndexFromCoord(d.row, d.col );
        if (dist[srcIdx] != Integer.MAX_VALUE && dist[srcIdx] + weight < dist[destIdx]) {
          dist[destIdx] = dist[srcIdx] + weight;
          d.memo = dist[destIdx];
          d.prevR = s.row;
          d.prevC = s.col;
        }
      }
    }

    System.out.println( this );

    for( Edge e : this.edges ) {
      System.out.println( e );
    }

    for( int i = 0; i < dist.length; i++ ) {
      System.out.print( dist[i] + " " );
    }
    System.out.println("");
    this.printSolution();
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

  public static class Edge {
    int cost = 0;
    Cell src = null;
    Cell dest = null;

    public Edge( Cell s, Cell d) {
      this.cost = d.points;
      this.src = s;
      this.dest = d;
    }

    public String toString() {
      return "[" + this.src.row + "," + this.src.col +"] --" + this.cost + "--> [" + this.dest.row + "," + this.dest.col + "]"; 
    }
  }
}
