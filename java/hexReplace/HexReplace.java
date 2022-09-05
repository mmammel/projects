import java.util.regex.*;
import java.io.*;

public class HexReplace {

  private static final String HEX_PATTERN = "0x([0123456789AaBbCcDdEeFf]+)";
  public static void main( String [] args ) {

    Matcher m = Pattern.compile( HEX_PATTERN).matcher( args[0] );

    StringBuffer sb = new StringBuffer();
    String tempMatch = null, tempReplacement = null;
    String errorMe = null;
    int num = 0;

    while( m.find() ) {
      tempMatch = m.group(1);
      tempReplacement = tempMatch;
      try {
        num = Integer.parseInt( tempMatch, 16);
        tempReplacement = ""+(char)num;
        errorMe.toUpperCase();
      } catch( Exception e ) {
        dumpException(e);
      }

      m.appendReplacement( sb, Matcher.quoteReplacement( tempReplacement ) );
    }

    m.appendTail( sb );

    System.out.println( sb.toString() );
    
  }

  public static void dumpException( Throwable e ) {
    StringWriter st = new StringWriter();
    e.printStackTrace( new PrintWriter(st) );
    st.flush();

    System.out.println( "Exception: " + e.toString() + "\n--Stack--\n" + st.toString() );
  }
}
