import java.io.*;
import java.net.*;
import javax.net.ssl.*;
import java.security.*;

public class RedirectTest {
  private static final int BUFFER_SIZE = 2048;

  public static final String BODY = "{ \"foo\" : \"bar\" }";
  public static final String HTTPSURL = "https://config.skillcheck.com/onlinetesting/launch/redirect/redirect.php?secure=true&redir=true";

  public static void main( String [] args ) {
    try {
      RedirectTest RT = new RedirectTest();
      System.out.println(" INPUT | OUTPUT | FOLLOW | METHOD ");
      System.out.println("-------+--------+--------+--------");
      System.out.println("   Y        Y       Y       POST  ");
      RT.makeRequest( HTTPSURL, BODY, "POST", new HttpOptions( true,true,true)); 
      System.out.println("   Y        Y       N       POST  ");
      RT.makeRequest( HTTPSURL, BODY, "POST", new HttpOptions( true,true,false)); 
      System.out.println("   Y        N       Y       POST  ");
      RT.makeRequest( HTTPSURL, BODY, "POST", new HttpOptions( true,false,true)); 
      System.out.println("   Y        N       N       POST  ");
      RT.makeRequest( HTTPSURL, BODY, "POST", new HttpOptions( true,false,false)); 
      System.out.println("   N        Y       Y       POST  ");
      RT.makeRequest( HTTPSURL, BODY, "POST", new HttpOptions( false,true,true)); 
      System.out.println("   N        Y       N       POST  ");
      RT.makeRequest( HTTPSURL, BODY, "POST", new HttpOptions( false,true,false)); 
      System.out.println("   N        N       Y       POST  ");
      RT.makeRequest( HTTPSURL, BODY, "POST", new HttpOptions( false,false,true)); 
      System.out.println("   N        N       N       POST  ");
      RT.makeRequest( HTTPSURL, BODY, "POST", new HttpOptions( false,false,false)); 
      System.out.println("   Y        Y       Y       GET  ");
      RT.makeRequest( HTTPSURL, "", "GET", new HttpOptions( true,true,true)); 
      System.out.println("   Y        Y       N       GET  ");
      RT.makeRequest( HTTPSURL, "", "GET", new HttpOptions( true,true,false)); 
      System.out.println("   Y        N       Y       GET  ");
      RT.makeRequest( HTTPSURL, "", "GET", new HttpOptions( true,false,true)); 
      System.out.println("   Y        N       N       GET  ");
      RT.makeRequest( HTTPSURL, "", "GET", new HttpOptions( true,false,false)); 
      System.out.println("   N        Y       Y       GET  ");
      RT.makeRequest( HTTPSURL, "", "GET", new HttpOptions( false,true,true)); 
      System.out.println("   N        Y       N       GET  ");
      RT.makeRequest( HTTPSURL, "", "GET", new HttpOptions( false,true,false)); 
      System.out.println("   N        N       Y       GET  ");
      RT.makeRequest( HTTPSURL, "", "GET", new HttpOptions( false,false,true)); 
      System.out.println("   N        N       N       GET  ");
      RT.makeRequest( HTTPSURL, "", "GET", new HttpOptions( false,false,false)); 
      
    } catch( Exception e ) {
      System.out.println( "Exception in main: " + e.toString() );
      e.printStackTrace();
    }
  }

  public static class HttpOptions {
    public boolean doInput = false;
    public boolean doOutput = false;
    public boolean followRedirects = true;

    public HttpOptions() {}

    public HttpOptions( boolean i, boolean o, boolean r ) {
      this.doInput = i;
      this.doOutput = o;
      this.followRedirects = r;
    }
  }

  public String makeRequest( String url, String content, String method, HttpOptions options ) {
    ByteArrayInputStream byteStream = null;
    HttpURLConnection connection = null;
    String response = null;
    String encoding = "UTF-8";

    char[] buffer = new char[BUFFER_SIZE];
    int readSize = 0;
    StringBuilder retVal = new StringBuilder();
    int c = 0;
    int rc = 0;
    String rm = null;
    String location = null;
    OutputStreamWriter writer = null;
    OutputStream os = null;
    InputStreamReader reader = null;
    InputStreamReader responseReader = null;

    try {

      URL endpoint = new URL( url );
      connection = (HttpURLConnection)endpoint.openConnection();

      connection.setRequestMethod( method );
      if( content != null && content.length() > 0 ) {
        connection.setRequestProperty("Content-Type","application/json; charset=utf-8" );
        connection.setRequestProperty("Content-Length",""+content.length());
      }
      connection.setInstanceFollowRedirects(options.followRedirects);
      connection.setDoInput(options.doInput);
      connection.setDoOutput(options.doOutput);

      if( options.doOutput ) {
        os = connection.getOutputStream();
        byteStream = new ByteArrayInputStream(content.getBytes(encoding));
        writer = new OutputStreamWriter(os, encoding);
        reader = new InputStreamReader(byteStream, encoding);
        while ((readSize = reader.read(buffer, 0, BUFFER_SIZE)) != -1) {
          writer.write(buffer, 0, readSize);
        }

        writer.flush();
        writer.close();
      }

      if( options.doInput ) {
        rc = connection.getResponseCode();
        rm = connection.getResponseMessage();
      }
      location = connection.getHeaderField("Location");
      System.out.println("Location: " + (location == null ? "null!" : location) );

      System.out.println( "rc/rval = " + rc + "/" + rm );

      if( rc / 2 != 100 ) {
        //error
        System.out.println( "Error response: " + rc );
      } else {
        //success
        System.out.println( "Success response: " + rc );
      }
     
      InputStream responseInputStream = null;

      if( options.doInput ) {
        responseInputStream = connection.getInputStream();
        responseReader = new InputStreamReader(responseInputStream,encoding);
        while((c = responseReader.read()) != -1 ) {
          char charin = (char)c;
          retVal.append(charin);
        }
      }
    }
    catch( IOException ioe ) {
      System.out.println( "Exception: " + ioe.toString() );
      ioe.printStackTrace();
    } catch( Exception ex ) {
      System.out.println( "Exception: " + ex.toString() );
      ex.printStackTrace();
    }
    finally {
      try {
        if( writer != null) writer.close();
        if( responseReader != null) responseReader.close();
        if( connection != null ) connection.disconnect();
      }
      catch (IOException ioe2) {
        System.out.println("Could not close streams");
      }
    }

    return retVal.toString();
  }
}
