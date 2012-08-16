import java.io.*;
import java.net.*;

public class FileURI
{
  public static void main( String [] args )
  {
    try
    {
      File diskfile = new File("words.txt");
      URI myuri = diskfile.toURI();
      System.out.println( "MyURI: " + myuri.toString() );
      File file = new File( new URI( "words.txt" ));
      BufferedReader reader = new BufferedReader( new InputStreamReader( new FileInputStream( file ) ) );

      String temp = null;

      while( (temp = reader.readLine()) != null )
      {
        System.out.println( temp );
      }
    }
    catch( Exception e )
    {
      System.out.println( "Exception: " + e.toString() );
    }
  }
}
