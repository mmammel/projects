import java.util.*;
import java.util.concurrent.locks.*;

public class ReadWriteCache {
  private final Map<String,String> cache = new HashMap<String,String>();
  private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
  private final Lock readLock = rwl.readLock();
  private final Lock writeLock = rwl.writeLock();

  public String get( String key )
  {
    String retVal = null;

    readLock.lock();
    System.out.println( "[T:" + Thread.currentThread().getId() + "] Read lock held for: " + key );
    try
    {
      retVal = this.cache.get(key);
    }
    finally
    {
      readLock.unlock();
    }

    return retVal;
  }

  public boolean containsKey( String key )
  {
    boolean retVal = false;

    readLock.lock();
    try
    {
      retVal = this.cache.containsKey(key);
    }
    finally
    {
      readLock.unlock();
    }

    return retVal;
  }

  public String put( String key, String value )
  {
    String retVal = null;

    writeLock.lock();
    System.out.println( "[T:" + Thread.currentThread().getId() + "] Write lock held for: [ " + key + ","+value + " ]" );
    try
    {
      retVal = this.cache.put(key,value);
    }
    finally
    {
      writeLock.unlock();
    }

    return retVal;
  }

}
