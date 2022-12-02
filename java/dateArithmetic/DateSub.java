import java.util.Calendar;
import java.text.SimpleDateFormat;

public class DateSub {
  public static void main( String [] args ) {
    String dateStr = null;
    int dateNum = 0;
    long currTime = 0L;
    SimpleDateFormat dbdateFormat = new SimpleDateFormat( "YYYYMMdd" );

    Calendar ref = Calendar.getInstance();
    ref.add(Calendar.DAY_OF_YEAR,  30 );

    dateStr = dbdateFormat.format( ref.getTime() );
    
    System.out.println( "Initial date: " + dateStr );

    for( int i = 0; i < 50; i++ ) {

      System.out.print("Minus " + i + " months:\t");

      ref = Calendar.getInstance();
      currTime = ref.getTime().getTime();
      currTime = currTime - ( i * 31L * 24L * 3600L * 1000L );
      ref.setTimeInMillis( currTime );
      //ref.add(Calendar.DAY_OF_YEAR,  30 );
      //ref.add(Calendar.MONTH,  -1 * i );
      dateStr = dbdateFormat.format( ref.getTime() );
      System.out.print("\""+dateStr+"\"\t");
      try {
        dateNum = Integer.parseInt( dateStr );
      } catch( Exception e ) {
      }

      System.out.print( " -> " + dateNum + "\n" );
    
    }
    
  }
}
