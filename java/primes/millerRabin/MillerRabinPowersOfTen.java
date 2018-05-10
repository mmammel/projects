import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;

public class MillerRabinPowersOfTen {
  private BigInteger n = null;
  private BigInteger n_minus_one = null;
  private BigInteger s = null;
  private BigInteger d = null;

  public static void printUsageAndExit() {
    System.out.println( "Usage: java MillerRabinPowersOfTen <pow>");
    System.out.println( "  where <pow> is n in 10^(2^n) + 1" );
    System.exit(1);
  }

  public static void main( String [] args ) {
    int power = 0;

    if( args.length == 1 ) {
      try {
        power = Integer.parseInt( args[0] );
      } catch( NumberFormatException nfe ) {
        printUsageAndExit();
      }
    }

    MillerRabinPowersOfTen M = new MillerRabinPowersOfTen(power);
    try {
      boolean isPrime = M.check();
      System.out.println( "" + M.getN() + " is " + (isPrime ? "" : "NOT ") + "prime!");
    } catch( Exception e ) {
      e.printStackTrace();
    }
  }

  public BigInteger getN() { return this.n; }

  private BigInteger generateN( int power ) {
    double pow2 = Math.pow( 2.0d, Integer.valueOf(power).doubleValue() );
    StringBuilder sb = new StringBuilder("1");
    for( int i = 1; i < Double.valueOf(pow2).intValue(); i++ ) {
      sb.append("0");
    }
    sb.append("1");
    return new BigInteger(sb.toString());
  }

  public MillerRabinPowersOfTen( int power ) {
    // get n, 10^(2^power)
    this.n = this.generateN( power );
    this.n_minus_one = this.n.subtract( BigInteger.ONE );
    
    /*
     * n - 1 has to be represented by:
     *   2^s*d
     * where d is odd.  Since n - 1 is of the form 10^(2^n) this is easy, we can write:
     *   n - 1 = 2^(2^n)*5^(2^n)
     * so s = 2^n and d = 5^(2^n)
     */
    int shift = this.n_minus_one.getLowestSetBit();
    this.s = BigInteger.valueOf(Integer.valueOf(shift).longValue());  // s = 2^n
    this.d = this.n_minus_one.shiftRight(shift); // d = 5^(2^n)
    System.out.println( "n = " + n + ", s = " + s + ", d = " + d );
  }

  /**
   * For all a, 0 < a < n - 1, check to see if:
   *   a^d !== 1 (mod n)
   * if we find this holds for some a, go on to check the second condition.
   */
  public boolean check() {
    boolean retVal = true;
    BigInteger temp = null;
    BigInteger a = BigInteger.valueOf( 2L );
    while( a.compareTo( this.n_minus_one ) < 0 ) {
      temp = a.modPow( this.d, this.n );
      //System.out.println( "First test on a = " + a + " -> " + temp );
      if( !temp.equals( BigInteger.ONE ) ) {
        //System.out.println( "Found a candidate!: " + a );
        if( !this.checkSecondCondition( a ) ) {
          retVal = false;
          //System.out.println( "Not prime! " + a + " is a witness" );
        } else {
          System.out.println( "" + a + " is NOT a witness for " + this.n + " : a^(2^s*d) === -1 (mod n) for some s");
        }
      } else {
        System.out.println( "" + a + " is NOT a witness for " + this.n + " : " + a + "^" + d + " === " + temp + " (mod n)" );
      }

      a = a.add( BigInteger.ONE );
    }
    return retVal;
  }

  private boolean checkSecondCondition( BigInteger candidate ) {
    boolean retVal = false, foundEqual = false;
    BigInteger aPower = null, result = null;
    for( long r = 0L; r < s.longValue(); r++ ) {
      aPower = (BigInteger.valueOf( 2L ).pow( Long.valueOf( r ).intValue()).multiply( this.d ));
      result = candidate.modPow( aPower, this.n );
      if( result.equals( this.n_minus_one ) ) {
        retVal = true;
        break;
      } else {
        //System.out.println( "Check for r = " + r + " failed: " + result );
      }
    }

    return retVal;
  }
}
