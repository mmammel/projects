import java.util.Calendar;

public class TimeoutCache<K,V> {

  public TimeoutCache()
  {
    this(300000L); //5 minutes
  }

  public TimeoutCache( long timeout )
  {
    this(timeout,LRUCache.DEFAULT_SIZE); //100 items
  }

  public TimeoutCache( long timeout, int maxItems )
  {
    this.timeoutMillis = timeout;
    this.cache = new LRUCache<K,Wrapper<V>>(maxItems);
  }

  private long timeoutMillis = 300000L; // 5 mins.

  private LRUCache<K,Wrapper<V>> cache = null;

  /**
   * Note that we *do not* reset the timestamp on the wrapper if we
   * find a valid value.  This will force a refresh in a semi-predictable
   * amount of time.
   * @param key
   * @return
   */
    public synchronized V get(K key)
    {
        V retVal = null;
        long now = Calendar.getInstance().getTimeInMillis();
        Wrapper<V> wrapper = null;

        if( (wrapper = this.cache.get(key)) != null )
        {
          if( now - wrapper.timestamp <= this.timeoutMillis )
          {
              retVal = wrapper.getItem();
          }
          else
          {
            this.cache.remove(key);
          }
        }

      return retVal;
    }

    public synchronized void put(K key, V value)
    {
    this.cache.put(key, new Wrapper(value));
    }

    private static class Wrapper<K> {
      long timestamp;
      K item;

      public K getItem() { return this.item; }

      public Wrapper(K item) {
        this.item = item;
        timestamp = Calendar.getInstance().getTimeInMillis();
      }
    }
}
