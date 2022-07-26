public class DivTest {
  public static void main( String [] args ) {
    try {
      int l = Integer.parseInt( args[0] );
      int b = Integer.parseInt( args[1] );

      System.out.println( "" + l + " / " + b + " = " + (l/b) );
    } catch( Exception e ) {
      System.out.println( "Error!: " + e.toString() );
    }
  }
}
