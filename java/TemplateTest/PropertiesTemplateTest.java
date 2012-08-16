import java.util.Properties;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.net.URL;

public class PropertiesTemplateTest extends AbstractTemplateTest<Properties>
{
    protected Properties getDataFromFile( File f ) throws IOException
    {
		Properties retVal = new Properties();
        retVal.load( new FileInputStream(f) );
        retVal.setProperty(TIMESTAMP_KEY, "" + new Date().getTime());
        return retVal;
    }

    protected long getTimeStamp( Properties p )
    {
    	return Long.parseLong( p.getProperty( TIMESTAMP_KEY ) );
    }


	protected URL getDataURL( String s )
	{
		return PropertiesTemplateTest.class.getResource("foobar.properties");
	}
}