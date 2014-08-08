import java.math.BigDecimal;

public class Functions {

  public static double rnd( String str1, String str2 ) {
      System.out.println( "rnd( " + str1 +","+ str2 + " )" );
      double z = 0;
      try {
         double x = Double.parseDouble(str1);; 
         str1 = Double.toString(x);
         double y = Double.parseDouble(str2);
         z = Math.round(x/y)*y;

         if (y < 1) {
            if (str1.indexOf('.') == -1 ||
                (str1.length() - str1.indexOf('.')) > (str2.length() - str2.indexOf('.'))) {
               BigDecimal t = new BigDecimal(z);
               t = t.setScale((str2.length()-1), BigDecimal.ROUND_HALF_UP);
               z = t.doubleValue();
            } else {
               z = x;
            }
         }
      } catch (Exception e) {
        System.out.println( "Exception: " + e.toString() );
      }
      return z;
   }

  public static void main( String [] args ) {
    System.out.println( rnd( args[0], args[1] ) );
  }

}
