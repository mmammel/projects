import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class EscapeChars
{

  private static final Pattern charsToEscape = Pattern.compile("([-+!(){}\\[\\]^\\\"~*?:\\\\])");

  public String escape( String str )
  {
    return charsToEscape.matcher(str).replaceAll("\\\\$0");
  }

  public static void main( String [] args )
  {
    EscapeChars EC = new EscapeChars();
    BufferedReader input_reader = null;
    String input_str = "";

    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( "Buenos Dias" );

      System.out.print("$ ");

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "quit" ) )
        {
          break;
        }
        else
        {
          System.out.println( EC.escape( input_str ) );
        }

        System.out.print( "\n$ " );
      }

      System.out.println( "Adios amigo" );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

