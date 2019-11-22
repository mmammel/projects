import java.math.BigInteger;

public class OnesPrimeBase {

  public static void main( String [] args ) {
    BigInteger temp = null;
    for( int b = 2; b < 32; b++ ) {
      if( OnesPrimeBase.isPrime(b) ) {
        for( int p = 0; p < 100; p++ ) {
          temp = OnesPrimeBase.generate( b, p );
          System.out.println( "(" + b + "," + p + "): " + temp.toString(10) ); 
        }
      }
    }
  }

  public static BigInteger generate( int base, int power ) {
    StringBuilder sb = new StringBuilder();
    for( int i = 0; i <= power; i++ ) sb.append("1");
    return new BigInteger( sb.toString(), base );
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


