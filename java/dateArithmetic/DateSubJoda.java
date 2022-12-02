import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;


public class DateSubJoda {
  public static void main( String [] args ) {
    String dateStr = null;
    int dateNum = 0;

    DateTime ref = new DateTime();

    ref = ref.minusDays(1);

    DateTimeFormatter fmt = DateTimeFormat.forPattern("YYYYMMdd");
    dateStr = fmt.print(ref);
    
    System.out.println( "Initial date: " + dateStr );

    for( int i = 0; i < 50; i++ ) {

      System.out.print("Minus " + i + " months:\t");

      ref = new DateTime();
      ref = ref.minusDays(1);
      ref = ref.minusMonths( i );;
      dateStr = fmt.print( ref );
      System.out.print("\""+dateStr+"\"\t");
      try {
        dateNum = Integer.parseInt( dateStr );
      } catch( Exception e ) {
      }

      System.out.print( " -> " + dateNum + "\n" );
    
    }
    
  }
}
