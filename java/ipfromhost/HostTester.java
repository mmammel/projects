import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URL;

public class HostTester
{
  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    String input_str = "";
    String ip_str = "";

    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( "Welcome to the IP looker-upper!  Just enter a hostname at the prompt and press enter." );

      System.out.print("hostname> ");

      while( (input_str = input_reader.readLine()) != null )
      {
        try
        {
          if( input_str.equalsIgnoreCase( "quit" ) )
          {
            break;
          }
          else
          {
            ip_str = InetAddress.getByName( input_str ).getHostAddress();
            System.out.println( "IP address for host \"" + input_str + "\" : " + ip_str );
            try
            {
               System.out.println( "Trying to make an http connection..." );
               URL url = new URL( "http://" + ip_str );
               url.openConnection();
               System.out.println( "...Success!!" );
            }
            catch( IOException ioe )
            {
              System.out.println( "...Got an error: " + ioe.toString() );
            }
          }
        }
        catch( UnknownHostException uhe )
        {
          System.out.println( "Unknown host: \"" + input_str + "\"" );
        }

        System.out.print( "\nhostname> " );
      }

      System.out.println( "Adios." );
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}
