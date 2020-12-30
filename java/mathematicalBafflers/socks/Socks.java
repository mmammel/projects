public class Socks {

  public static void main( String [] args ) {
    for( long l = 0l; l < 10000l; l++ ) {
      System.out.println( "" + l + ": " + Math.sqrt(32l*l*l + 1) ); 
    }
  }
}
