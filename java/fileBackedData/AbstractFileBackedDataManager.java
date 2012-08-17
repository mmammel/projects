import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

/**
 * User: mammelma
 * Date: Jan 21, 2009
 * Time: 6:14:29 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractFileBackedDataManager<T>
{
    protected Map<String,T> dataCache;

    protected FileBackedDataRetriever<T> retriever;

    private static Logger logger = Logger.getLogger(AbstractFileBackedDataManager.class);

    public AbstractFileBackedDataManager()
    {
    dataCache = Collections.synchronizedMap( new HashMap<String, T>() );
  }

    protected T getData( URL dataURL ) throws IOException
    {
        T retVal = null;
        String urlString;

        if( dataURL != null )
        {
            urlString = dataURL.toString();

            synchronized( urlString.intern() )
            {
                File dataFile;

                try {
                  dataFile = new File(dataURL.toURI());
                } catch(URISyntaxException e) {
                  dataFile = new File(dataURL.getPath());
                }

                // Probably redundant, but won't hurt
                if( dataFile.exists() )
                {
                    if( (retVal = dataCache.get( urlString )) == null ||
                         this.retriever.getTimeStamp(retVal) < dataFile.lastModified() )
                    {
                        logger.debug( "Cache missed!!  Re-loading data from file...");
                        retVal = this.retriever.getDataFromFile( dataFile );
                        this.retriever.resetTimeStamp(retVal);
                        dataCache.put( urlString, retVal );
                        logger.debug( "...Done." );
                    }
                }
            }
        }
        else
        {
            logger.error( "Attempted to retrieve file backed data with null URL");
        }

        return retVal;
    }
}
