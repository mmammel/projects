public class StringFormat {
  public static void main( String [] args ) {
    int i = Integer.parseInt( args[0] );
    System.out.println( "[" + String.format("%1$3d", i ) + "]" );
  }
}
