import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.util.Date;

public class TimeZoneTest
{
  SimpleDateFormat sdf_local, sdf_gmt, sdf_local_millis, sdf_gmt_millis;

  public TimeZoneTest()
  {
    sdf_local = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    sdf_local.setTimeZone( TimeZone.getDefault() );

    sdf_local_millis = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.S");
    sdf_local_millis.setTimeZone( TimeZone.getDefault() );

    sdf_gmt   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    sdf_gmt.setTimeZone( TimeZone.getTimeZone("GMT") );

    sdf_gmt_millis   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
    sdf_gmt_millis.setTimeZone( TimeZone.getTimeZone("GMT") );
  }

  private void print( String str )
  {
    //simplify printing.
    System.out.println( str );
  }

  public void imitateInitTime_orig( String date )
  {
    long ltime = 0L;
    long now = 0L, diff = 0L;

    this.print( "Imitating handling of init time in original" );
    this.print( "Processing FPTime: " + date );
    this.print( "First, convertToLong: " );

    ltime = this.convertToLong_orig( date );

    this.print( "Got: " + ltime );
    this.print( "Now get the current time and subtract" );

    Calendar cal = Calendar.getInstance();
    now = cal.getTime().getTime();
    diff = now - ltime;

    this.print( "Difference is: " + diff );
  }

  public void imitateInitTime_new( String date )
  {
    long ltime = 0L;
    long now = 0L, diff = 0L;

    this.print( "Imitating handling of init time in new" );
    this.print( "Processing FPTime: " + date );
    this.print( "First, convertToLong: " );

    ltime = this.convertToLong_new( date );

    this.print( "Got: " + ltime );
    this.print( "Now get the current time and subtract" );

    Calendar cal = Calendar.getInstance();
    now = cal.getTime().getTime();
    diff = now - ltime;

    this.print( "Difference is: " + diff );
  }

  public void imitateCreateTime_orig( String date )
  {
    this.print( "Imitating handling of creation time in orig" );
    this.print( "Processing creationTime: " + date );
    this.print( "New Creation Time = " + this.convertCreationTime_orig( date ) );
    
  }
    
  public void imitateCreateTime_new( String date )
  {
    this.print( "Imitating handling of creation time in new" );
    this.print( "Processing creationTime: " + date );
    this.print( "New Creation Time = " + this.convertCreationTime_new( date ) );
  }

  private long convertToLong_orig( String time )
  {
    long ltime = 0L;
    //take a millisecond time, GMT, and convert to String
    try
    {
      Date dtime = sdf_gmt_millis.parse( time );
      ltime = dtime.getTime();
    }
    catch( Exception e )
    {
      this.print( "Caught Exception: " + e.toString() );
    }

    return ltime;
  }

  private long convertToLong_new( String time )
  {
    long ltime = 0L;
    //take a millisecond time, GMT, and convert to local String
    try
    {
      Date dtime = sdf_local_millis.parse( time );
      ltime = dtime.getTime();
    }
    catch( Exception e )
    {
      this.print( "Caught Exception: " + e.toString() );
    }

    return ltime;
  }

  private String convertCreationTime_orig( String time )
  {
    String converted = time;

    try
    {
      long ltime = this.convertToLong_orig( converted );
      Date dcreate = new Date( ltime );
      converted = sdf_local.format( dcreate );
    }
    catch( Exception e )
    {
      this.print( "Caught Exception: " + e.toString() );
    }

    return converted;
  }

  private String convertCreationTime_new( String time )
  {
    String converted = time;

    try
    {
      long ltime = this.convertToLong_new( converted );
      Date dcreate = new Date( ltime );
      converted = sdf_gmt.format( dcreate );
    }
    catch( Exception e )
    {
      this.print( "Caught Exception: " + e.toString() );
    }

    return converted;
  }

  public static void main( String [] args )
  {
    TimeZoneTest tzt = null;
    String init_time = "2006-07-17 13:00:00.56";
    String create_time = "2006-07-16 14:15:00.117";

    try
    {
      tzt = new TimeZoneTest();

      System.out.println( "Testing init times" );
      System.out.println( "------------------" );
      System.out.println( "\n-----ORIG---------" );
      tzt.imitateInitTime_orig( init_time );
      System.out.println( "\n-----NEW----------" );
      tzt.imitateInitTime_new( init_time );

      System.out.println( "\nTesting create times" );
      System.out.println( "------------------" );
      System.out.println( "\n-----ORIG---------" );
      tzt.imitateCreateTime_orig( create_time );
      System.out.println( "\n-----NEW----------" );
      tzt.imitateCreateTime_new( create_time );

    }
    catch( Exception e )
    {
      System.out.println( "Exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}
