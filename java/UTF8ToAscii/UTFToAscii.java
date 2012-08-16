import java.io.*;
import java.net.URL;

public class UTFToAscii
{

  /*
   * Take a file as input, stream it, and produce a new file with
   * logical replacements for common unicode characters that we see
   * in descriptions and such.
   */
  public UTFToAscii()
  {
  }

  public void convert( String infile, String outfile )
  {
    String fileAsString = null;

    try
    {
      StringBuffer buff = new StringBuffer();
      URL source = new File( infile ).toURL();
      InputStreamReader reader = new InputStreamReader( source.openStream(), "UTF-8" );
      BufferedReader in = new BufferedReader( reader );

      String line;
      while( (line = in.readLine()) != null )
      {
        buff.append( line );
      }

      fileAsString = buff.toString();
      char[] fileChars = fileAsString.toCharArray();
      char readChar = '\u0000';
      for( int i = 0; i < fileChars.length; i++ )
      {
        readChar = fileChars[i];
        if( readChar > '\u007F' )
        {
          System.out.println( "Found one: U+" + Integer.toHexString((int)readChar) );
        }
      }
    }
    catch( Exception ioe )
    {
      System.out.println( "Caught an exception: " + ioe.toString() );
    }

  }

  public static void main( String [] args )
  {
    String infile = args[0];
    String outfile = args.length > 1 ? args[1] : null;

    UTFToAscii UTA = new UTFToAscii();
    UTA.convert( infile, outfile );
  }

}