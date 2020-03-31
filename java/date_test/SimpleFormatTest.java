import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class SimpleFormatTest
{
  public static void main( String [] args )
  {
    System.out.println( new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'Z'" ).format( Calendar.getInstance().getTime() ));
  }
}
