import java.util.Calendar;
import java.text.SimpleDateFormat;

public class DateSubBug {
  public static void main( String [] args ) {
    String dateStr = null;
    int dateNum = 0;
    SimpleDateFormat dbdateFormat = new SimpleDateFormat( "yyyyMMdd" );

    Calendar ref = Calendar.getInstance();
    ref.add(Calendar.DAY_OF_YEAR,  -1 );

    dateStr = dbdateFormat.format( ref.getTime() );
    
    System.out.println( "Initial date: " + dateStr );

    for( int i = 0; i < 50; i++ ) {

      System.out.print("Minus " + i + " months:\t");

      ref = Calendar.getInstance();
      ref.add(Calendar.DAY_OF_YEAR,  -1 );
      ref.add(Calendar.MONTH,  -1 * i );
      System.out.println( ""+ref.getTime() );
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
