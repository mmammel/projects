public class ABPuzzleNoRegex {
  public static void main( String [] args ) {
    boolean haveb = false;
    boolean result = true;
    for( int i = 0; i < args[0].length(); i++ ) {
      if( args[0].charAt(i) == 'b' ) {
        haveb = true;
      } else if( args[0].charAt(i) == 'a' && haveb ) {
        result = false;
        break;
      }
    }
    System.out.println( result );
  }
}
