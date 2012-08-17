import org.apache.log4j.Logger;

import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * User: mammelma
 * Date: Jan 26, 2009
 * Time: 4:50:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class MultiLineFlatFileDataRetriever implements FileBackedDataRetriever<Map<String,String>>
{
    private static Logger logger = Logger.getLogger(MultiLineFlatFileDataRetriever.class);

    public static final String DEFAULT_ENTRY_DELIM = "={6,}";
    public static final String DEFAULT_KEY_DELIM = "-{6,}";

    private String mEntryDelim;
    private String mKeyDelim;

    public MultiLineFlatFileDataRetriever( String entryDelim, String keyDelim )
    {
        this.mEntryDelim = entryDelim;
        this.mKeyDelim = keyDelim;
    }

    public MultiLineFlatFileDataRetriever()
    {
        this.mEntryDelim = DEFAULT_ENTRY_DELIM;
        this.mKeyDelim = DEFAULT_KEY_DELIM;
    }

    public Map<String,String> getDataFromFile( File f ) throws IOException
    {
        String line,key = "",val = "";
        boolean readKey = true;
        Map<String,String> retVal = new HashMap<String,String>();
        BufferedReader reader = new BufferedReader( new FileReader( f ));

        try
        {
            while( (line = reader.readLine()) != null )
            {
                // Don't process comment lines.
                if( !line.startsWith("#") )
                {
                    if( line.matches(this.mEntryDelim))
                    {
                        readKey = true;
                    }
                    else if( line.matches( this.mKeyDelim) )
                    {
                        readKey = false;
                    }
                    else
                    {
                        if( readKey )
                        {
                            key = this.readEntry( reader, line, this.mKeyDelim );
                            readKey = false;
                        }
                        else
                        {
                            val = this.readEntry( reader, line, this.mEntryDelim );
                            readKey = true;
                            retVal.put( key, val );
                            key = "";
                        }
                    }
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

    private String readEntry( BufferedReader reader, String first, String stopLine ) throws IOException
    {
        String temp;
        StringBuffer retVal = new StringBuffer(first + "\n");

        while( ( temp = reader.readLine()) != null )
        {
            if( temp.matches( stopLine) )
            {
                break;
            }
            else
            {
                retVal.append( temp ).append( "\n" );
            }
        }

        temp = retVal.toString();

        if( temp.length() > 0 ) temp = temp.substring( 0, temp.length() - 1);

        return temp;
    }

    public long getTimeStamp( Map<String,String> m )
    {
      return Long.parseLong( m.get( TIMESTAMP_KEY ) );
    }

    public void resetTimeStamp( Map<String,String> m )
    {
        m.put( TIMESTAMP_KEY, "" + new Date().getTime() );
    }
}
