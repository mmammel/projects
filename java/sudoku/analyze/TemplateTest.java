import java.util.Stack;
import java.io.File;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;

public class TemplateTest
{
  private static final String START_BOARD = "S";
  private static final String END_BOARD = "E";
  public static final int BLANK = -1;
  public int [][] board = new int [9][9];
  private int [] marked_cols = new int [9];
  private int [] marked_rows = new int [9];
  private SubSquare [] sub_squares = new SubSquare [9];
  private long count = 0L;
  private long meta_count = 0L;
  private Stack moveStack = new Stack();
  private BufferedReader boardReader = null;
  private boolean printing = false;
  private int numberToPrint = 1;

  public TemplateTest()
  {
    int subidx = 0;
    for( int i = 0; i < 9; i++ )
    {
      for( int j = 0; j < 9; j++ )
      {
        board[i][j] = -1;
        
        if( i%3 == 0 && j%3 == 0 )
        {
          sub_squares[subidx++] = new SubSquare(i,j);
        }
      }
    }
    
    this.reset_marks();
  }

  public void setNumber( int n )
  {
    if( n >= 1 && n <= 9 )
    {
      numberToPrint = n;
    }
  }

  public TemplateTest( String filename ) throws IOException
  {
    this();
    
    boardReader = new BufferedReader( new FileReader( filename ) );
  }
  
  private void reset_marks()
  { 
    count = 0;
    
    for( int k = 0; k < 9; k++ )
    {
      marked_rows[k] = -1;
      marked_cols[k] = -1;
    }
  }

  public void setPrintingOption( boolean val )
  {
    printing = val;
  }

  private boolean readBoard() throws IOException
  {
    String readStr = "";
    int row = 0;
    int readVal = 0;
    char readChar = 0;
    boolean boardRead = false;
    
    meta_count++;
    
    while( (readStr = boardReader.readLine()) != null )
    {
      if( readStr.equals( START_BOARD ) )
      {    
        while( ((readStr = boardReader.readLine()) != null) && row < 9 )
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
                  board[row][i] = readVal;
                }
              }
              else if( readChar == ' ' )
              {
                board[row][i] = BLANK;
              }
              else
              {
                throw new IOException( "Bad character #" + (i+1) + " in row " + row + ": " + readStr );
              }
            } //end for
          } //end else
    
          row++;
        } //end while
        
        if( readStr != null && readStr.equals( END_BOARD ) )
        {
          boardRead = true;
          break;
        }
        
      } //end if NEW_BOARD
    } //end while
    
    this.reset_marks();
    
    return boardRead;
  }

  private void pushMove( Position p )
  { 
    board[p.row][p.col] = numberToPrint;
    markRow( p.row );
    markCol( p.col );
    moveStack.push( p );
  }
  
  private void popMove()
  {
    Position temp = (Position)moveStack.pop();
    unmarkRow( temp.row );
    unmarkCol( temp.col );
    board[temp.row][temp.col] = BLANK;
  }

  private void markRow( int r )
  {
    for( int i = 0; i < 9; i++ )
    {
      if( marked_rows[i] == BLANK )
      {
        marked_rows[i] = r;
        break;
      }
    }
  }

  private void markCol( int c )
  {
    for( int i = 0; i < 9; i++ )
    {
      if( marked_cols[i] == BLANK )
      {
        marked_cols[i] = c;
        break;
      }
    }
  }

  private void unmarkRow( int r )
  {
    for( int i = 0; i < 9; i++ )
    {
      if( marked_rows[i] == r )
      {
        marked_rows[i] = BLANK;
        break;
      }
    }
  }

  private void unmarkCol( int c )
  {
    for( int i = 0; i < 9; i++ )
    {
      if( marked_cols[i] == c )
      {
        marked_cols[i] = -1;
        break;
      }
    }
  }

  public void traverse() throws IOException
  {
    if( boardReader != null )
    {
      while( this.readBoard() )
      {
        traverse_inner(0);
        System.out.println( "[" + meta_count + "] Count: " + count );
      }
    }
    else
    {
      traverse_inner(0);
      System.out.println( "Count: " + count );
    }
  }

  private void traverse_inner( int square )
  {
    for( int i = 0; i < 9; i++ )
    {
      if( !sub_squares[square].positions[i].rowMarked() && 
          !sub_squares[square].positions[i].colMarked() && 
          board[sub_squares[square].positions[i].row][sub_squares[square].positions[i].col] == BLANK)
      {
        pushMove( sub_squares[square].positions[i] );
        if( square < 8 )
        {
          traverse_inner( square + 1 );
        }
        else
        {
          count++;
          if(printing) dumpBoard();
        }
        popMove();
      }
    }
  }

  public void dumpBoard()
  {
    System.out.println("S");

    for( int i = 0; i < 9; i++ )
    {
      for( int j = 0; j < 9; j++ )
      {
        System.out.print( (board[i][j] == -1 ? " " : (""+board[i][j]) ) );
      }

      System.out.print( "\n" );
    }
    System.out.println("E");
  }
  
  class SubSquare
  {
    private Position root;
    public Position [] positions;
    
    public SubSquare( int x, int y )
    {
      int posidx = 0;
      root = new Position(x,y);
      positions = new Position [9];
      
      for( int i = 0; i < 3; i++ )
      {
        for( int j = 0; j < 3; j++ )
        {
          positions[posidx++] = new Position(x+i,y+j);
        }
      }
    }
  }
  
  class Position
  {
    public int row;
    public int col;
    
    public boolean rowMarked()
    {
      boolean retVal = false;
      
      for( int i = 0; i < 9; i++ )
      {
        if( marked_rows[i] == row )
        {
          retVal = true;
          break;
        }
      }
        
      return retVal;
    }
    
    public String toString()
    {
      return "(" + (row+1) + "," + (col+1) + ")";
    }
    
    public boolean colMarked()
    {
      boolean retVal = false;
      
      for( int i = 0; i < 9; i++ )
      {
        if( marked_cols[i] == col )
        {
          retVal = true;
          break;
        }
      }
        
      return retVal;
    }
    
    public Position( int r, int c )
    {
      row = r;
      col = c;
    }
  }
  
  public static void main( String [] args )
  {
    TemplateTest tt = null;
    boolean printing = false;
    String filename = null;
    int num_to_use = -1;
    
    try
    {
      for( int i = 0; i < args.length; i++ )
      {
        if( args[i].equals("-p") )
        {
          printing = true;
        }
        else if( args[i].equals("-f") )
        {
          if( ++i < args.length )
          {
            filename = args[i];
          }
          else
          {
            System.out.println( "Usage: java TemplateTest [-p] [-f <filename>] [-n <1-9>]" );
            System.exit(1);
          }
        }
        else if( args[i].equals("-n") )
        {
          if( ++i < args.length )
          {
            num_to_use = new Integer( args[i] ).intValue();
          }
          else
          {
            System.out.println( "Usage: java TemplateTest [-p] [-f <filename>] [-n <1-9>]" );
            System.exit(1);
          }
        }
      }
      
      if( filename != null )
      {
        System.out.println("JOMAMA");
        tt = new TemplateTest( filename );
      }
      else
      {
        tt = new TemplateTest();
      }
      
      if( num_to_use != -1 ) tt.setNumber( num_to_use );
      tt.setPrintingOption( printing );
    
      tt.traverse();
    }
    catch( Exception e )
    {
      System.out.println( "Exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}