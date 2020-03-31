import java.math.BigInteger;

public class SelfRefSequences {
  public static void main( String [] args ) {
    if( args.length != 4 ) {
      System.out.println( "Usage: java SelfRefSequences <C1> <C2> <A0> <A1>\nTo define the sequence: A(n) = C1*A(n-1) + C2*A(n-2), With A(0) = A0 and A(1) = A1" );
      System.exit(1);
    } else {
      BigInteger C1 = new BigInteger( args[0] );
      BigInteger C2 = new BigInteger( args[1] );
      BigInteger [] series = new BigInteger[ 100 ];
      series[0] = new BigInteger( args[2] );
      series[1] = new BigInteger( args[3] );
      for( int i = 2; i < 100; i++ ) {
        series[i] = series[ i - 1 ].multiply( C1 ).add( series[ i - 2 ].multiply( C2 ) );
      }
      printSeries( series );
    }
  }

  public static void printSeries( BigInteger [] series ) {
    for( int i = 0; i < series.length; i++ ) {
      System.out.print( ""+ series[i] + " " );
    }
    System.out.println( "" );
  }
}
