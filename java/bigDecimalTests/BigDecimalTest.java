import java.math.BigDecimal;

public class BigDecimalTest {
  public static void main( String [] args ) {
   BigDecimal bd = new BigDecimal(0);
   System.out.println( "BigDecimal( Float.parseFloat(\"1.1\")) : " );
    bd = new BigDecimal(Float.parseFloat("1.1"));
    System.out.println( "bd.floatVal: " + bd.floatValue() );
    System.out.println( "bd.doubleVal: " +bd.doubleValue() );
   System.out.println( "BigDecimal( Double.parseDouble(\"1.1\")) : " );
    bd = new BigDecimal(Double.parseDouble("1.1"));
    System.out.println( "bd.floatVal: " + bd.floatValue() );
    System.out.println( "bd.doubleVal: " + bd.doubleValue() );
   System.out.println( "BigDecimal(Double.valueOf(Double.parseDouble(\"1.1\")).floatValue()) : " );
    bd = new BigDecimal(Double.valueOf(Double.parseDouble("1.1")).floatValue());
    System.out.println( "bd.floatVal: " + bd.floatValue() );
    System.out.println( "bd.doubleVal: " + bd.doubleValue() );
  }
}
