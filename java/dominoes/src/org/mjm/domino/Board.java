package org.mjm.domino;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileReader;

public class Board {

  private int [][] board;
  private DominoDescriptor [][] dominoes;
  private int [] cursor;
  private int rows = 0;
  private int cols = 0;
  
  private Set<Domino> dominoPool = EnumSet.allOf(Domino.class);
  
  private static final int ROW = 0;
  private static final int COL = 1;

  public Board() {}

  public Board( String filename ) throws IOException
  {
    this();
    BufferedReader reader = null;
    String readStr = null;
    int readVal = 0;
    char readChar = 0;

    this.cursor = new int [2];
    this.cursor[0] = 0;
    this.cursor[1] = 0;
    
    List<List<Integer>> tempBoard = new ArrayList<List<Integer>>(); // so we are dimension agnostic
    List<Integer> tempRow = null;

    try {
      reader = new BufferedReader( new FileReader( filename ) );

      while( ((readStr = reader.readLine()) != null) )
      {
        tempRow = new ArrayList<Integer>();
        tempBoard.add( tempRow );

        for( int i = 0; i < readStr.length(); i++ )
        {
          readChar = readStr.charAt(i);
          if( Character.isDigit( readChar ) )
          {
            readVal = Character.digit( readChar, 10 );
            if( readVal > 6 )
            {
              throw new IOException( "Bad number in row " + i + ": " + readStr );
            }
            else
            {
              tempRow.add( readVal );
            }
          }
        }
      }
    } catch( IOException ioe ) {
      System.out.println( "Caught an exception: " + ioe.toString() );
    } finally {
      if( reader != null ) {
        try {
          reader.close();
        } catch( IOException e ) {
          // do nothing;
        }
      }
    }

    this.rows = tempBoard.size();
    this.cols = tempBoard.get(0).size();
    
    this.board = new int [ this.rows ][ this.cols ];
    this.dominoes = new DominoDescriptor[ this.rows ][ this.cols ];

    for( int i = 0; i < tempBoard.size(); i++ ) {
      tempRow = tempBoard.get(i);
      for( int j = 0; j < tempRow.size(); j++ ) {
        this.board[i][j] = tempRow.get(j);
        this.dominoes[i][j] = null;
      }
    }
  }
  
  private boolean isFree() {
    return this.isFree(this.cursor[ROW], this.cursor[COL]);
  }
  
  private boolean isFree( int i, int j ) {
    boolean retVal = true;
    DominoDescriptor d = null;
    
    if( i < 0 || i >= this.rows || j < 0 || j >= this.cols ) {
      retVal = false;
    } else if( (d = this.dominoes[i][j]) != null ) {
      retVal = false;
    } else if( i > 0 && (d = this.dominoes[i-1][j]) != null && d.getPosition() == DominoPosition.Vertical ) {
      retVal = false;
    } else if( j > 0 && (d = this.dominoes[i][j-1]) != null && d.getPosition() == DominoPosition.Horizontal ) {
      retVal = false;
    }
    
    return retVal;
  }
  
  public int [] getCursorPos() {
    return this.cursor;
  }
  
  public boolean advanceCursor() {
    boolean retVal = true; // did we advance?
    if( this.cursor[COL] < (this.cols - 1) ) {
      this.cursor[COL]++;
    } else if( this.cursor[ROW] < (this.rows - 1) ) {
      this.cursor[COL] = 0;
      this.cursor[ROW]++;
    } else {
      // reached the end!
      retVal = false;
    }
    
    return retVal;
  }
  
  public boolean gotoNextFree() {
    boolean retVal = true;
    while( !this.isFree() ) {
      //System.out.println( "Position [" + this.cursor[ROW] + "," + this.cursor[COL] + "] is NOT Free");
      if( !this.advanceCursor() ) {
        retVal = false;
        break;
      }
    }
    
    return retVal;
  }
  
  public boolean retreatCursor() {
    boolean retVal = true; // did we advance?
    
    do {
      if( this.cursor[COL] > 0 ) {
        this.cursor[COL]--;
      } else if( this.cursor[ROW] > 0 ){
        this.cursor[COL] = this.cols - 1;
        this.cursor[ROW]--;
      } else {
        // reached the beginning!
        retVal = false;
      }
    } while( retVal == true && !this.isFree() );
    
    return retVal;
  }
  
  public void setCursorPos( int i, int j ) {
    this.cursor[ROW] = i;
    this.cursor[COL] = j;
  }
  
