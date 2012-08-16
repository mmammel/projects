import java.io.*;

public class TableSeating implements TupleProcessor
{
  private int sets;
  private int elements;
  private NTuple tupleHandler;
  private int adjacentCount;
  private int nonAdjacentCount;


  public TableSeating( int x, int y )
  {
    adjacentCount = 0;
    nonAdjacentCount = 0;
    this.sets = x;
    this.elements = y;
    tupleHandler = new NTuple();
    tupleHandler.setProcessor( this );
  }

  public void process() throws Exception
  {
    Integer [] table = new Integer [ this.sets * this.elements ];
    
    for( int i = 0; i < table.length; i++ )
    {
      table[i] = new Integer( i );
    }
    
    tupleHandler.processTuples( table, this.elements );
  }
  
  public void processTuple( Object [] tuple )
  {
    boolean adj = false;
    int idx = -1;
    int prev = -1;
    
    if( ((Integer)tuple[0]).intValue() == 0 &&
        ((Integer)tuple[tuple.length - 1]).intValue() == (this.sets * this.elements - 1) )
    {
      adj = true;
    }
    else
    {
      for( int i = 0; i < tuple.length; i++ )
      {
        idx = ((Integer)tuple[i]).intValue();

        if( i == 0 )
        {
          prev = idx;
        }
        else
        {
          if( idx == (prev + 1) )
          {
            adj = true;
            break;
          }
          else
          {
            prev = idx;
          }
        }
      }
    }
    
    if( adj )
    {
      this.adjacentCount++;
    }
    else
    {
      this.nonAdjacentCount++;
    }
  }
  
  public int getAdjacentCount()
  {
    return this.adjacentCount;
  }
  
  public int getNonAdjacentCount()
  {
    return this.nonAdjacentCount;
  }
      
  public static void main( String [] args )
  {
    String [] pair;
    int sets,elements;
    TableSeating TS;
    String tempStr;

    try
    {
      BufferedReader input = new BufferedReader( new InputStreamReader( System.in ) );

      System.out.print( "Enter a pair (sets,elements): " );
      while( (tempStr = input.readLine()) != null )
      {
        if( tempStr.equalsIgnoreCase("quit") )
        {
          System.out.println( "Goodbye." );
          break;
        }

        pair = tempStr.split(",");
        sets = Integer.parseInt( pair[0] );
        elements = Integer.parseInt( pair[1] );
        TS = new TableSeating( sets, elements);
        TS.process();
        System.out.println( "(" + sets + "," + elements + "):" );
        System.out.println( "Non-adjacent: " + TS.getNonAdjacentCount() );
        System.out.println( "Adjacent: " + TS.getAdjacentCount() );

        System.out.print( "Enter a pair (sets,elements): " );
      }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }

  }
  
}
