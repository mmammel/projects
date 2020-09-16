public class BitTest
{
  public static void main( String [] args )
  {
    int foo = Integer.parseInt( args[0] );

    boolean matches1 = ((foo & 1<<0) > 0);
    boolean matches2 = ((foo & 1<<1) > 0);
    boolean matches4 = ((foo & 1<<2) > 0);
    boolean matchesC = ((foo & 0xC) > 0);

    System.out.println( Integer.toString(foo, 2 ) );
    System.out.println( Integer.toString(foo ^ 127, 2 ) );

    System.out.println( "Result: [" + matches1 + "," + matches2 +"," + matches4 + "," + matchesC + "]" );
  }
}
