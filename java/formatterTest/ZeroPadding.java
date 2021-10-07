public class ZeroPadding {
  public static void main( String [] args ) {
    try {
      int i = Integer.parseInt( args[0] );
      System.out.println( String.format( "%1$+03d", i ) );
    } catch( Exception e ) {
      System.out.println( e.toString() );
    }
  }
}
