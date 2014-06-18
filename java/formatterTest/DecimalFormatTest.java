import java.text.DecimalFormat;
import java.math.BigDecimal;

public class DecimalFormatTest {

  public static void main( String [] args ) {
    float total = 136352299.0f;
    final DecimalFormat df = new DecimalFormat();
    df.setMinimumFractionDigits(2);
    df.setMaximumFractionDigits(2);
    df.setGroupingUsed(false);
    System.out.println(df.format(total));
    System.out.println( "" + new Float( total ) );
    BigDecimal bd = new BigDecimal( df.format(total) );
    BigDecimal bd2 = new BigDecimal( "" + new Float(total) );
  }
}
