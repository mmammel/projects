import java.util.*;
import java.io.*;

public class FreeFlow {
  private static final char BLANK = ' ';
  private char [][] board;
  private Map<Character,int []> anchorPositions;
  private Map<Character,int []> ends;
  private char [] anchors;
  private int numBlanks = 0;

  private static final char [] COLORS = { '0','1','2','3','4','5','6','7','8','9','!','@','#','$','%','^','&','*','(',')' };

  private long completedPaths = 0L;

  private int [][] moves = { {-1,0},{0,1},{1,0},{0,-1} };

  /**
   * Initialize the board from this format:
   *      
   * A   
   *   A  
   *  B   
   *    B
   */ 
  public FreeFlow( String filename ) throws IOException
  {
    BufferedReader reader = null;
    String readStr = null;
    int row = 0;
    int readVal = 0;
    char readChar = 0;
    List<String> rows = new ArrayList<String>();

    reader = new BufferedReader( new FileReader( filename ) );

    while( (readStr = reader.readLine()) != null && readStr.length() > 0 )
    {
      rows.add( readStr );
    }

    char [][] inputArray = new char [ rows.size() ][ rows.size() ];
    String tempStr = null;
    for( int i = 0; i < rows.size(); i++ ) {
      tempStr = rows.get(i);
      for( int j = 0; j < tempStr.length(); j++ ) {
        inputArray[i][j] = tempStr.charAt( j );
      }
    }

    this.initialize( inputArray );
  }


  /**
   * For internal testing.
   */
  public FreeFlow( char [][] array ) {
    this.initialize( array );
  }

  /**
   * Build the board from a two dimensional char array.
   */
  private void initialize( char [][] array ) {
    this.anchorPositions = new TreeMap<Character,int[]>();
    this.ends = new TreeMap<Character,int[]>();
    this.board = array;

    int [] tempArray = null;

    for( int i = 0; i < this.board.length; i++ ) {
      for( int j = 0; j < this.board[i].length; j++ ) {
        if( this.board[i][j] != BLANK ) {
          tempArray = new int [2];
          tempArray[0] = i;
          tempArray[1] = j;
          if( this.anchorPositions.containsKey( this.board[i][j] ) ) {
            // already found it, make it an end.
            this.ends.put( this.board[i][j], tempArray );
          } else {
            // first time we found it, it's an anchor.
            this.anchorPositions.put( this.board[i][j], tempArray );
          }
          // Lower case denotes an anchor or endpoint that has been connected to.
          this.board[i][j] = Character.toLowerCase( this.board[i][j] );
        } else {
          this.numBlanks++;
        }
      }
    }
    this.anchors = new char [ this.anchorPositions.size() ];
    int idx = 0;
    for( Character key : this.anchorPositions.keySet() ) {
      this.anchors[ idx++ ] = key;
    }
  }

  /**
   * Print the board to stdout
   */
  public void printBoard() {
    for( int i = 0; i < this.board.length; i++ ) {
      for( int j = 0; j < this.board[i].length; j++ ) {
        System.out.print( this.board[i][j] + " " );
      }
      System.out.print( "\n" );
    }
    System.out.println("");
  }

  /**
   * Get a particular board value, at an int[2] coordinate.
   */
  private char getBoardValue( int [] coord ) {
    return this.board[ coord[0] ][ coord[1] ];
  }

  /**
   * Set the board valued at a particular coordinate.
   */
  private void setBoardValue( int [] coord, char c ) {
    this.board[ coord[0] ][ coord[1] ] = c;
  }

  /**
   * Public solver, the entry point.
   */
  public void solve() {
    this.solve( 0 );
  }

  /**
   * Internal solver, used when it is time to move to a new
   * anchor.
   */
  private void solve( int anchorIdx ) {
    int [] tempCoord = null;
    List<int []> path = null;
    if( anchorIdx == this.anchors.length ) {
      if( this.numBlanks == 0 ) {
        this.printBoard();
      }
    } else {
      path = new ArrayList<int []>();
      tempCoord = this.anchorPositions.get( this.anchors[ anchorIdx ] );
      path.add( tempCoord );
      this.setBoardValue( tempCoord, Character.toUpperCase(this.getBoardValue( tempCoord )) );
      this.solve( anchorIdx, tempCoord, path );
      this.setBoardValue( tempCoord, Character.toLowerCase(this.getBoardValue( tempCoord )) );
    }
  }

