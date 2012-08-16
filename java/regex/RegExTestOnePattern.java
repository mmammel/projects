import java.util.regex.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class RegExTestOnePattern
{
  Map<Pattern,String> replacePairs = new HashMap<Pattern,String>();
  private static final String PATTERN_PROP = "location.abbreviation.pattern";
  private static final String REPLACE_PROP = "location.abbreviation.replace";

  public RegExTestOnePattern()
  {
    try
    {
      URL config = this.getClass().getResource( "pattern.properties" );
      Properties props = new Properties();
      props.load( config.openStream() );

      int propNum = 0;
      String tempPattern = null, tempReplace = null;

      while( (tempPattern = props.getProperty( PATTERN_PROP + propNum )) != null )
      {
        tempReplace = props.getProperty( REPLACE_PROP + propNum++ );
        System.out.println( "[" + tempPattern + "," + tempReplace + "]" );
        this.replacePairs.put(Pattern.compile( tempPattern ), tempReplace );
      }
    }
    catch( Exception e )
    {
      // log and continue
      System.out.println( "Caught an exception: " + e.toString() );
    }
  }

  public String process( String str )
  {
    String retVal = str;
    Pattern tempPattern = null;

    for( Iterator<Pattern> iter = this.replacePairs.keySet().iterator(); iter.hasNext(); )
    {
      tempPattern = iter.next();
      retVal = tempPattern.matcher( retVal ).replaceAll( this.replacePairs.get(tempPattern) );
    }

    return retVal;
  }


  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    String input_str = "";
    RegExTestOnePattern RET = new RegExTestOnePattern();

    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( "Welcome to the pattern replacer." );

      System.out.print("$ ");

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "quit" ) )
        {
          break;
        }
        else
        {
          System.out.println( input_str + " -> " + RET.process(input_str));
        }

        System.out.print( "\n$ " );
      }

      System.out.println( "Goodbye." );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }

}
