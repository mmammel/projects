import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class DateExpiration {

  public static void main( String [] args ) {
    SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
    DateExpiration DE = new DateExpiration();
    try {
      Date expirationDate = sdf.parse( args[0] );
      if( DE.isExpired( expirationDate ) ) {
        System.out.println( "Expired!" );
      } else {
        System.out.println( "All Good." );
      }
    } catch( Exception e ) {
      System.out.println( "Exception! : " + e.toString() );
    }
  }

  	private boolean isExpired( Date expirationDate ) {
	  boolean retVal = false;
	  
	  if( expirationDate != null ) {
	    Calendar cal = Calendar.getInstance();
	    cal.setTime(expirationDate);
	    cal.add(Calendar.DAY_OF_MONTH, 1);
	    cal.add(Calendar.SECOND, -1);
        System.out.println( expirationDate.toString() );
        System.out.println( cal.getTime().toString() );
	    if( cal.getTime().before(new Date())) {
	      retVal = true;
	    }
	  }
	  
	  return retVal;
	}
}
