import java.io.BufferedReader;
import java.io.FileReader;

public class StringNumberTotaler
{
  public static void main( String [] args )
  {
    String fname = args[0];
    BufferedReader input_reader = null;
    String input_str = "";
    String tempNumStr = null;
    int tempNum = 0;
    int total = 0;

    try
    {
      input_reader = new BufferedReader( new FileReader ( fname ) );

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.trim().length() > 0 ) {
          tempNumStr = input_str.replaceAll( "^.*?([0-9]+).*?$", "$1" );
          if( tempNumStr != null && tempNumStr.matches("^[0-9]+$") ) {
            try {
              tempNum = Integer.parseInt( tempNumStr );
            } catch( NumberFormatException nfe ) {
              tempNum = 0;
            }

            total += tempNum;
          }
        }

      }

      System.out.println( "Total: " + total );
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

