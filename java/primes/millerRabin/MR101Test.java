import java.math.BigInteger;

public class MR101Test {
  public static void main( String [] args ) {

    BigInteger val = new BigInteger("101");
    BigInteger exp = new BigInteger("25");
    BigInteger a = null;
    for( long i = 2L; i < 100L; i++ ) {
      a = BigInteger.valueOf( i );
      System.out.println( "a = " + a + ", " + a.pow(25) + " (mod " + val + ") === " + a.modPow( exp, val ) );
    }
  }
}
