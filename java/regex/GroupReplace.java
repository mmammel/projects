
public class GroupReplace {
  private static final String TAG_HEADER_PATTERN = "^(.*?):.*$";

  public static void main( String [] args ) {
    if( args[0].matches( TAG_HEADER_PATTERN ) ) {
      System.out.println( args[0].replaceFirst( TAG_HEADER_PATTERN, "$1" ) );
    }
  }

}
