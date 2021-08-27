import java.math.BigDecimal;

public class BigDecimalTest {
  public static void main( String [] args ) {
    BigDecimal bd = new BigDecimal(0);
    System.out.println( bd );
    bd = new BigDecimal(1.0000000001);
    System.out.println( bd.floatValue() );
    System.out.println( bd.doubleValue() );
  }
}
