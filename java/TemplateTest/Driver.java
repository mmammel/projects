import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Properties;

public class Driver
{
  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    String input_str = "";

    try
    {
	  PropertiesTemplateTest PTT = new PropertiesTemplateTest();
      Properties p = PTT.getData( "foobar" );
File testfile = new File( "foobar.properties" );

System.out.println( testfile.toURL().toString() );

      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( "Hola!  Just try entering properties to see what you get." );

      System.out.print("$ ");

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "quit" ) )
        {
          break;
        }
        else
        {
          // Process Input
          p = PTT.getData( "foobar" );
		  System.out.println( p.getProperty( input_str ) );
        }

        System.out.print( "\n$ " );
      }

      System.out.println( "Latah man.." );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

