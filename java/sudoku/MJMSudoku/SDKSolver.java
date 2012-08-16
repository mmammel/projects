import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.Stack;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SDKSolver
{
  /**
   * Basic constructor
   */
  public SDKSolver(){ }

  public Set<Board> solve( Board b )
  {
    Set<Board> solutions = new HashSet<Board>();
    this.solve_internal( b, solutions );
    return solutions;
  }

  public Set<Board> solve( String filename ) throws IOException
  {
    return this.solve( new Board( filename ) );
  }

  /**
   * Recursive solver method.  Starts at top left position and
   * jumps to each blank square.  If the list of possibilities
   * for a square comes back blank the board is in an error state
   * and the method returns false.
   */
  private void solve_internal( Board b, Set<Board> solutions )
  {
    boolean retVal = true;
    int [] possible_vals = null;
    BoardCursor cursor = b.cursor();

    if( this.fillObviousPositions(b) )
    {

      System.out.println( b );

      if( b.filled() )
      {
        solutions.add( b );
      }
      else
      {
        cursor.nextBlank();
        possible_vals = this.getArrayFromMask( cursor.getPossibleValueMask() );

        for( int i = 0; (i < 9 && possible_vals[i] != -1); i++ )
        {
          System.out.println( "Guessing: " + possible_vals[i] + " for " + cursor );
          cursor.setValue( possible_vals[i] );
          this.solve_internal( new Board(b), solutions );
        }
      }
    }
  }

  /**
   * Starting at the given position search the current board for blanks
   * that can only contain 1 value, and fill it in.
   * If any were found, return true, else return false.
   */
  private boolean fillObviousPositions( Board b )
  {
    BoardCursor cursor = b.externalCursor();
    boolean found = false;
    boolean retVal = true;
    int singleVal = -1;
    int tempMask = 0;

    do
    {
      found = false;
      cursor.setPosition(0,0);

      while( !cursor.last() )
      {
        if( cursor.getValue() == Board.BLANK )
        {
          tempMask = cursor.getPossibleValueMask();

          if( tempMask == -1 || tempMask == 0 )
          {
System.out.println( "Board is in error, last guess was wrong" );
            //board is in error
            retVal = false;
            found = false;
            break;
          }
          else if( (singleVal = getSingleMaskValue( tempMask )) != -1 )
          {
System.out.println( "Setting: " + cursor + " to " + singleVal + " using rules" );
            cursor.setValue( singleVal );
            found = true;
          }
        }

        cursor.next();
      }
    } while( found );

    return retVal;
  }

  private int getSingleMaskValue( int mask )
  {
    int retVal = -1;


    for( int i = 0; i < 9; i++ )
    {
      if( mask == Board.VALS[i] )
      {
        retVal = i+1;
        break;
      }
    }

    return retVal;
  }


  /**
   * Given a mask, returns an integer array representing the values in
   * the mask.
   */
  private int [] getArrayFromMask( int mask )
  {
    int valCount = 0;
    int [] retArray = new int [9];
    for( int i = 0; i < Board.VALS.length; i++ )
    {
      retArray[i] = -1;

      if( (mask & Board.VALS[i]) != 0 )
      {
        retArray[valCount++] = i+1;
      }
    }

    return retArray;
  }

  public static void main( String [] args )
  {
    SDKSolver solver = null;
    Board solveMe = null;
    Set solutions = null;

    if( args.length != 1 )
    {
      System.out.println( "Usage: java SDKSolver <inputfile>" );
      return;
    }

    try
    {
      solver = new SDKSolver();
      solveMe = new Board( args[0] );

      System.out.println( "Processing:" );
      System.out.println( solveMe );

      if( solveMe.validate() )
      {
        solutions = solver.solve( solveMe );

        if( solutions.size() > 0 )
        {
          Board tempb = null;
          for( Iterator iter = solutions.iterator(); iter.hasNext(); )
          {
            tempb = (Board)iter.next();
            System.out.println( "Solution:" );
            System.out.println( tempb );
          }

        }
        else
        {
          System.out.println( "This puzzle has no solution." );
        }
      }
      else
      {
        System.out.println( "Board is Invalid:" );
        System.out.println( solveMe.getErrorInfo() );
      }
    }
    catch( Exception e )
    {
      System.out.println( "Error!  Caught an Exception: " + e.toString() );
      e.printStackTrace();
    }
  }

}

