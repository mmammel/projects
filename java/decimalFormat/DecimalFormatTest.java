import java.text.*;
import java.math.*;

public class DecimalFormatTest {
  public static void main( String []  args ) {
      DecimalFormat decimalFormat = new DecimalFormat("##.###");
      decimalFormat.setRoundingMode(RoundingMode.DOWN);
      System.out.println( Float.parseFloat(decimalFormat.format(.006f)));
  }
}
