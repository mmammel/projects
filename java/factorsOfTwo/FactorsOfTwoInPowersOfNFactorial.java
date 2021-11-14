import java.math.BigInteger;

public class FactorsOfTwoInPowersOfNFactorial {
  public static final BigInteger TWO = new BigInteger("2");
  public static void main( String [] args ) {
    BigInteger n = BigInteger.ZERO;
    int maxPow = 0;

    if(args.length != 2 ) {
      System.out.println( "Usage: java FactorsOfTwoInPowersOfNFactorial <n> <maxPow>" );
      System.exit(1);
    }

    try {
      n = new BigInteger( args[0] );
      maxPow = Integer.parseInt( args[1] );
    } catch( Exception e ) {
      System.out.println( "Bad arguments. Usage: java FactorsOfTwoInPowersOfNFactorial <n> <maxPow>" );
      System.exit(1);
    }

    BigInteger factorsOfTwo = BigInteger.ZERO;
    for( int i = 0; i <= maxPow; i++ ) {
      factorsOfTwo = getFactorsOfTwo(n, i);
      System.out.println( ""+n+"^"+i+"! -> "+factorsOfTwo + " = " + n + "^" + i + " - " + (n.pow(i).subtract(factorsOfTwo) ) );
    }
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
} 
