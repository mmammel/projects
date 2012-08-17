import org.apache.log4j.Logger;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by IntelliJ IDEA.
 * User: mammelma
 * Date: Jan 26, 2009
 * Time: 4:28:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class DelimitedFlatFileDataRetriever implements FileBackedDataRetriever<Map<String,String>>
{
    public static final String DEFAULT_DELIMITER = ";";
    private String mDelimiter;
    private static Logger logger = Logger.getLogger(DelimitedFlatFileDataRetriever.class);

    public DelimitedFlatFileDataRetriever( String delim )
    {
        this.mDelimiter = delim;
    }

    public DelimitedFlatFileDataRetriever()
    {
        this.mDelimiter = DEFAULT_DELIMITER;
    }

    public Map<String,String> getDataFromFile( File f ) throws IOException
    {
        String line,key,val;
        Map<String,String> retVal = new HashMap<String,String>();
        String [] splitResult;
        BufferedReader reader = new BufferedReader( new FileReader( f ));

        try
        {
            while( (line = reader.readLine()) != null )
            {
                // Don't process comment lines.
                if( !line.startsWith("#") )
                {
                    splitResult = line.split( this.mDelimiter, 2);

                    if( splitResult.length != 2 )
                    {
                       // Don't bother with null mappings or blank lines.
                       continue;
                    }

                    key = this.processKey(splitResult[0]);
                    val = this.processKey(splitResult[1]);
                    retVal.put( key,val );
                }
            }
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch( IOException ioe )
            {
                logger.error( "Caught an exception trying to close stream for file " + (f == null ? "<null>" : f.getName()) );
            }
        }

        return retVal;
    }

    public long getTimeStamp( Map<String,String> m )
    {
      return Long.parseLong( m.get( TIMESTAMP_KEY ) );
    }

    public void resetTimeStamp( Map<String,String> m )
    {
        m.put( TIMESTAMP_KEY, "" + new Date().getTime() );
    }

    protected String processKey( String key )
    {
        return key;
    }

    protected String processValue( String val )
    {
        return val;
    }
}
