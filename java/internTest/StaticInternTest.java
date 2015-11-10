public class StaticInternTest {
  public static final String FOOBAR = "foobar";

  public static void main( String [] args ) {
    String testStr = null;

    if( args.length > 0 ) {
      testStr = args[0].toLowerCase().intern();
    } else {
      testStr = FOOBAR;
    }

    System.out.println( "Test str: " + testStr + " DOES " + (testStr == FOOBAR ? "" : "NOT ") + "MATCH!!!" );
  }

}
