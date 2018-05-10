import java.math.BigInteger;

public class ModuloTest {

  public static void main( String [] args ) {
    int n = 0;
    if( args.length != 1 ) {
      System.out.println( "Usage: java ModuloTest <n>" );
      System.exit(1);
    } else {
      try {
        n = Integer.parseInt( args[0] );
      } catch( NumberFormatException nfe ) {
        System.out.println( "Exception! " + nfe.toString() );
        System.exit(1);
      }
    }

    BigInteger num = generateN( n );
    BigInteger TWO = new BigInteger("2");
    BigInteger ans = null;

    System.out.println( "N = " + num + " [" + num.toString(2) + "]" );

    for( long i = 0L; i < 2000000L; i++ ) {
      ans = TWO.modPow( BigInteger.valueOf(i), num );
      System.out.println( "2^" + i + " (mod " + num + ") === " + ans + " [" + ans.toString(2) + "][diff: " + num.subtract(ans) + "]" );
    }
  }

  public static BigInteger generateN( int power ) {
    double pow2 = Math.pow( 2.0d, Integer.valueOf(power).doubleValue() );
    StringBuilder sb = new StringBuilder("1");
    for( int i = 1; i < Double.valueOf(pow2).intValue(); i++ ) {
      sb.append("0");
    }
    sb.append("1");
    return new BigInteger(sb.toString());
  }

}
