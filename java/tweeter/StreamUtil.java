import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;


/**
 * A helper class for streaming data from input to output, useful for streaming
 * data to integration partners, or streaming large documents to a browser
 * @author mammelma
 */
public class StreamUtil {
    private static final int BUFFER_SIZE = 1024;
    private static final String DEFAULT_ENCODING = "UTF-8";

    /*
     * Three wrappers for the core method.
     * @param output
     * @param input
     */

    public static void stream(OutputStream output, InputStream input) {
      stream(output, input, DEFAULT_ENCODING);
    }

    public static void stream(OutputStream output, String input) {
      stream(output, input, DEFAULT_ENCODING);
    }

    public static void stream(OutputStream output, String input, String charset) {
      try
      {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(input.getBytes(charset));
        stream(output, byteStream, charset);
      }
      catch( UnsupportedEncodingException uee )
      {
        System.out.println("Unsupported encoding: " + charset + " in the StreamUtil: " + uee.toString());
      }
    }

    /**
     * The root stream method, takes an input and output stream, and simply
     * reads from input and writes to output.  Nice because it handles all of the
     * IOExceptions for you (important point - as this will log the error but simply
     * not write to the output stream, so be careful - might want to write a version that throws the error).
     * @param output
     * @param input
     * @param charset
     */
    public static void stream(OutputStream output, InputStream input, String charset) {
      char[] buffer = new char[BUFFER_SIZE];
      int readSize = 0;
      OutputStreamWriter writer = null;
      InputStreamReader reader = null;

      try {
        writer = new OutputStreamWriter(output, charset);
        reader = new InputStreamReader(input, charset);
        while ((readSize = reader.read(buffer, 0, BUFFER_SIZE)) != -1) {
          writer.write(buffer, 0, readSize);
        }
        writer.flush();
      }
      catch (IOException ioe) {
        System.out.println("Caught an exception: " + ioe.toString() );
      }
      finally {
        try {
          writer.close();
          reader.close();
        }
        catch (IOException ioe2) {
          System.out.println("Could not close streams");
        }
      }
    }

    /**
     * given a string, return the first portion up to the last space
     * in the first element and the rest (excluding the space) in the 
     * second element.
     */ 
    private static String [] prettyBreak( String input ) {
      String [] retVal = new String [2];
      retVal[0] = "";
      retVal[1] = "";
      int spaceIdx = -1;
      if( input != null && (spaceIdx = input.lastIndexOf( ' ')) > -1 ) {
        retVal[0] = input.substring(0, spaceIdx);
        if( (spaceIdx + 1) < input.length() ) {
          retVal[1] = input.substring( spaceIdx + 1 );
        }
      } 

      return retVal;
    }

    /**
     * Utility to get a list of strings, none longer than limit, 
     * from streamed input.  Usefule for chaining tweets together.
     * @param input
     * @param limit
     */
    public static List<String> stringsFromStream( InputStream input, int limit ) {
      return stringsFromStream(input, limit, DEFAULT_ENCODING);
    }

    /**
     * Utility to get a list of strings from a streamed input. 
     * @param input
     * @param limit
     * @param charset
     * @return
     */
    public static List<String> stringsFromStream(InputStream input, int limit, String charset) {
      int c;

      StringBuilder sb = new StringBuilder();
      InputStreamReader reader = null;
      List<String> retVal = new ArrayList<String>();
      int counter = 0;
      String [] prettySplit = null;

      try {
        reader = new InputStreamReader(input, charset);

        while ((c = reader.read()) != -1) {
          char charin = (char) c;
          counter++;
          sb.append(charin);

          if( counter == limit ) {
            prettySplit = prettyBreak( sb.toString() );
            retVal.add( prettySplit[0] );
            sb = new StringBuilder( prettySplit[1] );
            counter = prettySplit[1].length();
          }
        }

        if( counter > 0 ) {
          retVal.add( sb.toString() );
        }
      }
      catch (IOException ioe) {
        System.out.println("Caught an exception trying to read the input stream: " + ioe.toString() );
      }
      finally {
        try {
          reader.close();
        }
        catch (IOException ioe2) {
          System.out.println("Couldn't close input stream: " + ioe2.toString());
        }
      }

      return retVal;
    }

    /**
     * Utility to get a string from a streamed input. Useful for getting a
     * String version of a request XML or something.
     * @param input
     * @return
     */
    public static String stringFromStream(InputStream input) {
      return stringFromStream(input, DEFAULT_ENCODING);
    }

    /**
     * Utility to get a string from a streamed input. Useful for getting a
     * String version of a request XML or something.
     * @param input
     * @return
     */
    public static String stringFromStream(InputStream input, String charset) {
      int c;

      StringBuilder sb = new StringBuilder();
      InputStreamReader reader = null;

      try {
        reader = new InputStreamReader(input, charset);

        while ((c = reader.read()) != -1) {
          char charin = (char) c;
          sb.append(charin);
        }
      }
      catch (IOException ioe) {
        System.out.println("Caught an exception trying to read the input stream: " + ioe.toString() );
      }
      finally {
        try {
          reader.close();
        }
        catch (IOException ioe2) {
          System.out.println("Couldn't close input stream: " + ioe2.toString());
        }
      }

      return sb.toString();
    }

    /**
     * Util for getting an inputstream from a string, note that this is done inline
     * in the "stream" method, this is just a wrapper for external use.
     * @param source
     * @return
     */
    public static InputStream streamFromString(String source) {
      return streamFromString(source, DEFAULT_ENCODING);
    }

    /**
     * Util for getting an inputstream from a string, note that this is done inline
     * in the "stream" method, this is just a wrapper for external use.
     * @param source
     * @return
     */
    public static InputStream streamFromString(String source, String encoding) {
    InputStream retVal = null;
    try
    {
      retVal = new ByteArrayInputStream(source.getBytes(encoding));
    }
    catch( UnsupportedEncodingException uee )
    {
      System.out.println("Unsupported encoding: " + encoding + " in stream service, streamFromString: " + uee.toString());
    }

    return retVal;
    }
}
