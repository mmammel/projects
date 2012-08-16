import org.jcrontab.data.*;
import java.util.Calendar;

public class CronTest
{
  public static void main( String [] args )
  {
    try
    {
      CrontabEntryBean entry = new CrontabParser().marshall(args[0]);
    
      Calendar cal = Calendar.getInstance();

      cal.set( Calendar.DAY_OF_WEEK, Calendar.SUNDAY );
      if( entry.equals( cal ) ) System.out.println( "Match SUN" );
      cal.set( Calendar.DAY_OF_WEEK, Calendar.MONDAY );
      if( entry.equals( cal ) ) System.out.println( "Match MON" );
      cal.set( Calendar.DAY_OF_WEEK, Calendar.TUESDAY );
      if( entry.equals( cal ) ) System.out.println( "Match TUE" );
      cal.set( Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY );
      if( entry.equals( cal ) ) System.out.println( "Match WED" );
      cal.set( Calendar.DAY_OF_WEEK, Calendar.THURSDAY );
      if( entry.equals( cal ) ) System.out.println( "Match TH" );
      cal.set( Calendar.DAY_OF_WEEK, Calendar.FRIDAY );
      if( entry.equals( cal ) ) System.out.println( "Match FRI" );
      cal.set( Calendar.DAY_OF_WEEK, Calendar.SATURDAY );
      if( entry.equals( cal ) ) System.out.println( "Match SAT" );
    }
    catch( Exception e )
    {
      System.out.println( "Error: " + e.toString() );
      e.printStackTrace();
    }
  }

}

