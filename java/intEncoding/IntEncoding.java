public class IntEncoding {
  public static void main( String [] args ) {
    if( args.length != 1 ) {
      System.err.println( "Usage: java IntEncoding 00000000" );
      System.exit(1);
    }
    try {
      String input = args[0];
      int QNUM = Integer.parseInt( input );
 
      System.out.println( "Type: " + (QNUM / 1000000) );
      System.out.println( "Item Num: " + ((QNUM % 1000000) / 10000) );
      System.out.println( "Question num: " + ((QNUM % 10000) / 100) );
      System.out.println( "Attempt: " + (QNUM % 100 ) );

      int t = 3;
      int i = 1;
      int q = 4;
      int a = 1;

      int newQnum = t * 10000000 + i * 10000 + q * 100 + a;
      System.out.println( "New qnum = "+ newQnum);

    } catch( Exception e ) {
      System.out.println( "Error: " + e.toString() );
    }
  }
}
