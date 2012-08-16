import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

/**
 * Created by IntelliJ IDEA. User: mmammel Date: Feb 20, 2008 Time: 1:00:16 PM To change this template use File |
 * Settings | File Templates.
 */
public class QuickZip
{

  /**
   * Wrapper for ZipSingleFile that creates the result file name automatically
   * @param filename
   * @throws IOException
   */
  public static void zipSingleFile( String filename ) throws IOException
  {
    zipSingleFile( filename, getZipfileNameFromFile(filename));
  }

  /**
   * Code borrowed from http://www.exampledepot.com/egs/java.util.zip/CreateZip.html
   * @param filename
   * @param outputfile
   * @throws IOException
   */
  public static void zipSingleFile( String filename, String outputfile ) throws IOException
  {
    byte[] buf = new byte[1024];
    File toZip = new File( filename );
    FileInputStream in = null;
    ZipOutputStream out = null;

    if( !toZip.exists() || !toZip.canRead() )
    {
      System.out.println( "File: " + filename + " either does not exist or cannot be read" );
    }
    else
    {
      try
      {
        System.out.println( "Zipping file: " + filename + ", result file: " + outputfile );
        // Create the ZIP file
        out = new ZipOutputStream(new FileOutputStream(outputfile));

        // Compress the files
        in = new FileInputStream(toZip);

        // Add ZIP entry to output stream.
        out.putNextEntry(new ZipEntry(toZip.getName()));

        // Transfer bytes from the file to the ZIP file
        int len;
        while ((len = in.read(buf)) > 0) {
          out.write(buf, 0, len);
        }
      }
      finally
      {
        if( out != null )
        {
          out.closeEntry();
          out.close();
        }

        if( in != null )
        {
          in.close();
        }
      }
    }
  }

  /**
   * Given a file name attempt to create a nice zip file name from it.
   * @param filename
   * @return
   */
  private static String getZipfileNameFromFile( String filename )
  {
    String retVal = null;
    int idx = -1;

    if( filename != null )
    {
      if( (idx = filename.lastIndexOf(".")) != -1 )
      {
        retVal = filename.substring( 0, idx ) + ".zip";
      }
      else
      {
        retVal = filename + ".zip";
      }
    }

    return retVal;
  }
}