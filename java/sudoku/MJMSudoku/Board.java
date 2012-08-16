import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * Encapsulation of a board, for faster searching.
 */
public class Board
{
  public static final int BLANK = -1;
  public static final int ZERO  = 0;
  public static final int ONE   = 1 << 0;
  public static final int TWO   = 1 << 1;
  public static final int THREE = 1 << 2;
  public static final int FOUR  = 1 << 3;
  public static final int FIVE  = 1 << 4;
  public static final int SIX   = 1 << 5;
  public static final int SEVEN = 1 << 6;
  public static final int EIGHT = 1 << 7;
  public static final int NINE  = 1 << 8;
  public static final int [] VALS = { ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE };
  public static final int MASK  = 0x1FF;
  public static final String [] SQUARES = { "top-left", "top-center", "top-right",
                                            "middle-left", "middle-center", "middle-right",
                                            "bottom-left", "bottom-center", "bottom-right" };
                                            
  private String [][] indices = {
    { "000000", "011001", "022002", "033010", "044011", "055012", "066020", "077021", "088022" },
    { "100103", "111104", "122105", "133113", "144114", "155115", "166123", "177124", "188125" },
    { "200206", "211207", "222208", "233216", "244217", "255218", "266226", "277227", "288228" },
    { "300340", "311341", "322342", "333350", "344351", "355352", "366360", "377361", "388362" },
    { "400443", "411444", "422445", "433453", "444454", "455455", "466463", "477464", "488465" },
    { "500546", "511547", "522548", "533556", "544557", "555558", "566566", "577567", "588568" },
    { "600660", "611661", "622662", "633670", "644671", "655672", "666680", "677681", "688682" },
    { "700763", "711764", "722765", "733773", "744774", "755775", "766783", "777784", "788785" },
    { "800866", "811867", "822868", "833876", "844877", "855878", "866886", "877887", "888888" }
  };
  
  private int [][] position = new int [9][9];
  private int blanks = 81;

  private Set<String> mBadRowLog = new HashSet<String>();
  private Set<String> mBadColLog = new HashSet<String>();
  private Set<String> mBadPosLog = new HashSet<String>();
  private BoardCursor mCursor = new BoardCursorImpl();

  public Board()
  {
    for( int i = 0; i < 9; i++ )
    {
      for( int j = 0; j < 9; j++ )
      {
        position[i][j] = BLANK;
      }
    }
  }

  public Board( int [][] board )
  {
    this();

    for( int i = 0; i < 9; i++ )
    {
      for( int j = 0; j < 9; j++ )
      {
        position[i][j] = board[i][j];

        if( position[i][j] != BLANK )
        {
          blanks--;
        }
      }
    }
  }

  public Board( Board b )
  {
    this();

    BoardCursor tempCursor = b.cursor();

    for( int i = 0; i < 9; i++ )
    {
      for( int j = 0; j < 9; j++ )
      {
        this.setValue( i, j, b.getValue(i, j) );
      }
    }

    mCursor.setPosition( tempCursor.row(), tempCursor.col() );
  }

  public int hashCode()
  {
    StringBuffer temp = new StringBuffer();

    for( int i = 0; i < 9; i++ )
    {
      for( int j = 0; j < 9; j++ )
      {
        temp.append(this.getValue(i,j));
      }
    }

    return temp.toString().hashCode();
  }

  public boolean equals( Object o )
  {
    boolean retVal = false;

    if( o instanceof Board )
    {
      retVal = this.equals( (Board)o );
    }

    return retVal;
  }

