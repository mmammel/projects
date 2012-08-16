import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DoubleFormatTest
{
  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    String input_str = "";

    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( "Hola! Enter decimal numbers, or \"quit\" to exit." );

      System.out.print("Enter a decimal: ");
      double val = 0.0d;

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "quit" ) )
        {
          break;
        }
        else
        {
           try
           {
             val = Double.parseDouble( input_str );
             System.out.println( "  Using %#04.2f -> " + String.format( "%#04.2f", val ) );
           }
           catch( Exception e )
           {
             System.out.println( "Bogus input!  try again." );
           }
        }

        System.out.print( "\nEnter a decimal: " );
      }

      System.out.println( "Adios!" );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

