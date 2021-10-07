import java.math.BigInteger;

public class FactorsOfTwoBetweenPowersOfN {

  private static BigInteger TWO = new BigInteger("2");
  public static void main( String [] args ) {
    if( args.length != 2 ) {
      System.err.println( "Usage: java FactorsOfTwoBetweenPowersOfN <N> <HighestPower>" );
      System.exit(1);
    }

    BigInteger n = null;
    BigInteger init = BigInteger.ZERO;
    int highPower = null;

    List<String> powers = new ArrayList<String>();
    List<Integer> counts = new ArrayList<Integer>();
    try {
      n = new BigInteger(args[0]);
      highPower = Integer.parseInt( args[1] );

      int total = 0;

      for( int i = 0; i <= highPower ) {
        total = 0;
        powers.add("" + n + "^" + i );
        
        
        
      }
    } catch( Exception e ) {
      System.out.println( "Error! " + e.toString() );
      e.printStackTrace();
    }
  }

  public static int countFactorsOfTwo( BigInteger n ) {
    int retVal = -1;
    BigInteger [] tempResult = new BigInteger [2];
    tempResult[0] = n;
    tempResult[1] = BigInteger.ZERO;

    while( tempResult[1] == BigInteger.ZERO ) {
      retVal++;
      tempResult = tempResult[0].divideAndRemainder(TWO); 
    }

    return retVal;
  }
}
