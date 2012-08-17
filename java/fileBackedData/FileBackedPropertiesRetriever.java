import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: mammelma
 * Date: Jan 26, 2009
 * Time: 4:47:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileBackedPropertiesRetriever implements FileBackedDataRetriever<Properties>
{
    private static Logger logger = Logger.getLogger(FileBackedPropertiesRetriever.class);

    public Properties getDataFromFile( File f ) throws IOException
    {
        InputStream is = new FileInputStream(f);
        Properties retVal = new Properties();

        try
        {
            logger.debug("Retrieving property data from file " + f.getName() + "...");
            retVal.load( is );
            logger.debug("...Done.");
        }
        finally
        {
            try
            {
                is.close();
            }
            catch( IOException ioe )
            {
               logger.error( "Caught an exception trying to close a property file stream for " + f.getName(), ioe );
            }
        }

        return retVal;
    }

    public long getTimeStamp( Properties p )
    {
      return Long.parseLong( p.getProperty( TIMESTAMP_KEY ) );
    }

    public void resetTimeStamp( Properties p )
    {
        p.setProperty( TIMESTAMP_KEY, "" + new Date().getTime() );
    }

}