  /**
   * Attempt to place a domino at the current cursor position in the given orientation.
   * @param i
   * @param j
   * @param p
   * @return
   */
  public boolean placeDomino( DominoPosition p ) {
    boolean retVal = false;
    Domino d = null;
    
    int i = this.cursor[ROW];
    int j = this.cursor[COL];
    
    if( p == DominoPosition.Horizontal ) {
      if( this.isFree( i, j) && this.isFree(i, j+1) ) {
        d = Domino.getDomino(this.board[i][j], this.board[i][j+1]);
        if( this.dominoPool.contains(d) ) {
          this.dominoes[i][j] = new DominoDescriptor( d, p );
          //System.out.println("Placed: \n" + this.dominoes[i][j] + " at [" + i + "," + j + "]");
          retVal = true;
        } else {
          //System.out.println( d + "is not available...");
        }
      }
    } else if( p == DominoPosition.Vertical ) {
      if( this.isFree(i, j) && this.isFree(i+1, j)) {
        d = Domino.getDomino(this.board[i][j], this.board[i+1][j]);
        if( this.dominoPool.contains(d)) {
          this.dominoes[i][j] = new DominoDescriptor( d, p );
          //System.out.println("Placed: \n" + this.dominoes[i][j] + " at [" + i + "," + j + "]");
          retVal = true;
        } else {
          //System.out.println( d + "is not available...");
        }
      }
    }
    
    if( retVal ) this.dominoPool.remove(d);
    
    return retVal;
  }
  
  /**
   * Remove the domino at the current cursor position, if there is one there.
   * @param i
   * @param j
   */
  public void removeDomino() {
    DominoDescriptor dd = this.dominoes[this.cursor[ROW]][this.cursor[COL]];
    if( dd != null ) {
      this.dominoPool.add(dd.getDomino());
      this.dominoes[this.cursor[ROW]][this.cursor[COL]] = null;
    }
  }
  
  public void play() {
    int [] currPos = new int [2];
    currPos[ROW] = this.cursor[ROW];
    currPos[COL] = this.cursor[COL];
    
    // try placing a domino horizontally
    if( this.placeDomino(DominoPosition.Horizontal) ) {
      if( this.gotoNextFree() ) {
        this.play();
      } else if( this.dominoPool.isEmpty() ) {
        // We found a solution!
        System.out.println( "Solution found: \n" );
        System.out.println(this);
      }
    }
    
    // reset the cursor, remove the last placed domino
    this.setCursorPos(currPos[ROW], currPos[COL]);
    this.removeDomino();
    
    // now try placing a domino vertically
    if( this.placeDomino(DominoPosition.Vertical) ) {
      if( this.gotoNextFree() ) {
        this.play();
      } else if( this.dominoPool.isEmpty() ) {
        // We found a solution!
        System.out.println( "Solution found: \n" );
        System.out.println(this);
      }
    }
    
    // reset the cursor, remove the last placed domino
    this.setCursorPos(currPos[ROW], currPos[COL]);
    this.removeDomino();
  }
  
