import java.io.BufferedReader;
import java.io.FileReader;

public class NormExtremes
{
  public static void main( String [] args )
  {
    String fname = args[0];
    BufferedReader input_reader = null;
    String input_str = "";

    int num = 0, min = 0, tempMin = 0, max = 0, tempMax = 0;;

    try
    {
      input_reader = new BufferedReader( new FileReader ( fname ) );

      while( (input_str = input_reader.readLine()) != null )
      {
        tempMin = 9999;
        tempMax = -9999;

        if( input_str.equalsIgnoreCase( "quit" ) )
        {
          break;
        }
        else
        {
          // Process Input
          String [] weights = input_str.split(",");
          try {
            for( String weight : weights ) {
              num = Integer.parseInt( weight );
              if( num < tempMin ) tempMin = num;
              if( num > tempMax ) tempMax = num;
            }

            min = min + tempMin;
            max = max + tempMax;
          } catch( Exception e ) {
            System.err.println( "Bogus input! -> " + input_str );
            throw new RuntimeException( "Bad input, bailing out." );
          } 

        }
      }

      System.out.println( "Min: " + min + ", Max: " + max );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

