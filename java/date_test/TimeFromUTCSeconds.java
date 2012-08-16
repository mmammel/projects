import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class TimeFromUTCSeconds
{
  public static void main( String [] args )
  {

    SimpleDateFormat sdf_gmt   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
    sdf_gmt.setTimeZone( TimeZone.getTimeZone("GMT") );

    long timeToCheck = Long.parseLong( args[0] );

    try
    {
      Date d = new Date( timeToCheck * 1000 );
      System.out.println( "Date:\n-----\n  " + sdf_gmt.format( d ) );
      d = new Date( (timeToCheck - (timeToCheck & 2047)) * 1000 );
      System.out.println( "Date:\n-----\n  " + sdf_gmt.format( d ) );

    }
    catch( Exception e )
    {
      System.out.println( "Exception!: " + e.toString() );
    }

  }

}
