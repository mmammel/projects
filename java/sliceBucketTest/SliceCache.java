
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.constructs.blocking.BlockingCache;

public class SliceCache
{
  //private static Trace trace = Trace.getInstance(SequenceCache.class);
  private CacheManager cacheManager = CacheManager.getInstance();
  private BlockingCache innerCache = null;
  private String name;

  static
  {
    // TODO: This should be done in a base calss common with all types of
    // caches, not just the sequence cache.
    // CacheManager.create("C:\\Documents and Settings\\max_mammel\\My
    // Documents\\projects\\java\\sequenceCache\\ehcache.xml");
    CacheManager.create("C:\\Documents and Settings\\max_mammel\\My Documents\\projects\\java\\sequenceCache\\ehcache.xml");
  }

  public SliceCache(String name)
  {
    this.name = name;

    if (!this.cacheManager.cacheExists(name))
    {
      // create a cache with the default configuration, shouldn't happen.
      cacheManager.addCache(name);
    }
    
    this.init();
  }

  private void init() throws CacheException
  {
    synchronized( this.getClass() )
    {
      Ehcache cache = cacheManager.getEhcache(this.name);
      
      if( !(cache instanceof BlockingCache) )
      {
        BlockingCache newBlockingCache = new BlockingCache( cache );
        cacheManager.replaceCacheWithDecoratedCache(cache, newBlockingCache );
      }
      
      this.innerCache = (BlockingCache)cacheManager.getEhcache(this.name);
    }
  }
  
  /**
   * Given a slice, retrieve the bucket it belongs to.
   * Might return a NULL bucket (check state)
   * @param slice
   * @return
   */
  public SliceBucket getBucketBySlice( Slice slice )
  {
    return this.getBucketByTime( slice.getEndTime(), slice.getHostName(), slice.getJvmID(), slice.getArchiveFolder() );
  }

  /**
   * Get a bucket given a time, hostname, and jvmid
   * @param time
   * @param host
   * @param jvm
   * @return
   */
  public SliceBucket getBucketByTime( long time, String host, String jvm, String archiveFolder )
  {
    SliceBucket retVal = null;
    SliceBucketKey key = new SliceBucketKey( time, host, jvm, archiveFolder );
    Element element = this.innerCache.get( key );

    if( element != null )
    {
      retVal = (SliceBucket)element.getObjectValue();
    }
    else
    {
      // create a NULL bucket
      retVal = new SliceBucket( time, host, jvm, archiveFolder );
      retVal.setState(SliceBucket.NULL);
    }

    return retVal;
  }

  /**
   * Get the bucket immediately following this bucket, or null if none exists.
   * @param bucket
   * @return
   */
  public SliceBucket getNextBucket( SliceBucket bucket )
  {
    return this.getBucketByTime(SliceBucket.getNextBucketNumber(bucket.getBucketNumber()),
                                bucket.getHost(), bucket.getJvm(), bucket.getArchiveFolder());
  }

  /**
   * Add a new bucket to the cache.
   * Bucket is assumed to have a state of FILLED
   * @param bucket
   */
  public void insertBucket( SliceBucket bucket )
  {
    this.innerCache.put( new Element( bucket.getKey(), bucket ) );
  }

  public SliceCacheWriter getWriter()
  {
    return new SliceCacheWriter(this);
  }

  public SliceCacheReader getReader( Slice protoSlice )
  {
    return new SliceCacheReader(protoSlice, this);
  }
}