  /**
   * 
   * +---+---+---+---+---+---+
   * | 1   2   3   4 | 5   6 |
   * +               +---+---+
   * | 6   5   4   3 | 2 | 1 |
   * +---+---+---+---+   +   +
   * | 2   4 | 6   1 | 3 | 5 |
   * +---+---+---+---+---+---+
   * 
   * Build sort of a "raster", 2*rows+1 X 4*cols+1.  Then just fill it in, a layer at a time.
   * 
   */
  public String toString() {
    
    char[][] raster = new char [this.rows * 2 + 1][this.cols * 4 + 1];
    
    for( int i = 0; i < this.rows * 2 + 1; i++ ) {
      for( int j = 0; j < this.cols * 4 + 1; j++ ) {
        raster[i][j] = ' '; // initialize to all blank spaces.
      }
    }
    
    // fill in the outline.
    // top
    for( int i = 0; i < raster[0].length; i++ ) {
      raster[0][i] = '-';
    }
    
    // sides
    for( int i = 0; i < raster.length; i++ ) {
      raster[i][0] = '|';
      raster[i][raster[i].length - 1] = '|';
    }
    
    // bottom
    for( int i = 0; i < raster[raster.length - 1].length; i++ ) {
      raster[raster.length - 1][i] = '-';
    }
    
    // corners
    raster[0][0] = '+';
    raster[0][raster[0].length - 1] = '+';
    raster[raster.length - 1][0] = '+';
    raster[raster.length - 1][raster[raster.length - 1].length - 1] = '+';
    
    // now fill in the numbers.
    for( int i = 0; i < this.rows; i++ ) {
      for( int j = 0; j < this.cols; j++ ) {
        raster[ i*2 + 1 ][ j*4 + 2 ] = (""+this.board[i][j]).charAt(0);
      }
    }
    
    // now we go through the dominoes and draw them
    /*
     * Mapping of board[i][j]:
     * 
     * number:      raster[ i*2 + 1 ][ j*4 + 2 ]
     * left wall:   raster[ i*2 + 1 ][ j*4 ]
     * right wall:  raster[ i*2 + 1 ][ j*4 + 4 ]
     * ceiling:     raster[ i*2 ][ j*4 + 1 ], raster[ i*2 ][ j*4 + 2 ], raster[ i*2 ][ j*4 + 3 ]
     * floor:       raster[ i*2 + 2 ][ j*4 + 1 ], raster[ i*2 + 2 ][ j*4 + 2 ], raster[ i*2 + 2 ][ j*4 + 3 ]
     * upper left:  raster[ i*2 ][ j*4 ]
     * upper right: raster[ i*2 ][ j*4 + 4 ]
     * lower left:  raster[ i*2 + 2 ][ j*4 ]
     * lower right: raster[ i*2 + 2 ][ j*4  + 4 ]
     */
    DominoDescriptor d = null;
    int i = 0, j = 0;  // copies of the loop coords.
    for( int r = 0; r < this.rows; r++ ) {
      for( int c = 0; c < this.cols; c++ ) {
        if( (d = this.dominoes[r][c]) != null ) {
          i = r; j = c;
          if( d.getPosition() == DominoPosition.Horizontal ) {
            // draw a horizontal domino, left side first
            raster[ i*2 + 1 ][ j*4 ] = '|'; // left wall
            raster[ i*2 ][ j*4 + 1 ] = '-';
            raster[ i*2 ][ j*4 + 2 ] = '-';
            raster[ i*2 ][ j*4 + 3 ] = '-'; // ceiling
            raster[ i*2 + 2 ][ j*4 + 1 ] = '-';
            raster[ i*2 + 2 ][ j*4 + 2 ] = '-';
            raster[ i*2 + 2 ][ j*4 + 3 ] = '-'; // floor
            raster[ i*2 ][ j*4 ] = '+'; // upper left corner
            raster[ i*2 + 2 ][ j*4 ] = '+'; // lower left corner;
            if( raster[ i*2 ][ j*4 + 4 ] != '+' ) raster[ i*2 ][ j*4 + 4 ] = '-'; // upper right corner
            if( raster[ i*2 + 2 ][ j*4  + 4 ] != '+' ) raster[ i*2 + 2 ][ j*4  + 4 ] = '-'; // lower right corner
            // now do the left side
            j = j+1;
            raster[ i*2 ][ j*4 + 1 ] = '-';
            raster[ i*2 ][ j*4 + 2 ] = '-';
            raster[ i*2 ][ j*4 + 3 ] = '-'; // ceiling
            raster[ i*2 + 1 ][ j*4 + 4 ] = '|'; // right wall
            raster[ i*2 + 2 ][ j*4 + 1 ] = '-';
            raster[ i*2 + 2 ][ j*4 + 2 ] = '-';
            raster[ i*2 + 2 ][ j*4 + 3 ] = '-'; // floor
            raster[ i*2 ][ j*4 + 4 ] = '+'; // upper right corner
            raster[ i*2 + 2 ][ j*4  + 4 ] = '+'; // lower right corner
          } else {
            // draw a vertical domino, top first
            raster[ i*2 + 1 ][ j*4 ] = '|'; // left wall
            raster[ i*2 ][ j*4 + 1 ] = '-';
            raster[ i*2 ][ j*4 + 2 ] = '-';
            raster[ i*2 ][ j*4 + 3 ] = '-'; // ceiling
            raster[ i*2 + 1 ][ j*4 + 4 ] = '|'; // right wall
            raster[ i*2 ][ j*4 ] = '+'; // upper left corner
            raster[ i*2 ][ j*4 + 4 ] = '+'; // upper right corner
            if( raster[ i*2 + 2 ][ j*4 ] != '+' ) raster[ i*2 + 2 ][ j*4 ] = '|'; // lower left corner;
            if( raster[ i*2 + 2 ][ j*4  + 4 ] != '+' ) raster[ i*2 + 2 ][ j*4  + 4 ] = '|'; // lower right corner
            // now do the bottom
            i = i+1;
            raster[ i*2 + 1 ][ j*4 ] = '|'; // left wall
            raster[ i*2 + 1 ][ j*4 + 4 ] = '|'; // right wall
            raster[ i*2 + 2 ][ j*4 + 1 ] = '-';
            raster[ i*2 + 2 ][ j*4 + 2 ] = '-';
            raster[ i*2 + 2 ][ j*4 + 3 ] = '-'; // floor
            raster[ i*2 + 2 ][ j*4 ] = '+'; // lower left corner;
            raster[ i*2 + 2 ][ j*4  + 4 ] = '+'; // lower right corner
          }
        }
      }
    }
    
    StringBuilder sb = new StringBuilder();
    
    for( int k = 0; k < raster.length; k++ ) {
      sb.append(new String( raster[k]) ).append("\n");
    }
    
    return sb.toString();
  }
  
}
