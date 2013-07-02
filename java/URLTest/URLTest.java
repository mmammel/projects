import java.net.URL;

public class URLTest {
  public static void main( String [] args )
  {
    try
    {
      URL myurl = new URL( args[0] );
    }
    catch( Exception e )
    {
      System.out.println( "Exception caught: " + e.toString() );
    }
  }
}
