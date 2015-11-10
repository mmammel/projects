public class DoubleOverflow {
  public static void main( String [] args ) {
    System.out.println( ""+((Long.MAX_VALUE * 2 + 5000L) + Long.MAX_VALUE) );
    System.out.println( System.currentTimeMillis() );
  }
}
