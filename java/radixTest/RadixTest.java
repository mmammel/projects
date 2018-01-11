import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RadixTest
{
  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    String input_str = "";

    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( "Welcome to the radix tester - enter <number representation> <radix in decimal>" );

      System.out.print("> ");

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "quit" ) )
        {
          break;
        }
        else
        {
          // Process Input
          try {
            String [] splits = input_str.split("\\s+");
            int val = Integer.parseInt( splits[0], Integer.parseInt( splits[1] ) );
            System.out.println("Value: " + val );
          } catch( Exception e ) {
            System.out.println( "Bad input! Enter <number> <radix in decimal>, e.g. 101 2 or FF 16" );
          }
        }

        System.out.print( "\n> " );
      }

      System.out.println( "Seeya later alligator" );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

