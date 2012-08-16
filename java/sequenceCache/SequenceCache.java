import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class SequenceCache
{
  protected int mMaxSize = -1;
  protected int mCurrentSize = -1;

  protected String mName = null;
  
  public static final int DEFAULT_SIZE = 128;
  public static final String SEQ_INDEX = "SEQUENCE_INDEX";
  
  protected Cache mInnerCache = null;
  
  private long mCurrentSequenceKey = 0L;
  
  static
  {
    // TODO: This should be done in a base calss common with all types of caches, not just the sequence cache.
    CacheManager.create("C:\\Documents and Settings\\max_mammel\\My Documents\\projects\\java\\sequenceCache\\ehcache.xml");
  }
  
  protected CacheManager cacheManager = CacheManager.getInstance();
  private Object mCacheLock = new Object();

  public String toString()
  {
    StringBuffer buff = new StringBuffer();
    SequenceCacheSegment seg = null;
    SequenceDescriptor sequence_descriptor = null;
    List seqidx = null;
    int idx = 0;
    
    synchronized( this.mCacheLock )
    {
      seqidx = this.getSequenceIndex();
      
      for( Iterator iter = seqidx.iterator(); iter.hasNext(); )
      {
        sequence_descriptor = (SequenceDescriptor)iter.next();
        seg = this.getSegmentFromDescriptor( sequence_descriptor );
        buff.append("\nSegment[" + idx++ + "]:" ).append( seg.toString() );
      }
    }
    
    buff.append("\n");
    return buff.toString();
  }
  
  public int getMaxSize()
  {
    return this.mMaxSize;
  }

  public int getCurrentUsed()
  {
    return this.mCurrentSize;
  }

  public int getCurrentFree()
  {
    return this.mMaxSize - this.mCurrentSize;
  }

  public SequenceCache( String name )
  {
    this.mName = name;
    
    if( !this.cacheManager.cacheExists(this.mName) )
    {
      // create a cache with the default configuration, shouldn't happen.
      cacheManager.addCache(this.mName );
    }
    
    this.mInnerCache = cacheManager.getCache(this.mName);
    
    if( !this.mInnerCache.isKeyInCache(SEQ_INDEX) )
    {
      this.mInnerCache.put(new Element( SEQ_INDEX, new ArrayList()));
    }
  }

  /**
   * One of the two public entry points, make sure this is
   * synchronized
   * @param sequence
   */
  public void insert( List sequence )
  {
    this.insert(sequence, null, null );
  }
  
  /**
   * One of the two public entry points, make sure this is
   * synchronized
   * @param sequence
   */
  public void insert( List sequence, SequenceCacheItem first, SequenceCacheItem last )
  {
    SequenceCacheSegment toAdd = new SequenceCacheSegment(sequence, first, last);
    
    toAdd.getDescriptor().setSequenceKey(new Long(this.mCurrentSequenceKey++).toString());
    
    synchronized( this.mCacheLock )
    {
      this.addSegment(toAdd);
    }
  }
  
  public SequenceCacheQueryResult query( SequenceDescriptor query )
  {
    SequenceCacheQueryResult retVal = new SequenceCacheQueryResult();
    SequenceCacheItem start = query.getFirst();
    SequenceCacheItem end   = query.getLast();
    SequenceDescriptor tempDescriptor = null;
    List seqidx = null;
    int comparison = SequenceDescriptor.UNDEFINED;
    retVal.setDescriptor(SequenceCacheQueryResult.NO_DATA);
    retVal.setFront(start);
    retVal.setBack(end);
    
    SequenceCacheSegment tempSegment = null;
    
    synchronized( this.mCacheLock )
    {
      seqidx = this.getSequenceIndex();
      
      for( Iterator iter = seqidx.iterator(); iter.hasNext(); )
      {
        tempDescriptor = (SequenceDescriptor)iter.next();
        
        comparison = query.compareTo(tempDescriptor);
        
        switch( comparison )
        {
          case SequenceDescriptor.CONTAINED:
            tempSegment = this.getSegmentFromDescriptor(tempDescriptor);
            retVal.setDescriptor(SequenceCacheQueryResult.COMPLETE_DATA);
            retVal.setCachedData(tempSegment.midList(start, end.successor()));
            break;
          case SequenceDescriptor.FRONT_OVERLAP:
            tempSegment = this.getSegmentFromDescriptor(tempDescriptor);
            retVal.setDescriptor(SequenceCacheQueryResult.POST_QUERY_DATA);
            retVal.setFront(start);
            retVal.setBack(tempSegment.getFirst());
            retVal.setCachedData(tempSegment.headList(end.successor()));
            break;
          case SequenceDescriptor.END_OVERLAP:
            tempSegment = this.getSegmentFromDescriptor(tempDescriptor);
            retVal.setDescriptor(SequenceCacheQueryResult.PRE_QUERY_DATA);
            retVal.setFront(tempSegment.getLast());
            retVal.setBack(end);
            retVal.setCachedData(tempSegment.tailList(start));
            break;
        }
      }
    }
    
    return retVal;
  }
  
  /**
   * 
   * @return
   */
  private List getSequenceIndex()
  {
    List retVal = null;
    
    retVal = (List)this.mInnerCache.get(SEQ_INDEX).getObjectValue();
    
    return retVal;
  }
  
  /**
   * 
   * @param descriptor
   * @return
   */
  private SequenceCacheSegment getSegmentFromDescriptor( SequenceDescriptor descriptor )
  {
    SequenceCacheSegment retVal = null;
    
    retVal = (SequenceCacheSegment)this.mInnerCache.get(descriptor.getSequenceKey()).getObjectValue();
    
    return retVal;
  }
  
  /**
   * Look at the newly added segment, traverse the rest of the tree
   * and see if other segments can be merged with it cumulatively.
   * Because this was done with every previously added segment, 
   * only need to try it on the new one.
   */
  private void addSegment(SequenceCacheSegment toAdd)
  {
    SequenceDescriptor tempDescriptor = null;
    SequenceDescriptor toAddDescriptor = toAdd.getDescriptor();
    List seqidx = this.getSequenceIndex();
    boolean addSegment = true;
    
    int comparison = SequenceDescriptor.UNDEFINED;
    
    for( Iterator iter = seqidx.iterator(); iter.hasNext(); )
    {      
      tempDescriptor = (SequenceDescriptor)iter.next();
      
      comparison = toAddDescriptor.compareTo(tempDescriptor);
      
      switch( comparison )
      {
        case SequenceDescriptor.SUPERSET:
          /**
           * toAdd completely contains temp:
           * toAdd: 2,3,4,5,6
           * temp: 3,4,5
           */
          this.mInnerCache.remove(tempDescriptor.getSequenceKey());
          iter.remove();
          break;
        case SequenceDescriptor.FRONT_OVERLAP:
          /**
           * toAdd contains some subset of temp, starting with
           * the first element:
           * toAdd: 2,3,4,5,6
           * temp: 5,6,7,8
           */
          toAdd.mergeTail(this.getSegmentFromDescriptor(tempDescriptor));
          toAddDescriptor.setLast(tempDescriptor.getLast());
          this.mInnerCache.remove(tempDescriptor.getSequenceKey());
          iter.remove();
          break;
        case SequenceDescriptor.END_OVERLAP:
          /**
           * toAdd contains some subset of temp, ending with
           * the last element:
           * toAdd: 2,3,4,5,6
           * temp: 1,2,3
           */
          toAdd.mergeHead(this.getSegmentFromDescriptor(tempDescriptor));
          this.mInnerCache.remove(tempDescriptor.getSequenceKey());
          iter.remove();
          break;
        case SequenceDescriptor.CONTAINED:
          /**
           * temp completely contains toAdd:
           * toAdd: 2,3,4,5,6
           * temp: 1,2,3,4,5,6,7
           */
          addSegment = false;
          break;
      }
    }
    
    if( addSegment )
    {
      seqidx.add(0, toAddDescriptor);
      this.mInnerCache.put(new Element(SEQ_INDEX, seqidx));
      this.mInnerCache.put(new Element(toAddDescriptor.getSequenceKey(), toAdd));
    }
  }
}