  public boolean equals( Board b )
  {
    for( int i = 0; i < 9; i++ )
    {
      for( int j = 0; j < 9; j++ )
      {
        if( this.getValue( i, j ) != b.getValue( i, j ) )
        {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Constructor.  Takes an input file name as an argument.
   * Minimal validation is done on the input, if the initial
   * board is in an error state, the results are not defined,
   * although the solve method should still return.
   */
  public Board( String filename ) throws IOException
  {
    this();
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
              this.setValue( row, i, readVal );
            }
          }
          else if( readChar == ' ' )
          {
            this.setValue(row, i, BLANK);
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
   * true if the board is completely filled
   */
  public boolean filled()
  {
    return (blanks == 0);
  }

  /**
   * Get the internal cursor for this board
   */
  public BoardCursor cursor()
  {
    return mCursor;
  }

  /**
   * Get an external cursor for this board
   */
  public BoardCursor externalCursor()
  {
    return new BoardCursorImpl();
  }

  /**
   * Set value at a position
   */
  public void setValue( int r, int c, int val )
  {
    if( this.position[r][c] == BLANK )
    {
      if( val != BLANK )
      {
        this.blanks--;
      }
    }
    else
    {
      if( val == BLANK )
      {
        this.blanks++;
      }
    }

    this.position[r][c] = val;
  }

  /**
   * Get value at a position
   */
  private int getValue( int r, int c )
  {
    return this.position[r][c];
  }

  /**
   * Get the index of the subsquare of a position
   */
  private int getSubSquareIdx( int row, int col )
  {
    return ((row/3)*3 + (col/3));
  }

  /**
   * Get the index of the subsquare we are in and also the index within that subsquare
   */
  private int [] getSubSquarePosition( int row, int col )
  {
    int [] retVal = new int [2];
    retVal[0] = Integer.parseInt("" + this.indices[row][col].charAt(4));
    retVal[1] = Integer.parseInt("" + this.indices[row][col].charAt(5));
    return retVal;
  }

  /**
   * Get the index of the row we are in and also the index within that row.
   */
  private int [] getRowPosition( int row, int col )
  {
    int [] retVal = new int [2];
    retVal[0] = Integer.parseInt("" + this.indices[row][col].charAt(0));
    retVal[1] = Integer.parseInt("" + this.indices[row][col].charAt(1));
    return retVal;
  }

  /**
   * Get the index of the column we are in and also the index within that column.
   */
  private int [] getColumnPosition( int row, int col )
  {
    int [] retVal = new int [2];
    retVal[0] = Integer.parseInt("" + this.indices[row][col].charAt(2));
    retVal[1] = Integer.parseInt("" + this.indices[row][col].charAt(3));
    return retVal;
  }

  /**
   * Print out a simple ascii representation of the board in
   * its current state.
   */
  public String toString()
  {
    StringBuffer sb = new StringBuffer();

    sb.append(" +-------+-------+-------+\n");

    for( int i = 0; i < 9; i++ )
    {
      for( int j = 0; j < 9; j++ )
      {
        sb.append( ((j%3 == 0) ? " | " : " ") + (this.position[i][j] == -1 ? " " : (""+this.position[i][j])) );
      }

      sb.append( " |\n" );
      if( i%3 == 2 ) sb.append(" +-------+-------+-------+\n");
    }

    return sb.toString();
  }

  /**********************
   * Validation methods *
   **********************/

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
  private void logBadPosition( int r, int c )
  {
    mBadPosLog.add( SQUARES[ this.getSubSquareIdx(r,c) ] );
  }

  /**
   * Validate a single position in the board
   */
  public boolean validate()
  {
    this.clearErrorLog();
    BoardCursor temp_cursor = new BoardCursorImpl();
    boolean isValid = true;

    for( int i = 0; i < 9; i++ )
    {
      for( int j = 0; j < 9; j++ )
      {
        if( i == j )
        {
          temp_cursor.setPosition( i, j );

          if( -1 == temp_cursor.getRowMask() )
          {
            isValid = false;
            logBadRow( i );
          }

          if( -1 == temp_cursor.getColMask() )
          {
            isValid = false;
            logBadColumn( i );
          }
        }

        if( (i%3 == 0) && (j%3 == 0) )
        {
          temp_cursor.setPosition( i, j );

          if( -1 == temp_cursor.getSubSquareMask() )
          {
            isValid = false;
            logBadPosition( i, j );
          }
        }
      }
    }

    return isValid;
  }

  public class ValueToIndexMap
  {
    public SortedSet<Integer> [] indexMap;
    
    public ValueToIndexMap()
    {
      indexMap = new SortedSet<Integer> [9];
      
      for( SortedSet<Integer> set : indexMap )
      {
        set = new TreeSet<Integer>();
      }
    }
    
    public void addMapping( int value, int idx )
    {
      this.indexMap[value - 1].add( idx );
    }
    
    public void removeMapping( int value, int idx )
    {
      this.indexMap[value - 1].remove( idx );
    }
    
    public String getValueString( int value )
    {
      StringBuffer retVal = new StringBuffer();
      for( int n : this.indexMap[value - 1] )
      {
        retVal.append( "" + n);
      }
      
      return retVal.toString();
    }
    
    public Map<String,Set<Integer>> getLikeValueSets()
    {
      Map<String,Set<Integer>> retVal = new HashMap<String,Set<Integer>>();
      String tempStr = "";
      Set<Integer> tempSet;
      
      for( int i = 1; i <= 9; i++ )
      {
        tempStr = this.getValueStr(i);
        if( !retVal.containsKey(tempStr) )
        {
          tempSet = new HashSet<Integer>();
          retVal.put( tempStr, tempSet );
        }
        else
        {
          tempSet = retVal.get(tempStr);
        }
        
        tempSet.add( i );
      }
      
      for( String key : retVal.keySet() )
      {
        if( key.length() != retVal.get(key).size() )
        {
          retVal.remove(key);
        }
      }
      
      return retVal;
    }
    
    public String toString()
    {
      StringBuffer retVal = new StringBuffer();
      for( int i = 0; i < 9; i++ )
      {
        retVal.append( "" + (i+1) + ":");
        retVal.append( this.getValueString(i+1) );
        retVal.append("\n");
      }
      
      return retVal.toString();
    }

  }

  /***********************************************
   * Implementation of the BoardCursor interface.*
   ***********************************************/
  private class BoardCursorImpl implements BoardCursor
  {
    private int row;
    private int col;
    private Board mBoard = null;

    private BoardCursorImpl()
    {
      this( Board.this, 0, 0 );
    }

    private BoardCursorImpl( Board b, int r, int c )
    {
      mBoard = b;
      row = r;
      col = c;
    }

    public int row()
    {
      return row;
    }

    public int col()
    {
      return col;
    }

    public void setPosition( int r, int c )
    {
      row = r;
      col = c;
    }

   /**
    * Moves to the next position, wraps if at the last position
    */
    public void next()
    {
      if( this.last() )
      {
        row = 0;
        col = 0;
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
    }

    /**
     * true if the cursor is in the last position, false otherwise
     */
    public boolean last()
    {
      return ((row == 8) && (col == 8));
    }

    /**
     * Move to the next (or stay on the current) blank and return true,
     * or retrun false if no more blanks are found.  wraps.
     */
    public boolean nextBlank()
    {
      int initrow = row;
      int initcol = col;
      boolean retVal = true;

      while( mBoard.position[row][col] != BLANK )
      {
        this.next();

        if( row == initrow && col == initcol )
        {
          retVal = false;
          break;
        }
      }

      return retVal;
    }

    /**
     * Move to the next filled position and returns true, if nothing
     * is filled, return false. wraps.
     */
    public boolean nextFilled()
    {
      int initrow = row;
      int initcol = col;
      boolean retVal = true;

      while( mBoard.position[row][col] == BLANK )
      {
        this.next();

        if( row == initrow && col == initcol )
        {
          retVal = false;
          break;
        }
      }

      return retVal;
    }

    public String toString()
    {
      return "(" + (row+1) + "," + (col+1) + ")";
    }

    /**
     * Get the value at this position
     */
    public int getValue()
    {
      return mBoard.getValue(row,col);
    }

    /**
     * Set the value at this position
     */
    public void setValue( int val )
    {
      mBoard.setValue( row, col, val );
    }

    /**
     * Get the index of the subsquare that this position is in
     */
    public int getSubSquareIdx()
    {
      return mBoard.getSubSquareIdx( row, col );
    }

    /**
      * Get the index of the subsquare we are in and also the index within that subsquare
      */
     public int [] getSubSquarePosition()
     {
       return mBoard.getSubSquarePosition( row, col );
     }

     /**
      * Get the index of the row we are in and also the index within that row.
      */
     public int [] getRowPosition()
     {
       return mBoard.getRowPosition( row, col );
     }

     /**
      * Get the index of the column we are in and also the index within that column.
      */
     public int [] getColumnPosition()
     {
       return mBoard.getColumnPosition( row, col );
     }
     
    /**
     * The get mask methods return a mask with bits filled corresponding
     * to values filled in the row, column or sub square.  If an error is detected,
     * i.e. repeat values in any row, column or subsquare, -1 is returned.
     */
    public int getRowMask()
    {
      int tempVal = 0;
      int mask = 0;

      for( int i = 0; i < 9; i++ )
      {
        if( (tempVal = mBoard.getValue( row, i )) != BLANK )
        {
          if( (mask & VALS[tempVal - 1]) != 0 )
          {
            mask = -1;
            break;
          }
          else
          {
            mask |= VALS[tempVal - 1];
          }
        }
      }

      return mask;
    }

    /**
     * Column mask
     */
    public int getColMask()
    {
      int tempVal = 0;
      int mask = 0;

      for( int i = 0; i < 9; i++ )
      {
        if( (tempVal = mBoard.getValue( i, col )) != BLANK )
        {
          if( (mask & VALS[tempVal - 1]) != 0 )
          {
            mask = -1;
            break;
          }
          else
          {
            mask |= VALS[tempVal - 1];
          }
        }
      }

      return mask;
    }

    /**
     * SubSquare mask
     */
    public int getSubSquareMask()
    {
      int row_mod = row % 3;
      int col_mod = col % 3;
      int tempVal = 0;
      int mask = 0;

      for( int i = 0; i < 3; i++ )
      {
        for( int j = 0; j < 3; j++ )
        {
          if( (tempVal = mBoard.getValue( (row + (i - row_mod)), (col + (j - col_mod)) ) )  != BLANK )
          {
            if( (mask & VALS[tempVal - 1]) == 0 )
            {
              mask |= VALS[tempVal - 1];
            }
            else
            {
              return -1;
            }
          }
        }
      }
      return mask;
    }

    /**
     * Get a mask representing the possible values, or -1 if there's an error
     */
    public int getPossibleValueMask()
    {
      int rowMask = 0, colMask = 0, sqrMask = 0;
      int retMask = 0;

      rowMask = this.getRowMask();
      colMask = this.getColMask();
      sqrMask = this.getSubSquareMask();

      if( rowMask >= 0 && colMask >= 0 && sqrMask >= 0 )
      {
        rowMask = ~rowMask & MASK;
        colMask = ~colMask & MASK;
        sqrMask = ~sqrMask & MASK;

        retMask = rowMask & colMask & sqrMask;
      }
      else
      {
        retMask = -1;
      }

      return retMask;
    }

  }

  /* End BoardCursor Impl */

  public static void main( String [] args )
  {
    try
    {
      Board board = new Board("evil.dat");

      System.out.println( "\n" + board );

      BoardCursor cursor = board.cursor();

      while( !cursor.last() )
      {
        System.out.println("Square mask at " + cursor.row() + "," + cursor.col() + ":" + Integer.toBinaryString(cursor.getSubSquareMask()) );
        System.out.println("Row mask at " + cursor.row() + "," + cursor.col() + ":" + Integer.toBinaryString(cursor.getRowMask()) );
        System.out.println("Col mask at " + cursor.row() + "," + cursor.col() + ":" + Integer.toBinaryString(cursor.getColMask()) );
        System.out.println("Possible value mask at " + cursor.row() + "," + cursor.col() + ":" + Integer.toBinaryString(cursor.getPossibleValueMask()) );

        cursor.next();
      }

      System.out.println( "\n" + board );
    }
    catch( Exception e )
    {
      System.out.println( "Exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}