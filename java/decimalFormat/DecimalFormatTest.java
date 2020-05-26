import java.text.*;
import java.math.*;

public class DecimalFormatTest {
  public static void main( String []  args ) {
      double d = Double.parseDouble( args[0] );
      DecimalFormat decimalFormat = new DecimalFormat("\u0020\u0020 ####0.00000000 ; \u0020-###0.00000000");
      System.out.println( decimalFormat.format(d) );
  }
}
