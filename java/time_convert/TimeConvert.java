import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimeConvert
{

  public static void main( String [] args )
  {
    if( args.length == 0 )
    {
      System.out.println( "usage: java TimeConvert <time in secs> [<time in secs> ...]");
    }
    else
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

      for( int i = 0; i < args.length; i++ )
      {
        formatted = args[i];

        if( formatted.length() < 13 )
        {
          formatted += "000";
        }

        System.out.println( sdf.format( new Date( Long.parseLong( formatted ) ) ) );
      }
    }
  }
}
