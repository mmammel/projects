public class SBTest {

  public static void main( String [] args ) {
    StringBuilder sb = new StringBuilder();

    sb.append( "ABCDEFGHIJKLMNOP" );

    System.out.println( sb.toString() );

    for( int i = 1; i < sb.length() / 4; i++ ) {
      sb.insert( i*4 + (i - 1), ' ');
    }

    System.out.println( sb.toString() );
  }

}
