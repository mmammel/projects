import java.util.*;
import java.text.*;

public class LeapYearTest
{

  public static void main( String [] args )
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date now = new Date();
    System.out.println( "Now: " + sdf.format( now ) );
    long postingLifeSpan = 30L;
    long futDate = now.getTime();
    futDate += postingLifeSpan * 24L * 3600L * 1000L;
    Date expDate = new Date();
    expDate.setTime(futDate);

    int month = expDate.getMonth() + 1;
    int day = expDate.getDate();
    int year = expDate.getYear();

    System.out.println( "Expiration: " + sdf.format( expDate) );


    GregorianCalendar cal = new GregorianCalendar();

    System.out.println( "Now: (using calendar): " + sdf.format( cal.getTime() ) );

    cal.add( Calendar.DAY_OF_YEAR, 30 );

    System.out.println( "Expiration: (using calendar): " + sdf.format( cal.getTime() ) );

  }

}