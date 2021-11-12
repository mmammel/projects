import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class FactorsOfTwoBetweenPowersOfN {

  private static BigInteger TWO = new BigInteger("2");
  public static void main( String [] args ) {
    if( args.length != 2 ) {
      System.err.println( "Usage: java FactorsOfTwoBetweenPowersOfN <N> <HighestPower>" );
      System.exit(1);
    }

    BigInteger lowFactors = BigInteger.ONE, highFactors = null;
    BigInteger n = null;
    BigInteger factorsInN = null;
    BigInteger between = null;
    long tempFactors = 0l;
    int highPower = 0;
    BigInteger init = BigInteger.ZERO;

    List<String> powers = new ArrayList<String>();
    List<Integer> counts = new ArrayList<Integer>();
    try {
      n = new BigInteger(args[0]);
      factorsInN = BigInteger.valueOf( countFactorsOfTwo(n) );
      highPower = Integer.parseInt( args[1] );

      int total = 0;
      BigInteger grandTotal = BigInteger.ZERO;

      for( int i = 1; i <= highPower; i++ ) {
        between = getFactorsOfTwoBetweenPowers( n, i - 1, i );
        lowFactors = factorsInN.multiply( BigInteger.valueOf( Integer.valueOf( i - 1 ).longValue() ) );
        highFactors = factorsInN.multiply( BigInteger.valueOf( Integer.valueOf( i ).longValue() ) );
        if( i == 1 ) {
          grandTotal = grandTotal.add( lowFactors );
          System.out.println( "" + n + "^" + (i - 1) + "\t" + lowFactors + "\t" + grandTotal );
        }
        grandTotal = grandTotal.add( between );
        System.out.println("  |  \t" + between + "\t" + grandTotal );
        grandTotal = grandTotal.add( highFactors );
        System.out.println( "" + n + "^" + i + "\t" + highFactors + "\t" + grandTotal );
      }
    } catch( Exception e ) {
      System.out.println( "Error! " + e.toString() );
      e.printStackTrace();
    }
  }

  public static long countFactorsOfTwo( BigInteger n ) {
    long retVal = -1l;
    BigInteger [] tempResult = new BigInteger [2];
    tempResult[0] = n;
    tempResult[1] = BigInteger.ZERO;

    while( tempResult[1] == BigInteger.ZERO ) {
      retVal++;
      tempResult = tempResult[0].divideAndRemainder(TWO); 
    }

    return retVal;
  }

  public static BigInteger getFactorsOfTwo( BigInteger n, int pow ) {
    BigInteger b = n.pow(pow);
    BigInteger retVal = BigInteger.ZERO;
    while( b.compareTo( BigInteger.ONE ) > 0 ) {
      b = b.divide( TWO );
      retVal = retVal.add( b );
    }

    return retVal;
  }

  public static BigInteger getFactorsOfTwoBetweenPowers( BigInteger n, int pow1, int pow2 ) {
    long factorsInN = countFactorsOfTwo( n );
    BigInteger pow1Fac = getFactorsOfTwo( n, pow1 );
    BigInteger pow2Fac = getFactorsOfTwo( n, pow2 );
    BigInteger retVal = pow2Fac.subtract( pow1Fac ).subtract( BigInteger.valueOf( factorsInN * pow2 ) );
    return retVal;
  }
}
