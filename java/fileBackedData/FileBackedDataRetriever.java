import java.io.File;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: mammelma
 * Date: Jan 26, 2009
 * Time: 3:49:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface FileBackedDataRetriever<T>
{
    public static final String TIMESTAMP_KEY = "__tImEsTaMp__";
    public T getDataFromFile( File f ) throws IOException;
    public long getTimeStamp( T t );
    public void resetTimeStamp( T t );
}
