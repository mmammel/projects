public class PercentageTest {
  public static void main( String [] args ) {
    int n = 0;
    int outOf = 0;
    try {
      n = Integer.parseInt( args[0] );
      outOf = Integer.parseInt( args[1] );
    } catch( Exception e ) {
    }
    int percentage = n >= outOf ? 100 : (100*n)/outOf;
    System.out.println( ""+percentage+"%" );
  }
}