  /**
   * recursive solve function, tries every path, trying to eliminate known bad moves.
   */
  private void solve(int anchorIdx, int [] position, List<int []> path ) {
    int [] nextPos = null;
    if( !doRegionColoring( Character.toLowerCase(this.anchors[anchorIdx])) ) {
      return;
    }

    for( int i = 0; i < this.moves.length; i++ ) {
      // try UP, RIGHT, DOWN, LEFT
      nextPos = this.getNextPosition( position, this.moves[i] );
      if( this.isTerminal( anchorIdx, nextPos ) ) {
        // reached the endpoint!
        this.setBoardValue( nextPos, Character.toUpperCase(this.getBoardValue( nextPos )) );
//if( ++this.completedPaths % 10000L == 0 ) this.printBoard();
        this.solve( anchorIdx + 1 );
        this.setBoardValue( nextPos, Character.toLowerCase(this.getBoardValue( nextPos )) );
      } else if( this.isValid( anchorIdx, nextPos, path ) ) {
        // we can move there, but it's not terminal.
        this.board[ nextPos[0] ][ nextPos[1] ] = this.anchors[ anchorIdx ];
        this.numBlanks--;
        path.add( nextPos );
        this.solve( anchorIdx, nextPos, path );
        path.remove( path.size() - 1 );
        this.board[ nextPos[0] ][ nextPos[1] ] = BLANK;
        this.numBlanks++;
      }
    }
  }

  /**
   * If we moved the path described by "path" to "position", would it be valid?
   *  1. is it in bounds.
   *  2. Does it create a "dead end"
   *  3. Does it double-back on itself.
   */
  private boolean isValid( int anchorIdx, int [] position, List<int []> path ) {
    boolean retVal = false;
    int [] pastMove = null;
    int [] tempLocation = null;
    char anchorName = this.anchors[ anchorIdx ];
    if( this.isInBounds( position ) ) {
      // we're in bounds
      if( this.board[ position[0] ][ position[1] ] == BLANK ) {
        retVal = true;

        //so far so good.  Make sure it's not a double-back.
        if( path.size() >= 3 ) {
          pastMove = path.get( path.size() - 3 );
          if( Math.abs( pastMove[0] - position[0] ) + Math.abs( pastMove[1] - position[1] ) == 1 ) {
            retVal = false;
          }
        }

        // If we're still ok check for a 4 move "dead ender"
        if( retVal && path.size() >= 4 ) {
          pastMove = path.get( path.size() - 4 );
          if( ( Math.abs( pastMove[0] - position[0] ) == 2 && pastMove[1] == position[1] ) ||
              ( Math.abs( pastMove[1] - position[1] ) == 2 && pastMove[0] == position[0] ) ) {
            /*
             *  We're in a "dead end" 4 move scenario, e.g.:
             *    A A A
             *    A * A
             *
             * Find coordinates for "*" and see if it is blank, if it is, don't allow this move.
             * if it is blank, this space can never be filled to make a solution.
             */
            tempLocation = new int [2];
            tempLocation[0] = pastMove[0] - ( (pastMove[0] - position[0]) / 2 );
            tempLocation[1] = pastMove[1] - ( (pastMove[1] - position[1]) / 2 );
            if( this.board[ tempLocation[0] ][ tempLocation[1] ] == BLANK ) {
              retVal = false;
            }
          }
        }
      }
    }
    return retVal;
  }

  /**
   * Is this point an endpoint of the current path?
   */
  private boolean isTerminal( int anchorIdx, int [] position ) {
    boolean retVal = false;
    int [] endpoint = this.ends.get( this.anchors[anchorIdx] );
    if( position[0] == endpoint[0] && position[1] == endpoint[1] ) {
      retVal = true;
    }

    return retVal;
  }

  /**
   * is the given point on the board?
   */
  private boolean isInBounds( int [] position ) {
    return ( position[0] >= 0 && position[0] < board.length &&
             position[1] >= 0 && position[1] < board[ position[0] ].length );
  }

  /**
   * Apply a simple shift (move) to a coordinate.
   */
  private int[] getNextPosition( int [] currPos, int [] move ) {
    int [] retVal = new int [2];
    retVal[0] = currPos[0] + move[0];
    retVal[1] = currPos[1] + move[1];
    return retVal;
  } 

  /**
   * after we color the regions, erase the coloring.
   */
  private void cleanColoring() {
    for( int i = 0; i < this.board.length; i++ ) {
      for( int j = 0; j < this.board[i].length; j++ ) {
        if( !Character.isLetter(this.board[i][j]) ) {
          this.board[i][j] = BLANK;
        }
      }
    }
  }

