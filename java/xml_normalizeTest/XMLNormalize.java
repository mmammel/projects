import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.jfree.xml.writer.XMLWriterSupport;

public class XMLNormalize
{
  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    String input_str = "";

    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( "Hola." );

      System.out.print("$ ");

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "quit" ) )
        {
          break;
        }
        else
        {

          System.out.println( XMLWriterSupport.normalize(input_str) );

        }

        System.out.print( "\n$ " );
      }

      System.out.println( "Adios." );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

