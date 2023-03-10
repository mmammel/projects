import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampString
{

  public static void main( String [] args )
  {
    Date now = new Date();
    System.out.println( now.toString() );
    SimpleDateFormat f = new SimpleDateFormat( "yyyy-MM-dd HH:mm a z" );
    System.out.println( f.format(now) );
  }


}
