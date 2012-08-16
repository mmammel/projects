import java.net.InetAddress;
import java.net.UnknownHostException;

public class LocalHostTest
{

  public static void main( String [] args )
  {
    String result = null;
    
    try
    {
      result = InetAddress.getLocalHost().getHostAddress();
    }
    catch( Exception uhe )
    {
      result = "unknown";
    }

    System.out.println( "Result: " + result );
  }

}
