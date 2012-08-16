import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.*;
import java.net.URL;
import java.util.Properties;
import java.io.FileReader;

public class SplitTest
{
  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    String input_str = "";
    String [] splitResult;
    Properties mProps = null;

    try
    {
      URL config = SplitTest.class.getResource( "properties.properties" );
      mProps = new Properties();
      mProps.load( config.openStream() );
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
    }

    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( "Hola amigo!!" );

      System.out.println( "Tests from Properties file:" );

      printResultArray( mProps.getProperty( "test.string" ).split( "(?<!\\\\),", 2 ) );

      System.out.println( "Tests from regular file:" );
      BufferedReader fileReader = new BufferedReader( new FileReader( "testStrings.txt" ) );
      while( (input_str = fileReader.readLine()) != null )
      {
        printResultArray( input_str.split( "(?<!\\\\),", 2 ) );
      }

      System.out.print("$> ");

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "quit" ) )
        {
          break;
        }
        else
        {
          printResultArray( input_str.split( "(?<!\\\\),", 2 ) );
        }

        System.out.print( "\n$> " );
      }

      System.out.println( "Adios!" );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }

  public static String unescape( String str )
  {
    return Pattern.compile( "\\\\," ).matcher( str ).replaceAll( "," );
  }

  public static void printResultArray( String [] array )
  {
    System.out.println("");
    for( int i = 0; i < array.length; i++ )
    {
      System.out.print( "[" + unescape(array[i]) + "]" );
    }
    System.out.println("");
  }

}