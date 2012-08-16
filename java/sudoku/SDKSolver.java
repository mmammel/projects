import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Stack;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class SDKSolver
{
  public static final int BLANK = -1;
  private static final int ONE   = 1 << 0;
  private static final int TWO   = 1 << 1;
  private static final int THREE = 1 << 2;
  private static final int FOUR  = 1 << 3;
  private static final int FIVE  = 1 << 4;
  private static final int SIX   = 1 << 5;
  private static final int SEVEN = 1 << 6;
  private static final int EIGHT = 1 << 7;
  private static final int NINE  = 1 << 8;
  private static final int [] VALS = { ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE };
  private static final int MASK  = 0x1FF;
  private static final String [] SQUARES = { "top-left", "top-center", "top-right",
                                             "middle-left", "middle-center", "middle-right",
                                             "bottom-left", "bottom-center", "bottom-right" };

  private int [][] mBoard = new int [9][9];
  private Stack mMoves = new Stack();
  private Set mBadRowLog = new HashSet();
  private Set mBadColLog = new HashSet();
  private Set mBadPosLog = new HashSet();
    
  /**
   * Constructor.  Takes an input file name as an argument.
   * Minimal validation is done on the input, if the initial
   * board is in an error state, the results are not defined, 
   * although the solve method should still return.
   */
  public SDKSolver( String filename ) throws IOException
  {
    BufferedReader reader = null;
    String readStr = null;
    int row = 0;
    int readVal = 0;
    char readChar = 0;

    reader = new BufferedReader( new FileReader( filename ) );

    while( ((readStr = reader.readLine()) != null) && row < 9 )
    {
      if( readStr.length() != 9 )
      {
        throw new IOException( "Bad Input!  Line: " + readStr + " does not have length 9" );
      }
      else
      {
        for( int i = 0; i < 9; i++ )
        {
          readChar = readStr.charAt(i);
          if( Character.isDigit( readChar ) )
          {
            readVal = Character.digit( readChar, 10 );
            if( readVal > 9 || readVal < 1 )
            {
              throw new IOException( "Bad number in row " + row + ": " + readStr );
            }
            else
            {
              mBoard[row][i] = readVal;
            }
          }
          else if( readChar == ' ' )
          {
            mBoard[row][i] = BLANK;
          }
          else
          {
            throw new IOException( "Bad character #" + (i+1) + " in row " + row + ": " + readStr );
          }
        } //end for
      } //end else

      row++;
    } //end while
  }

  /**
   * Print out a simple ascii representation of the board in
   * its current state.
   */
  public void dumpBoard()
  {
    System.out.println(" +-------+-------+-------+");

    for( int i = 0; i < 9; i++ )
    {
      for( int j = 0; j < 9; j++ )
      {
        System.out.print( ((j%3 == 0) ? " | " : " ") + (mBoard[i][j] == -1 ? " " : (""+mBoard[i][j])) );
      }

      System.out.print( " |\n" );
      if( i%3 == 2 ) System.out.println(" +-------+-------+-------+");
    }
  }

  /**
   * Pushes a move onto the stack and sets the 
   * move on the board.
   */
  private void pushMove( Move mv )
  {
    mMoves.push( mv );
    mBoard[mv.row][mv.col] = mv.val;
  }

  /**
   * Pops the last move from the stack and undoes
   * the move on the board.
   */
  private Move popMove()
  {
    Move mv = (Move)mMoves.pop();
    mBoard[mv.row][mv.col] = BLANK;
    return mv;
  }

  /**
   * Public solve method.
   */
  public boolean solve()
  {
    return this.solve_internal(0,0);
  }

  /**
   * Public board validation method, validates all filled in
   * squares
   */
  public boolean isBoardValid()
  {
    this.clearErrorLog();
    
    return this.validate_all();
  }
  
  /**
   * Public position validation method, validates a given position
   */
  public boolean isPositionValid( int r, int c )
  {
    this.clearErrorLog();
    
    return this.validate_position( new Position( r, c ) );
  }

  /**
   * Purge the validation log
   */
  private void clearErrorLog()
  {
    mBadRowLog.clear();
    mBadColLog.clear();
    mBadPosLog.clear();
  }

  /**
   * Return a string representation of the error log
   */
  public String getErrorInfo()
  {
    StringBuffer errors = new StringBuffer();
    
    for( Iterator row_iter = mBadRowLog.iterator(); row_iter.hasNext(); )
    {
      errors.append("Row #" + row_iter.next() + " contains duplicates.\n");
    }
    
    for( Iterator col_iter = mBadColLog.iterator(); col_iter.hasNext(); )
    {
      errors.append("Column #" + col_iter.next() + " contains duplicates.\n");
    }
    
    for( Iterator pos_iter = mBadPosLog.iterator(); pos_iter.hasNext(); )
    {
      errors.append("The " + pos_iter.next() + " subsquare contains duplicates.\n" );
    }
    
    return errors.toString();
  }

  /**
   * Log a bad row
   */
  private void logBadRow( int row )
  {
    mBadRowLog.add(""+(row+1));
  }
  
  /**
   * Log a bad column
   */
  private void logBadColumn( int col )
  {
    mBadColLog.add(""+(col+1));
  }
  
  /**
   * Log a bad subsquare based on a position
   */
  private void logBadPosition( Position p )
  {
    mBadPosLog.add( SQUARES[ p.getSubSquareIdx() ] );
  }

  /**
   * Internal board validation method, validates all filled in
   * positions
   */
  private boolean validate_all()
  {
    boolean retVal = true;
    
    Position p = new Position(0,0);
  
    while( p.findNextValue() )
    {
      if( !validate_position(p) )
      {
        retVal = false;
      }
      p.increment();
    }
    
    return retVal;
  }
  
  /**
   * internal position validation method, validates a given
   * position.
   */
  private boolean validate_position( Position p )
  {
    boolean retVal = false;
    int [] possible_vals = this.getPossibilitiesForPosition(p);
    
    if( possible_vals != null && possible_vals.length > 0 )
    {
      for( int i = 0; i < possible_vals.length; i++ )
      {
        if( mBoard[p.row][p.col] == possible_vals[i] )
        {
          retVal = true;
          break;
        }
      }
    }
    else
    {
      retVal = false;
    }
    
    return retVal;
  }

  /**
   * Recursive solver method.  Starts at top left position and
   * jumps to each blank square.  If the list of possibilities 
   * for a square comes back blank the board is in an error state
   * and the method returns false.
   */
  private boolean solve_internal( int r, int c )
  {
    boolean retVal = false;
    int [] possible_vals = null;
    Position p = new Position(r,c);

    if( !p.findNextBlank() )
    {
      retVal = true;
      System.out.println("Solution:");
      this.dumpBoard();
    }
    else
    {
      possible_vals = this.getPossibilitiesForPosition( p );

      if( possible_vals == null || possible_vals.length == 0 )
      {
        retVal = false;
      }
      else
      {
        for( int i = 0; i < possible_vals.length; i++ )
        {
          this.pushMove( new Move( p.row, p.col, possible_vals[i] ) );

          if( this.solve_internal( p.row, p.col ) )
          {
            retVal = true;
            this.popMove();
            //break;
          }
          else
          {
            this.popMove();
          }
        }
      }
    }
    return retVal;
  }

  /**
   * This gets the positions within the given position's 3x3
   * square, other than those that lie within the same row or
   * column.  Those are handled by the row and column checks.
   */
  private Position [] getLocalPositions( Position p )
  {
    int row_mod = p.row % 3;
    int col_mod = p.col % 3;
    Position [] retVal = new Position [8];
    int pos_count = 0;

    for( int i = 0; i < 3; i++ )
    {
      for( int j = 0; j < 3; j++ )
      {
        if( i != row_mod || j != col_mod )
        {
          retVal[pos_count++] = new Position( (p.row + (i - row_mod)), (p.col + (j - col_mod)) );
        }
      }
    }

    return retVal;
  }

  /**
   * Given a position in the board, find all the possible values
   * for it based on values found in the same row, column, and local
   * 3x3 square.
   */
  private int [] getPossibilitiesForPosition( Position p )
  {
    int [] retArray = null;
    int rowMask, colMask, localMask;
    rowMask   = this.eliminateRowVals( p );
    colMask   = this.eliminateColVals( p );
    localMask = this.eliminateLocalVals( p );
    
    if( rowMask != -1 && colMask != -1 && localMask != -1 )
    {
      retArray = getArrayFromMask( rowMask & colMask & localMask );
    }

    return retArray;
  }

  /**
   * Remove the decimal "remove" value (assumed to be 1-9) from the given
   * mask. The updated mask is returned in the parameter list, while true is
   * returned if the value is successfully removed and false is returned if 
   * the value did not exist in the mask in the first place.
   */
  private boolean removePossibility( Mask m, int remove )
  {
    boolean retVal = true;
    
    if( (m.val & VALS[remove - 1]) == 0 )
    {
      retVal = false;
    }
    else
    {
      m.val ^= VALS[remove - 1];
    }
    
    return retVal;
  }

  /**
   * Returns a mask containing the bit-representations of the values not
   * eliminated by values in the given position's row
   */
  private int eliminateRowVals( Position p )
  {
    Mask mask = new Mask();
    boolean error = false;
    
    for( int i = 0; i < 9; i++ )
    {   
      if( i != p.col && mBoard[p.row][i] != BLANK )
      {
        if( !this.removePossibility( mask, mBoard[p.row][i] ) )
        {
          //board is in an error state, log the error
          this.logBadRow( p.row );
          error = true;
        }
        else if( p.getValue() == mBoard[p.row][i] )
        {
          this.logBadRow( p.row );
          error = true;
        }
      }
    }
    
    return error ? -1 : mask.val;
  }
  
  /**
   * Returns a mask containing the bit-representations of the values not
   * eliminated by values in the given position's column
   */
  private int eliminateColVals( Position p )
  {
    boolean error = false;
    Mask mask = new Mask();
    
    for( int i = 0; i < 9; i++ )
    {   
      if( i != p.row && mBoard[i][p.col] != BLANK )
      {
        if( !this.removePossibility( mask, mBoard[i][p.col] ) )
        {
          //board is in an error state, log the error
          this.logBadColumn( p.col );
          error = true;
        }
        else if( p.getValue() == mBoard[i][p.col] )
        {
          this.logBadColumn( p.col );
          error = true;
        }
      }
    }
    
    return error ? -1 : mask.val;
  }
  
  /**
   * Returns a mask containing the bit-representations of the values not
   * eliminated by values in the given position's local 3x3 square
   */
  private int eliminateLocalVals( Position p )
  {
    boolean error = false;
    Mask mask = new Mask();
    
    Position [] adjPos = this.getLocalPositions( p );

    for( int i = 0; i < adjPos.length; i++ )
    {
      if( adjPos[i].getValue() != BLANK )
      {
        if( !this.removePossibility( mask, adjPos[i].getValue() ) )
        {
          //board is in an error state, log the error
          this.logBadPosition( p );
          error = true;
        }
        else if( p.getValue() == adjPos[i].getValue() )
        {
          this.logBadPosition( p );
          error = true;
        }
      }
    }
    return error ? -1 : mask.val;
  }

  /**
   * Given a mask, returns an integer array representing the values in 
   * the mask.
   */
  private int [] getArrayFromMask( int mask )
  {
    int valCount = 0;
    int [] retArray = null;
    
    for( int i = 0; i < VALS.length; i++ )
    {
      if( (mask & VALS[i]) != 0 )
      {
        valCount++;
      }
    }
    
    retArray = new int [valCount];
    valCount = 0;
    
    for( int j = 0; j < VALS.length; j++ )
    {
      if( (mask & VALS[j]) != 0 )
      {
        retArray[valCount++] = j + 1;
      }
    }
    
    return retArray;
  }

  /**
   * Used to encapsulate a position and a value so they can be
   * pushed and popped on the stack during recursion.
   */
  private class Move
  {
    public int row;
    public int col;
    public int val;

    public String toString()
    {
      return "(" + (row+1) + "," + (col+1) + ")[" + val + "]";
    }

    public Move() { }
    public Move( Position p, int v ) { this( p.row, p.col, v ); }
    public Move( int r, int c, int v )
    {
      row = r;
      col = c;
      val = v;
    }
  }

  /**
   * Encapsulates a position and provides methods for incrementing
   * to the next position, indicating the last position, and finding 
   * the next blank position.
   */
  private class Position
  {
    public int row = 0;
    public int col = 0;

    public Position( int r, int c ) { row = r; col = c; }

    public String toString()
    {
      return "(" + (row+1) + "," + (col+1) + ")";
    }

    /**
     * Get the value at this position
     */
    public int getValue()
    {
      return mBoard[row][col];
    }

    /**
     * Get the index of the subsquare that this position is in
     */
    public int getSubSquareIdx()
    {
      return ((row/3)*3 + (col/3));
    }

    /**
     * returns true if the position is the bottom right
     * corner of the board, false otherwise.
     */
    public boolean isLast()
    {
      boolean retVal = false;

      if( row == 8 && col == 8 )
      {
        retVal = true;
      }

      return retVal;
    }

    /**
     * Moves the position to the next position and returns true, 
     * if the position is in the last position, the method returns false
     */
    public boolean increment()
    {
      boolean retVal = true;

      if( isLast() )
      {
        retVal = false;
      }
      else
      {
        if( col < 8 )
        {
          col++;
        }
        else
        {
          col = 0;
          row++;
        }
      }
      return retVal;
    }

    /**
     * Attempts to change positions to the next position on the 
     * board with a blank in it.  If the position is on a blank square
     * when the method is called, no increment will happen and the return
     * value is true, unless the position is the last position.
     */
    public boolean findNextBlank()
    {
      boolean retVal = true;

      if( isLast() && mBoard[row][col] == BLANK )
      {
        retVal = false;
      }
      else
      {
        while( mBoard[row][col] != BLANK )
        {
          retVal = true;
        
          if( !increment() )
          {
            retVal = false;
            break;
          }
        }
      }

      return retVal;
    }

    /**
     * Attempts to change positions to the next position on the 
     * board with a value in it.  If the position is on a filled square
     * when the method is called, no increment will happen and the return
     * value is true, unless the position is the last position.
     */
    public boolean findNextValue()
    {
      boolean retVal = true;

      if( isLast() && mBoard[row][col] != BLANK )
      {
        retVal = false;
      }
      else
      {
        while( mBoard[row][col] == BLANK )
        {
          retVal = true;
        
          if( !increment() )
          {
            retVal = false;
            break;
          }
        }
      }
      
      return retVal;
    }
    
  }

  /**
   * Need to wrap the mask with an object in order to persist values across method calls
   */
  private class Mask
  {
    public Mask( int n ) { val = n; };
    public Mask() { val = MASK; }
    
    public int val;
  }
  
  public static void main( String [] args )
  {
    SDKSolver solver = null;

    if( args.length != 1 )
    {
      System.out.println( "Usage: java SDKSolver <inputfile>" );
      return;
    }

    try
    {
      solver = new SDKSolver(args[0]);

      System.out.println( "Processing:" );
      solver.dumpBoard();
      
      if( solver.isBoardValid() )
      {
        if( solver.solve() )
        {
          //System.out.println( "Solution:" );
          //solver.dumpBoard();
        }
        else
        {
          System.out.println( "This puzzle has no solution." );
        }
      }
      else
      {
        System.out.println( "\nThe Board is Invalid!!\n----------------------" );
        System.out.println( solver.getErrorInfo() );
      }
    }
    catch( Exception e )
    {
      System.out.println( "Error!  Caught an Exception: " + e.toString() );
      e.printStackTrace();
    }
  }

}

