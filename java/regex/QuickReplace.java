import java.util.regex.*;

public class QuickReplace {
  public static void main( String [] args )
  {
    String sourceText = "This is a test String.\n, I want to see\n if I can \n reliably replace \n newline characters with a multiline\n pattern.";

    Pattern pattern = Pattern.compile( "$", Pattern.MULTILINE );
    String result = pattern.matcher( sourceText ).replaceAll( "<br>" );

    System.out.println( "Before:\n\n" + sourceText );
    System.out.println( "After:\n\n" + result );
  }
}
