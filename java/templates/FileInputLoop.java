import java.io.BufferedReader;
import java.io.FileReader;

public class ${CLASS_NAME}
{
  public static void main( String [] args )
  {
    String fname = args[0];
    BufferedReader input_reader = null;
    String input_str = "";

    try
    {
      input_reader = new BufferedReader( new FileReader ( fname ) );

      System.out.println( "${HELLO}" );

      System.out.print("${PROMPT}");

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "${QUIT}" ) )
        {
          break;
        }
        else
        {
          // Process Input

        }

        System.out.print( "\n${PROMPT}" );
      }

      System.out.println( "${GOODBYE}" );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

