import java.util.regex.*;
import java.io.*;

public class WeirdRegexTest
{
  //public static final String WEIRD_REGEX = "^[^\\x80-\\xFF]*\\([\\x80-\\xFF]\\)[^\\x80-\\xFF]*$";
  public static final String WEIRD_REGEX = "([^\\x00-\\x7F])";
  public static final Pattern WEIRD_PATTERN = Pattern.compile(WEIRD_REGEX);
  public static Matcher WEIRD_MATCHER = WEIRD_PATTERN.matcher("");
  
  public static void main( String [] args )
  {
    String inputStr = null;
    BufferedReader reader = null;
    
    try
    { 
      reader = new BufferedReader( new FileReader( "SEQUOIA_Descriptions.txt" ) );
      char match = 0;
      int idx = 0;
      while( (inputStr = reader.readLine()) != null )
      {
        inputStr = inputStr.trim();

        //System.out.println( "Checking line: '" + inputStr + "'" ); 

        WEIRD_MATCHER.reset( inputStr );
        idx = 0;
        
        if( WEIRD_MATCHER.find() )
        {
          System.out.println( "Found a match in " + inputStr );
          while( WEIRD_MATCHER.find(idx) )
          {
            match = WEIRD_MATCHER.group().charAt(0);
            idx = WEIRD_MATCHER.start();
            inputStr = WEIRD_MATCHER.replaceFirst( "&#" + (int)match + ";" );
            System.out.println( "Found a match at index " + idx ); 
            WEIRD_MATCHER.reset( inputStr );
          }
          
          System.out.println( "End result: " + inputStr );
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