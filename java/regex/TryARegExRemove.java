import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.*;

class TryARegExRemove
{
  public static void main( String [] args )
  {
    String pattern = null;
    Matcher tempMatcher = null;
    Pattern thePattern = null;
    BufferedReader input_reader = null;
    String input_str = "";

    if( args.length != 1 )
    {
      System.out.println( "Usage: java TryARegExRemove <pattern>" );
      System.exit(1);
    }
    else
    {
      pattern = args[0];
      thePattern = Pattern.compile( pattern );
    }

    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( "Hello, enter strings to match against: " + pattern );

      System.out.print("$ ");

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "quit" ) )
        {
          break;
        }
        else
        {
          tempMatcher = thePattern.matcher( input_str );

          if( tempMatcher.matches() )
          {
            System.out.println( "Entire String matches Pattern: [" + tempMatcher.replaceAll("") + "]" );
          }
          else if( tempMatcher.find() )
          {
            System.out.println( "Pattern found in String: [" + tempMatcher.replaceAll("") + "]");
          }
          else
          {
            System.out.println( "Pattern not found in String: [" + tempMatcher.replaceAll("") + "]" );
          }
        }

        System.out.print( "\n$ " );
      }

      System.out.println( "Seeya Later." );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }


}
