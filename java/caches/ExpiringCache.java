import java.util.Map;
import java.util.HashMap;
import java.util.Date;

/**
 * Very simplistic expiring cache.
 * X milliseconds after the last put, a get on any key will clear the entire cache.
 * As there is no limit on the size of the cache, one must be very careful
 * where and how this is used.
 *
 * User: mammelma
 * Date: Aug 3, 2010
 * Time: 2:01:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExpiringCache<K,V> {
    private long lifespan = 0L;
    private long lastUpdate = 0L;
    private Map<K,V> cache;

    private static final long DEFAULT_LIFESPAN = 600000L; //10 minutes
    private static final long MINIMUM_LIFESPAN = 60000L;  //1 minute
    public ExpiringCache()
    {
        this(DEFAULT_LIFESPAN);
    }

    public ExpiringCache(long lifespan)
    {
        this.lifespan = lifespan > MINIMUM_LIFESPAN ? lifespan : DEFAULT_LIFESPAN;
        this.cache = new HashMap<K,V>();
    }

    public synchronized void put(K k,V v)
    {
        this.lastUpdate = new Date().getTime();
        this.cache.put(k,v);
    }

    public synchronized V get(K k)
    {
        V retVal = null;
        long now = new Date().getTime();
        if( now - this.lifespan > this.lastUpdate )
        {
            this.cache.clear();
        }
        else
        {
            retVal = this.cache.get(k);
        }

        return retVal;
    }

    public synchronized int size()
    {
        return this.cache.size();
    }
}