import java.util.regex.*;
import java.io.*;

public class RegexTest
{
  public static final String DEST_REGEX = "^D\\w+\\w*$";
  public static final Pattern DEST_PATTERN = Pattern.compile(DEST_REGEX);
  public static Matcher DEST_MATCHER = DEST_PATTERN.matcher("");
  
  public static final String SOURCE_REGEX = "^S\\w+\\w*$";
  public static final Pattern SOURCE_PATTERN = Pattern.compile(SOURCE_REGEX);
  public static Matcher SOURCE_MATCHER = SOURCE_PATTERN.matcher("");
  
  public static final String STRING_REGEX = "^'[a-zA-Z_0-9 ]*'$";
  public static final Pattern STRING_PATTERN = Pattern.compile(STRING_REGEX);
  public static Matcher STRING_MATCHER = STRING_PATTERN.matcher("");
  
  public static final String VAR_REGEX = "^\\$\\w+\\w*$";
  public static final Pattern VAR_PATTERN = Pattern.compile(VAR_REGEX);
  public static Matcher VAR_MATCHER = VAR_PATTERN.matcher("");
  
  public static final String NUM_REGEX = "^[1-9]+[0-9]*|0|\\.[0-9]+|[0-9]+\\.[0-9]+$";
  public static final Pattern NUM_PATTERN = Pattern.compile(NUM_REGEX);
  public static Matcher NUM_MATCHER = NUM_PATTERN.matcher("");
  
  public static void main( String [] args )
  {
    String inputStr = null;
    BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );
    
    try
    { 
      while( (inputStr = reader.readLine()) != null )
      {
        inputStr = inputStr.trim();

        if( DEST_MATCHER.reset( inputStr ).matches() )
        {
          System.out.println( "Destination field!\n" );
        }
        else if( SOURCE_MATCHER.reset( inputStr ).matches() )
        {
          System.out.println( "Source field!\n" );
        }
        else if( STRING_MATCHER.reset( inputStr ).matches() )
        {
          System.out.println( "String literal!\n" );
        }
        else if( VAR_MATCHER.reset( inputStr ).matches() )
        {
          System.out.println( "Variable!\n" );
        }
        else if( NUM_MATCHER.reset( inputStr ).matches() )
        {
          System.out.println( "Number!\n" );
        }
        else
        {
          System.out.println( "Huh???\n" );
        }
      }
    }
    catch( Exception ioe )
    {
      System.out.println( "Exception: " + ioe.toString() );
      ioe.printStackTrace();
    }
  
  }
}