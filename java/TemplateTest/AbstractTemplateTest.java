import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.net.URL;
import java.net.URISyntaxException;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;

public abstract class AbstractTemplateTest<T> implements TemplateTest<T>
{
    protected Map<String,T> dataCache;

    protected abstract T getDataFromFile( File f ) throws IOException;

    protected abstract long getTimeStamp( T t );

    protected abstract URL getDataURL( String str );

    public AbstractTemplateTest()
    {
		dataCache = new HashMap<String, T>();
	}

    public T getData( String key ) throws IOException
    {
        T retVal = null;
        URL dataURL = this.getDataURL( key );

        if( dataURL != null )
        {
System.out.println( dataURL.toString() );
            synchronized( key.intern() )
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

                    if( (retVal = dataCache.get( key )) == null ||
                         this.getTimeStamp(retVal) < dataFile.lastModified() )
                    {
                        retVal = getDataFromFile( dataFile );
                        dataCache.put( key, retVal );
                    }
                }
            }
        }

        return retVal;
    }
}
