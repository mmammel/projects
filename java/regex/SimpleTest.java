import java.io.*;
import java.util.regex.*;

public class SimpleTest
{
  //public static Pattern PATTERN = Pattern.compile("(?si)<form[^>]*?id=\"fadv-(.*?)\"[^>]+?>|(?:<([A-z0-9]+?) ([^>]*?id=\"fadv-(.*?)\"[^>]*?)(?:/>|>.*?</\\2>))");
  //public static Pattern PATTERN = Pattern.compile("<.*?>|[\\r\\n]");
  //public static Pattern PATTERN = Pattern.compile("^\\$\\{([^~]+)~([^~]+)~([^(}]+)");
  //public static Pattern PATTERN = Pattern.compile("^[A-Za-z_0-9]+%2[Cc].*$");
  //public static Pattern PATTERN = Pattern.compile("^(.*?):(.*?):(.*)$");
  //public static Pattern PATTERN = Pattern.compile("^(?:(?:[-a-zA-Z0-9!#$%&'*+/=?^_`{|}~])+\\.)*[-a-zA-Z0-9!#$%&'*+/=?^_`{|}~]+@\\w(?:(?:-|\\w)*\\w)*\\.(?:\\w(?:(?:-|\\w)*\\w)*\\.)*\\w{2,4}$");
  //public static Pattern PATTERN = Pattern.compile("^(?:[-0-9A-z_() ]+,[0-9]+)+$");
  //public static Pattern PATTERN = Pattern.compile("\\(\\?(.*?)\\?\\)");
  //public static Pattern PATTERN = Pattern.compile("^[0-9]+\\/[0-9]+$");
  public static Pattern PATTERN = Pattern.compile("\\$\\$SN?'(.*?)'");

  public static void main( String [] args )
  {
    BufferedReader reader = null;

    try
    {
      String tmp = null;
      StringBuffer buff = new StringBuffer();

      reader = new BufferedReader( new FileReader( args[0] ) );
      while( (tmp = reader.readLine()) != null )
      {
        buff.append( tmp ).append( "\n" );
      }

      Matcher m = PATTERN.matcher( buff.toString() );

      while( m.find() )
      {
        System.out.println( "Found Match: [" + m.group(0) + "]" );
        for( int i = 1; i <= m.groupCount(); i++ )
        {
          System.out.println( "Group " + i + ": [" + m.group(i) + "]" );
        }
      }

      System.out.println( "After replace all with FOOBAR:\n\n" + buff.toString().replaceAll("(?i)\\$\\{OPTOUT_URL\\}", "FOOBAR") );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
    }
  }
}
