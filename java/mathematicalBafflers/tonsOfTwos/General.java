public class General {
  public static void main( String [] args ) {
    try {
      double root = Double.parseDouble( args[0] );
      int iteration = Integer.parseInt( args[1] );
      double lim = Math.sqrt(root);
      for( int i = 0; i < iteration; i++ ) {
        lim = Math.sqrt( root + lim );
        System.out.println( lim );
      }
    } catch( Exception e ) {
      System.out.println( "Error: usage: java General <root> <iterations>" );
    }
  }
}
