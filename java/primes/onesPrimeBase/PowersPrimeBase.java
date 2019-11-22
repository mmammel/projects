import java.math.BigInteger;

public class PowersPrimeBase {

  public static void main( String [] args ) {
    int baseLim = 200;
    int powerLim = 200;
    if( args.length >= 1 ) {
      baseLim = Integer.parseInt( args[0] );
    }

    if( args.length == 2 ) {
      powerLim = Integer.parseInt( args[1] );
    }
      
    BigInteger temp = null;
    for( int b = 2; b < baseLim; b++ ) {
      if( PowersPrimeBase.isPrime(b) ) {
        for( int p = 0; p < powerLim; p++ ) {
          temp = PowersPrimeBase.generate( b, p );
          System.out.println( "(" + b + "," + p + "): " + temp.toString() ); 
        }
      }
    }
  }

  public static BigInteger generate( int base, int power ) {
    StringBuilder sb = new StringBuilder();
    BigInteger result = BigInteger.ZERO;
    BigInteger b = new BigInteger(""+base);
    for( int i = 0; i <= power; i++ ) {
      result = result.add( b.pow( i ) );
    } 
    return result;
  }

  public static boolean isPrime( int n ) {
    boolean retVal = false;
    if( n == 2 ) {
      retVal = true;
    } else if( n > 2 && n % 2 != 0 ) {
      retVal = true;
      double root = Math.sqrt( new Integer( n ).doubleValue() );
      for( int i = 3; i <= root; i+=2 ) {
        if( n % i == 0 ) {
          retVal = false;
          break;
        }
      }
    }

    return retVal;
  }
}


