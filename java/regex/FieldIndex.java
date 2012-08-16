import java.util.regex.*;

public class FieldIndex
{
  private static Pattern indexPattern = Pattern.compile("^([0-9]*?)\\.(.*)");


  public static void main( String [] args )
  {
    Matcher m = indexPattern.matcher( args[0] );
    System.out.println( "It " + (m.matches() ? "matches!!" : "doesn't match :(") );
    System.out.println( "First part: " + m.group(1) + ", Second part: " + m.group(2) );
 
  }


}
