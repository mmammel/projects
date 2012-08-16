import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class DateTest
{

  /**
   * Avoid using DOMs, just get attributes by searching the XML string
   */
  public long getIntervalsFromSpan( String start, String end, long interval )
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Calendar cal = new GregorianCalendar();
    long retval = 0L, start_millis = 0L, end_millis = 0L;

    try
    {
      cal.setTime( sdf.parse( start ) );
      start_millis = cal.getTimeInMillis();
      cal.setTime( sdf.parse( end ) );
      end_millis = cal.getTimeInMillis();
      retval = (end_millis - start_millis) / (interval * 60L * 1000L);
    }
    catch( Exception e )
    {
      System.out.println( "Exception: " + e.toString() );
      retval = -1L;
    }

    return retval;
  }

  public static void main( String [] args )
  {
    if( args.length != 3 )
    {
      System.out.println( "Usage: java DateTest <startTime> <endTime> <interval>" );
    }
    else
    {
      DateTest dt = new DateTest();

      try
      {
        System.out.println( "Result: " + dt.getIntervalsFromSpan( args[0], args[1], Long.parseLong(args[2]) ) );
      }
      catch( Exception e )
      {
        System.out.println( "Exception: " + e.toString() );
        e.printStackTrace();
      }
    }

  }

}
