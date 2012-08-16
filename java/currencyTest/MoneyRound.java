import java.util.*;
import java.text.*;

public class MoneyRound
{
   public static void main( String [] args )
   {
      float f1 = 43565.3456f;
      float f2 = 5.3436f;
      float f3 = 5.34f;
      float f4 = 5.3f;
      float f5 = 5f;

      DecimalFormat df = new DecimalFormat();
      //df.setCurrency( Currency.getInstance( Locale.US ) );
      df.applyPattern( "#,##0.00" );

      //System.out.println( "" + f1 + " -> " + df.format( new Float(f1).doubleValue() ) );
      System.out.println( "" + f1 + " -> " + df.format( f1 ) );
      System.out.println( "" + f2 + " -> " + df.format( f2 ) );
      System.out.println( "" + f3 + " -> " + df.format( f3 ) );
      System.out.println( "" + f4 + " -> " + df.format( f4 ) );
      System.out.println( "" + f5 + " -> " + df.format( f5 ) );


   }


}
