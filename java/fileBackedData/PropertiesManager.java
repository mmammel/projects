import org.apache.log4j.Logger;

import java.util.Properties;
import java.util.Date;
import java.net.URL;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Load properties from a properties file (now only in etc/common/properties/...)
 * based on a given name that matches the first part of the property file name.
 * User: mammelma
 * Date: Jan 19, 2009
 * Time: 9:07:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class PropertiesManager extends AbstractFileBackedDataManager<Properties>
{
    private static PropertiesManager theInstance = new PropertiesManager();
    private static final String CLIENT_BASE_PATH = "/etc/clients/";
    private static final String BASE_PATH = "/etc/common/properties/";

    private static Logger logger = Logger.getLogger(PropertiesManager.class);

    private PropertiesManager()
    {
        this.retriever = new FileBackedPropertiesRetriever();
    }

    public static PropertiesManager getInstance()
    {
        return theInstance;
    }

    public Properties getProperties( String propKey ) throws IOException
    {
        return this.getData( PropertiesManager.class.getResource( BASE_PATH + propKey + (propKey.endsWith(".properties") ? "" : ".properties" ) ) );
    }

    public Properties getClientProperties( String client ) throws IOException
    {
        return this.getClientProperties( client, client + ".properties" );
    }

    public Properties getClientProperties( String client, String propKey ) throws IOException
    {
        return this.getData( PropertiesManager.class.getResource( CLIENT_BASE_PATH + client + File.separator + propKey + (propKey.endsWith(".properties") ? "" : ".properties" ) ) );
    }
}
