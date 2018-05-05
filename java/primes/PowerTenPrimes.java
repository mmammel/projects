import java.math.BigInteger;

public class PowerTenPrimes {

  public static void main( String [] args ) {
    String num = null;
    int zeros = 1;
    BigInteger bigNum = null;
    for( int i = 1; i < 1000; i++ ) {
      zeros *= 2;
      num = "1" + getZeros( zeros - 1 ) + "1";
      bigNum = new BigInteger( num );
      System.out.println( "10^(2^" + i + ") is" + (bigNum.isProbablePrime(5) ? " " : " not ") + "prime" );
    }   
  }

  public static String getZeros( int numZeros ) {
    StringBuilder sb = new StringBuilder();
    for( int i = 0; i < numZeros; i++ ) {
      sb.append('0');
    }
    return sb.toString();
  }
}
