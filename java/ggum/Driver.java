import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class Driver
{
  public static void main( String [] args )
  {
    String paramFileName = args[0];
    String responseFileName = args[1];

    ResponseDescriptor tempResponse = null;
    ItemDescriptor tempItem = null;
    List<ResponseDescriptor> people = new ArrayList<ResponseDescriptor>();
    List<ItemDescriptor> items = new ArrayList<ItemDescriptor>();
    int numCats = 0;

    BufferedReader input_reader = null;
    String [] input_array = null;
    String input_str = "";

    // read the params.
    try
    {
      input_reader = new BufferedReader( new FileReader ( paramFileName ) );

      while( (input_str = input_reader.readLine()) != null )
      {
        input_array = input_str.split(",");
        tempItem = new ItemDescriptor();
        tempItem.setId( input_array[0] );
        tempItem.setDelta( Double.parseDouble( input_array[1] ) );
        tempItem.setAlpha( Double.parseDouble( input_array[2] ) );
        numCats = Integer.parseInt( input_array[3] );
        tempItem.setNumCategories( numCats );
        for( int i = 4; i < (4+numCats); i++ ) {
          tempItem.addTau( Double.parseDouble( input_array[i] ) );
        }

        items.add( tempItem );
      }

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    } finally {
      try {
        input_reader.close();
      } catch( IOException ioe ) {
      }
    }

    // read the responses
    try
    {
      input_reader = new BufferedReader( new FileReader ( responseFileName ) );

      while( (input_str = input_reader.readLine()) != null )
      {
        input_array = input_str.split(",");
        tempResponse = new ResponseDescriptor(input_array[0]);
        for( int i = 1; i < input_array.length; i++ ) {
          if( input_array[i].equals("NA") ) {
            tempResponse.addResponse( -1 );
          } else {
            tempResponse.addResponse( Integer.parseInt( input_array[i] ) );
          }
        }

        people.add( tempResponse );
      }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    } finally {
      try {
        input_reader.close();
      } catch( IOException ioe ) {
      }
    }


    ScoreProcessor processor = new ScoreProcessor( items );
    List<ThetaEstimate> estimates = processor.process( people );
    for( ThetaEstimate estimate : estimates ) {
      System.out.println( estimate );
    }
  }
}

