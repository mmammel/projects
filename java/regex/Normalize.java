import java.util.regex.*;

public class Normalize {

  public static void main( String [] args ) {
    String retVal = args[0].replaceAll("[^\\sA-Za-z0-9_-]", "");
    System.out.println( "1: " + retVal );
    retVal = retVal.replaceAll("\\s{2,}"," "); // collapse spaces.
    System.out.println( "2: " + retVal );
    retVal = retVal.toLowerCase();
    System.out.println( "3: " + retVal );

    Pattern pattern = Pattern.compile(" ([a-z0-9])");
    Matcher matcher = pattern.matcher(retVal);

    StringBuffer sb = new StringBuffer();

    while (matcher.find()) {
      matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
    }

    matcher.appendTail(sb);
    System.out.println( sb.toString() );
  }
}