  /**
   * Get the first blank, from the top left of the board.
   */
  private int [] getBlank() {
    int [] retVal = null;
    for( int i = 0; i < this.board.length; i++ ) {
      for( int j = 0; j < this.board[i].length; j++ ) {
        if( this.board[i][j] == BLANK ) {
          retVal = new int [2];
          retVal[0] = i;
          retVal[1] = j;
          return retVal;
        }
      }
    }
    return retVal;
  }

  /**
   * Color in all of the blank regions, if we find an unacceptable region return false.
   */
  private boolean doRegionColoring(Character ignoreAnchor) {
    boolean retVal = true;
    Map<Character,int[]> anchorCounter = new HashMap<Character,int[]>();
    int [] nextBlank = null;
    int [] tempAnchorCount = null;
    int regionNameIdx = 0;
    int anchorCount = 0;

    while( (nextBlank = this.getBlank()) != null ) {
      anchorCounter.put( COLORS[regionNameIdx], new int [ this.anchors.length ] );
      anchorCount = this.colorRegion( nextBlank, regionNameIdx, anchorCounter, new HashSet<String>() );
      if( anchorCount == 0 ) {
        // we found a region with no anchors in it, inst-fail.
        retVal = false;
        break;
      }
      regionNameIdx++;
    }

    // If we made it through the coloring with no empty regions, see if 
    // we have any orphaned anchors.
    if( retVal ) {
      int [] testArray = new int [ this.anchors.length ];
      int [] regionArray = null;
      for( Character key : anchorCounter.keySet() ) {
        regionArray = anchorCounter.get( key );
        for( int i = 0; i < regionArray.length; i++ ) {
          if( i != (ignoreAnchor - 'a') && regionArray[i] > testArray[i] ) {
            testArray[i] = regionArray[i];
          }
        }
      }
     
      int count = 0; 
      for( int j : testArray ) {
        if( j == 1 ) {
          //System.out.println( this.getAnchorCountString( testArray ) );
          //this.printBoard();
          retVal = false;
          break;
        }
        count++;
      }
    }
    this.cleanColoring();
    return retVal;
  }


  /**
   * For logging a set.
   */
  private String getSetString( Set set ) {
    StringBuilder sb = new StringBuilder();
    sb.append("[ ");
    for( Object o : set ) {
      sb.append( o.toString() ).append(" " );
    }
    sb.append("]");
    return sb.toString();
  }

  private String getAnchorCountString( int [] anchorCounts ) {
    StringBuilder sb = new StringBuilder();
    for( int i = 0; i < anchorCounts.length; i++ ) {
      sb.append( (char)('a'+i) ).append(":").append( anchorCounts[i] ).append(" ");
    }
    return sb.toString();
  }

  /**
   * given a blank position in the board, recursively "color" all of the reachable blanks
   * from this one, and count how many anchors are colored in as well.
   * 
   * accumulate the anchor counts in a Map like this:
   *   [color(char)]->[int[]], where the int[] is a copy of the anchors array.
   */
  private int colorRegion( int [] position, int regionNameIdx, Map<Character,int[]> anchorCounter, Set<String> seen ) {
    int retVal = 0;
    int [] nextPos = null;
    String tempKey = null;
    char tempVal = 0;
    this.setBoardValue( position, COLORS[regionNameIdx] );
    for( int i = 0; i < this.moves.length; i++ ) {
      nextPos = this.getNextPosition( position, this.moves[i] );
      if( this.isInBounds( nextPos ) ) {
        tempVal = this.getBoardValue( nextPos );
        if( Character.isLowerCase( tempVal ) ) {
          // We found an anchor in this region, track it somehow.
          tempKey = ""+nextPos[0]+"_"+nextPos[1];
          if( !seen.contains( tempKey ) ) {
            seen.add( tempKey );
            retVal++;
            anchorCounter.get(COLORS[regionNameIdx])[ tempVal - 'a' ]++;
          }
        } else if( tempVal == BLANK ) {
          retVal += this.colorRegion( nextPos, regionNameIdx, anchorCounter, seen );
        }
      }
    }
    return retVal;
  }

  public static void main( String [] args ) {
    char [][] array = { 
                        { 'A', ' ', ' ', ' ' },
                        { ' ', ' ', 'A', ' ' },
                        { ' ', 'B', ' ', ' ' },
                        { ' ', ' ', ' ', 'B' }
                      };
    FreeFlow FF = null;

    try {
      if( args.length == 1 ) {
        FF = new FreeFlow(args[0]);
      } else {
        FF = new FreeFlow( array );
      }
    } catch( Exception e ) {
      System.out.println( "Exception caught: " + e.toString() );
    }

    FF.solve(); 
  }
}
