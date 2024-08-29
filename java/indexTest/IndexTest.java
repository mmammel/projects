public class IndexTest {
  public static void main( String [] args ) {
    String arg = null;
    int num = 4;

    if( args.length == 1 ) {
      try {
        num = Integer.parseInt( args[0] );
      } catch( Exception e ) {
      }
    } 

    for( int i = 0; i < num - 1; i++ ) {
      for( int j = i+1; j < num; j++ ) {
        System.out.println( (i*num) + j - (((i+1)*(i+2))/2) );
      }
    }
  }
}
