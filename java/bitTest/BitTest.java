public class BitTest
{
  public static void main( String [] args )
  {
    int foo = Integer.parseInt( args[0] );
    int places = Integer.parseInt( args[1]);

    boolean matches1 = ((foo & 1<<0) > 0);
    boolean matches2 = ((foo & 1<<1) > 0);
    boolean matches4 = ((foo & 1<<2) > 0);
    boolean matchesC = ((foo & 0xC) > 0);

    System.out.println( Integer.toString(foo, 2 ) );
    System.out.println( Integer.toString(foo ^ 127, 2 ) );

    System.out.println( "Rotate left: " + places + " places: " + Long.toString(BitTest.rol( foo, places ),2) );

    System.out.println( "Result: [" + matches1 + "," + matches2 +"," + matches4 + "," + matchesC + "]" );


    long h = 0l;

    for( int i = 0; i < 7; i++ ) {
      h = hash( h, 'x' );
      System.out.println( "Hash " + i + ": " + h );
    }
  }

  public static long rol( long val, long places ) {
    // rotate a value "places" bits to the left.
    long retVal = val << places | val >> (32l - places);
    retVal &= 0xffffffffl;
    return retVal;
  }

  public static long hash( long h, char c ) {
    return c + rol( h, 7l);
  }
}
