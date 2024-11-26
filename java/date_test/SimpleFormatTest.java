import java.util.Date;
import java.util.Calendar;
import java.util.TimeZone;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

public class SimpleFormatTest
{
  public static void main( String [] args )
  {
    System.out.println( new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'Z'" ).format( Calendar.getInstance().getTime() ));
    System.out.println( new SimpleDateFormat( "yyyy-MM-dd 00:00:00" ).format( Calendar.getInstance().getTime() ));
    System.out.println( "Cookie Expire String: " );
    Date expdate= Calendar.getInstance().getTime();
    expdate.setTime (expdate.getTime() + (30 * 1000));
    expdate.setTime(0);
    DateFormat df = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss zzz", java.util.Locale.US);
    df.setTimeZone(TimeZone.getTimeZone("GMT"));
    String cookieExpire = "expires=" + df.format(expdate);
    System.out.println( cookieExpire );
  }
}
