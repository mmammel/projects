import java.util.regex.*;
import java.util.*;


public class QuoteReplace {
  private static final Pattern VARPATTERN = Pattern.compile("\\Q${\\E(.*?)\\Q}\\E");
  private static final String PROCESSME = "Thsi is a string with a ${VARIABLE} to replace";

  public static void main( String [] args ) {
    Matcher m = VARPATTERN.matcher( PROCESSME );
    Map<String,String> vars = new HashMap<String,String>();
    vars.put( "VARIABLE", "<li>special character (~!@#$%^*()-_=[{]}\\\\|;:<.>/?)</li>");
    StringBuffer buff = new StringBuffer();

    String tempVar = null;
    String tempReplacement = null;
    while( m.find() ) {
      tempVar = m.group(1);
      tempReplacement = vars.get(tempVar);

      if( tempVar != null && tempVar.length() > 0 && tempReplacement != null ) {
        m.appendReplacement( buff, Matcher.quoteReplacement(tempReplacement) );
      } else {
        m.appendReplacement( buff, Matcher.quoteReplacement(m.group(0)));
      }
    }

    m.appendTail( buff );

    System.out.println( buff.toString() );

    
  }
}
