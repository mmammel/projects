package org.mjm.sudoku;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Board
{

   /*-----------*-----------*-----------*
    |           |           |           |
    |  0  1  2  |  3  4  5  |  6  7  8  |
    |           |           |           |
    |  9  10 11 |  12 13 14 |  15 16 17 |
    |           |           |           |
    |  18 19 20 |  21 22 23 |  24 25 26 |
    |           |           |           |
    *-----------*-----------*-----------*
    |           |           |           |
    |  27 28 29 |  30 31 32 |  33 34 35 |
    |           |           |           |
    |  36 37 38 |  39 40 41 |  42 43 44 |
    |           |           |           |
    |  45 46 47 |  48 49 50 |  51 52 53 |
    |           |           |           |
    *-----------*-----------*-----------*
    |           |           |           |
    |  54 55 56 |  57 58 59 |  60 61 62 |
    |           |           |           |
    |  63 64 65 |  66 67 68 |  69 70 71 |
    |           |           |           |
    |  72 73 74 |  75 76 77 |  78 79 80 |
    |           |           |           |
    *-----------*-----------*-----------*/


  private static final int [][] ROW_INDICES = { { 0,1,2,3,4,5,6,7,8 },
                                                { 9,10,11,12,13,14,15,16,17},
                                                { 18,19,20,21,22,23,24,25,26},
                                                { 27,28,29,30,31,32,33,34,35},
                                                { 36,37,38,39,40,41,42,43,44},
                                                { 45,46,47,48,49,50,51,52,53},
                                                { 54,55,56,57,58,59,60,61,62},
                                                { 63,64,65,66,67,68,69,70,71},
                                                { 72,73,74,75,76,77,78,79,80}
                                              };

  private static final int [][] COLUMN_INDICES = { { 0,9,18,27,36,45,54,63,72},
                                                   { 1,10,19,28,37,46,55,64,73},
                                                   { 2,11,20,29,38,47,56,65,74},
                                                   { 3,12,21,30,39,48,57,66,75},
                                                   { 4,13,22,31,40,49,58,67,76},
                                                   { 5,14,23,32,41,50,59,68,77},
                                                   { 6,15,24,33,42,51,60,69,78},
                                                   { 7,16,25,34,43,52,61,70,79},
                                                   { 8,17,26,35,44,53,62,71,80}
                                                 };

  private static final int [][] BOX_INDICES = { { 0,1,2,9,10,11,18,19,20},
                                                { 3,4,5,12,13,14,21,22,23},
                                                { 6,7,8,15,16,17,24,25,26},
                                                { 27,28,29,36,37,38,45,46,47},
                                                { 30,31,32,39,40,41,48,49,50},
                                                { 33,34,35,42,43,44,51,52,53},
                                                { 54,55,56,63,64,65,72,73,74},
                                                { 57,58,59,66,67,68,75,76,77},
                                                { 60,61,62,69,70,71,78,79,80}
                                              };

  private Cell [] mBoard;
  private Row [] mRows;
  private Column [] mColumns;
  private Box [] mBoxes;
  private Map<Cell,Row> mCellToRowMap;
  private Map<Cell,Column> mCellToColumnMap;
  private Map<Cell,Box> mCellToBoxMap;

  public Board()
  {
    this.mBoard = new Cell [81];

    for( int i = 0; i < 81; i++ )
    {
      this.mBoard[i] = new Cell(i);
    }

    this.mRows = new Row [9];
    this.mColumns = new Column [9];
    this.mBoxes = new Box [9];
    this.mCellToRowMap = new HashMap<Cell,Row>();
    this.mCellToColumnMap = new HashMap<Cell,Column>();
    this.mCellToBoxMap = new HashMap<Cell,Box>();

    for( int j = 0; j < 9; j++ )
    {
      this.mRows[j] = new Row( ROW_INDICES[j], this.mBoard );
      this.mColumns[j] = new Column( COLUMN_INDICES[j], this.mBoard );
      this.mBoxes[j] = new Box( BOX_INDICES[j], this.mBoard );

      for( int k = 0; k < 9; k++ )
      {
        this.mCellToRowMap.put( this.mRows[j].getCells()[k], this.mRows[j] );
        this.mCellToColumnMap.put( this.mColumns[j].getCells()[k], this.mColumns[j] );
        this.mCellToBoxMap.put( this.mBoxes[j].getCells()[k], this.mBoxes[j] );
      }
    }
  }

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
              mRows[row].getCells()[i].setValue(Value.fromInt(readVal));
            }
          }
        }
      }

      row++;
    }
  }

  /*
    **=====*=====*=====**=====*=====*=====**=====*=====*=====**
    || 123 | 123 | 123 || 123 | 123 | 123 || 123 | 123 | 123 ||
    || 456 | 456 | 456 || 456 | 456 | 456 || 456 | 456 | 456 ||
    || 789 | 789 | 789 || 789 | 789 | 789 || 789 | 789 | 789 ||
    **-----*-----*-----**-----*-----*-----**-----*-----*-----**
    || 123 | 123 | 123 || 123 | 123 | 123 || 123 | 123 | 123 ||
    || 456 | 456 | 456 || 456 | 456 | 456 || 456 | 456 | 456 ||
    || 789 | 789 | 789 || 789 | 789 | 789 || 789 | 789 | 789 ||
    **-----*-----*-----**-----*-----*-----**-----*-----*-----**
    || 123 | 123 | 123 || 123 | 123 | 123 || 123 | 123 | 123 ||
    || 456 | 456 | 456 || 456 | 456 | 456 || 456 | 456 | 456 ||
    || 789 | 789 | 789 || 789 | 789 | 789 || 789 | 789 | 789 ||
    **=====*=====*=====**=====*=====*=====**=====*=====*=====**
    || 123 | 123 | 123 || 123 | 123 | 123 || 123 | 123 | 123 ||
    || 456 | 456 | 456 || 456 | 456 | 456 || 456 | 456 | 456 ||
    || 789 | 789 | 789 || 789 | 789 | 789 || 789 | 789 | 789 ||
    **-----*-----*-----**-----*-----*-----**-----*-----*-----**
    || 123 | 123 | 123 || 123 | 123 | 123 || 123 | 123 | 123 ||
    || 456 | 456 | 456 || 456 | 456 | 456 || 456 | 456 | 456 ||
    || 789 | 789 | 789 || 789 | 789 | 789 || 789 | 789 | 789 ||
    **-----*-----*-----**-----*-----*-----**-----*-----*-----**
    || 123 | 123 | 123 || 123 | 123 | 123 || 123 | 123 | 123 ||
    || 456 | 456 | 456 || 456 | 456 | 456 || 456 | 456 | 456 ||
    || 789 | 789 | 789 || 789 | 789 | 789 || 789 | 789 | 789 ||
    **=====*=====*=====**=====*=====*=====**=====*=====*=====**
    || 123 | 123 | 123 || 123 | -23 | 123 || 123 | 123 | 123 ||
    || 456 | 456 | 456 || 456 | -5- | 456 || 456 | 456 | 456 ||
    || 789 | 789 | 789 || 789 | 78- | 789 || 789 | 789 | 789 ||
    **-----*-----*-----**-----*-----*-----**-----*-----*-----**
    || 123 | 123 | 123 || 123 | 123 | 123 || 123 | 123 | 123 ||
    || 456 | 456 | 456 || 456 | 456 | 456 || 456 | 456 | 456 ||
    || 789 | 789 | 789 || 789 | 789 | 789 || 789 | 789 | 789 ||
    **-----*-----*-----**-----*-----*-----**-----*-----*-----**
    || 123 | 123 | 123 || 123 | 123 | 123 || 123 | 123 | 123 ||
    || 456 | 456 | 456 || 456 | 456 | 456 || 456 | 456 | 456 ||
    || 789 | 789 | 789 || 789 | 789 | 789 || 789 | 789 | 789 ||
    **=====*=====*=====**=====*=====*=====**=====*=====*=====**

  */
  public String toString()
  {
    String tempRow1, tempRow2, tempRow3;
    String divider = "";
    StringBuilder sb = new StringBuilder();
    Row tempRow = null;
    Cell tempCell = null;

    sb.append("++=====+=====+=====++=====+=====+=====++=====+=====+=====++\n");

    for( int i = 0; i < 9; i++ )
    {
      tempRow1 = tempRow2 = tempRow3 = "|| ";
      tempRow = this.mRows[i];
      for( int j = 0; j < 9; j++ )
      {
        tempCell = tempRow.getCells()[j];
        tempRow1 += tempCell.isPossible(Value.ONE) ? "1" : "-";
        tempRow1 += tempCell.isPossible(Value.TWO) ? "2" : "-";
        tempRow1 += tempCell.isPossible(Value.THREE) ? "3" : "-";
        tempRow2 += tempCell.isPossible(Value.FOUR) ? "4" : "-";
        tempRow2 += tempCell.isPossible(Value.FIVE) ? "5" : "-";
        tempRow2 += tempCell.isPossible(Value.SIX) ? "6" : "-";
        tempRow3 += tempCell.isPossible(Value.SEVEN) ? "7" : "-";
        tempRow3 += tempCell.isPossible(Value.EIGHT) ? "8" : "-";
        tempRow3 += tempCell.isPossible(Value.NINE) ? "9" : "-";

        if( (j+1) % 3 == 0 )
        {
          divider = "||";
        }
        else
        {
          divider = "|";
        }

        if( j < 8 ) divider += " ";

        tempRow1 += " " + divider;
        tempRow2 += " " + divider;
        tempRow3 += " " + divider;
      }

      sb.append( tempRow1 ).append("\n");
      sb.append( tempRow2 ).append("\n");
      sb.append( tempRow3 ).append("\n");

      if( (i+1) % 3 == 0 )
      {
       sb.append("++=====+=====+=====++=====+=====+=====++=====+=====+=====++\n");
      }
      else
      {
        sb.append("++-----+-----+-----++-----+-----+-----++-----+-----+-----++\n");
      }
    }

    return sb.toString();
  }

  public String toSimpleString()
  {
    StringBuilder sb = new StringBuilder();
    Row tempRow;
    Cell tempCell;

    sb.append(" +-------+-------+-------+\n");

    for( int i = 0; i < 9; i++ )
    {
      tempRow = this.getRows()[i];
      for( int j = 0; j < 9; j++ )
      {
        tempCell = tempRow.getCells()[j];
        sb.append( ((j%3 == 0) ? " | " : " ") + tempCell.getPrintableValue() );
      }

      sb.append( " |\n" );
      if( i%3 == 2 ) sb.append(" +-------+-------+-------+\n");
    }

    return sb.toString();
  }

  public Row getRowForCell( Cell cell )
  {
    return this.mCellToRowMap.get(cell);
  }

  public Column getColumnForCell( Cell cell )
  {
    return this.mCellToColumnMap.get(cell);
  }

  public Box getBoxForCell( Cell cell )
  {
    return this.mCellToBoxMap.get(cell);
  }

  /**
    if the list of cells are in the same box, return it.
    otherwise return null.
   */
  public Box getBoxForCells( List<Cell> cells )
  {
    Box temp = null;
    Box retVal = null;

    for( Cell cell : cells )
    {
      temp = this.getBoxForCell(cell);
      if( retVal != null && !retVal.equals(temp) )
      {
        retVal = null;
        break;
      }
      else
      {
        retVal = temp;
      }
    }

    return retVal;
  }

  public Cell [] getCells()
  {
    return mBoard;
  }

  public Row [] getRows()
  {
    return this.mRows;
  }

  public Column [] getColumns()
  {
    return this.mColumns;
  }

  public Box [] getBoxes()
  {
    return this.mBoxes;
  }

  public static void main( String [] args )
  {
    try
    {
      Board b = new Board("evil.dat");
      System.out.println( "The Board:\n\n" + b );
    }
    catch( Exception e )
    {
      System.out.println( "Caught an Exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}
