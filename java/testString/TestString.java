import java.util.*;

public class TestString
{
  public static final String TEST = "A#$%@&Token B#$%@&Token C#$%@&Token D#$%@&Token E";
  public static final String DELIM = "#$%@";

  public synchronized static String process()
  {
    StringTokenizer strtok = new StringTokenizer( TEST, DELIM );
    String temp = null;
    String result = new String();
    while( strtok.hasMoreTokens() )
    {
      temp = strtok.nextToken();
      result += new StringBuffer(temp).reverse().toString() + DELIM;
    }

    return result.substring( 0, (result.length() - DELIM.length()) ); 
  }

  public static void main( String [] args )
  {
    System.out.println( TestString.TEST );
    System.out.println( TestString.process() );
  }
}
