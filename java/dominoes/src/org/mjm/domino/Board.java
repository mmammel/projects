package org.mjm.domino;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Board {

  private int [][] board;

  public Board( String filename ) throws IOException
  {
    this();
    BufferedReader reader = null;
    String readStr = null;
    int readVal = 0;
    char readChar = 0;

    List<List<Integer>> tempBoard = new ArrayList<List<Integer>>(); // so we are dimension agnostic
    List<Integer> tempRow = null;

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
            throw new IOException( "Bad number in row " + row + ": " + readStr );
          }
          else
          {
            tempRow.add( readVal );
          }
        }
      }
    }

    this.board = new int [ tempBoard.size() ][ tempBoard.get(0).size() ];

    for( int i = 0; i < tempBoard.size(); i++ ) {
      tempRow = tempBoard.get(i);
      for( int j = 0; j < tempRow.size(); j++ ) {
        this.board[i][j] = tempRow.get(j);
      }
    }
  }
}
