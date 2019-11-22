public class JSONUnquote {
  public static void main( String [] args ) {
    String input = "\"{ \\\"foo\\\" : \\\"bar\\\" }\"";

    System.out.println( input );
    System.out.println( unquoteJSON( input ) );
  }

  public static String unquoteJSON( String quoted ) {
    String retVal = null;
    if( quoted != null ) {
      retVal = quoted.trim();
      retVal = retVal.replaceAll("^\"(.*?)\"$", "$1" );
      retVal = retVal.replaceAll("\\\\\"", "\"" );
    }
    
    return retVal;
  }
}
