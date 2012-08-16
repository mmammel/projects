import java.net.URL;
import java.net.HttpURLConnection;

public class Defaults {

  public static void main( String [] args )
  {
    try
    {
      URL url = new URL( args[0] );
      HttpURLConnection conn = (HttpURLConnection)url.openConnection();
      System.out.println( "Connect timeout: " + conn.getConnectTimeout() );
      System.out.println( "Read timeout: " + conn.getReadTimeout() );
    
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception!!!! " + e.toString() );
    }
  }

}
