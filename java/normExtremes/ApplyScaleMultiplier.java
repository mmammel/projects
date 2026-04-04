import java.io.BufferedReader;
import java.io.FileReader;

public class ApplyScaleMultiplier
{
  public static void main( String [] args )
  {
    String fname = args[0];
    String mult = args[1];
    BufferedReader input_reader = null;
    String input_str = "";

    Integer [] result = null;
    double multiplier = Double.parseDouble( mult );

    try
    {
      input_reader = new BufferedReader( new FileReader ( fname ) );


      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "${QUIT}" ) )
        {
          break;
        }
        else
        {
          // Process Input
          String [] cuts = input_str.split("~");
          result = new Integer [ cuts.length ];
          for( int i = 0; i < result.length; i++ ) {
            result[i] = Double.valueOf( Double.parseDouble( cuts[i] ) * multiplier ).intValue();;
          }

        }

      }

      for( int i = 0; i < result.length; i++ ) {
        if( i > 0 ) System.out.print( "~" );
        System.out.print( result[i] );
      }
      System.out.println("");

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

