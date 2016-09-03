import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.*;

class TryARegEx
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
      //pattern = "^([^\\.]+?)\\.(?:(Q[0-9][0-9][0-9])|(Q[0-9][0-9][0-9])-(Q[0-9][0-9][0-9]))$";
      //pattern = "^(.*?):(.*?):(.*)$";
      //pattern = "(?i)^.*?<img.*?src=\\s*['\"](.*?)['\"].*$";
      pattern = "o:(.*)$";
    }
    else
    {
      pattern = args[0];
    }
    
    thePattern = Pattern.compile( pattern );

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
          System.out.println( "Testing: " + input_str + " ..." );
          tempMatcher = thePattern.matcher( input_str );

          if( tempMatcher.matches() )
          {
            System.out.println( "Entire String matches Pattern!" );
            printGroups( tempMatcher );
          }
          else if( tempMatcher.find() )
          {
            System.out.println( "Pattern found in String!" );
            printGroups( tempMatcher );
          }
          else
          {
            System.out.println( "Pattern not found in String." );
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

  private static void printGroups( Matcher m )
  {
    for( int i = 0; i < m.groupCount(); i++ )
    {
      System.out.println( "Group " + (i+1) + ": " + m.group(i+1) );
    }
  }

}
