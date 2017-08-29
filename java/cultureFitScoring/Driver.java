public class Driver {
  public static void main( String [] args ) {
    int runs = 1;

    if( args.length == 1 ) {
      runs = Integer.parseInt( args[0] );
    } 

    CultureFitScoreGenerator C = new CultureFitScoreGenerator();

    double sum = 0.0d;

    for( int i = 0; i < runs; i++ ) {
      sum += C.run(true);
    }

    System.out.println( "Overall average: " + (sum / new Double( runs )) );
  }
}
