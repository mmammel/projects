public class IntegerTest {
  public static void main( String [] args ) {
    try {
      int n = Integer.parseInt( args[0] );
      System.out.println( (n * 100)  % 1000000007 );
    } catch( Exception e ) {
      System.out.println( e.toString() );
    }
  }
}
