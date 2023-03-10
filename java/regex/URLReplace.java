import java.util.regex.*;

public class URLReplace {
  public static void main( String [] args ) {
    String url = args[0];

    System.out.println( url.replaceAll("^https?://[^/]+","") );

    Pattern p = Pattern.compile( "\\+-{2,}" );

    Matcher m = p.matcher( args[0] );
    if( m.find() ) {
      System.out.println( "Possible ascii table" );
    } else {
      System.out.println( "Not an ascii table" );
    }
  }

}
