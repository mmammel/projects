import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

 /**
  * Can assume that segments within a sequence cache are
  * non-overlapping.
  */
public class SequenceCacheSegment
{
  protected SortedSet mSegment = null;
  
  /**
   * Allow for gaps in the sequence data - so if a set of data is:
   * 2,3,4,    ,11,12,13,...
   * A query of (2,10) will miss the cache, but then cache
   * 2,3,4 with a virtual last value of 10.  Then a subsequent
   * query of 2,9 will return complete data consisting of 2,3,4
   */
  protected SequenceDescriptor mDescriptor = null;
  
  protected SequenceCacheSegment( List cacheObjs )
  {
    this( cacheObjs, null, null );      
  }
  
  protected SequenceCacheSegment( List cacheObjs, SequenceCacheItem vfirst, SequenceCacheItem vlast )
  {
    this.mDescriptor = new SequenceDescriptor();
    this.mSegment = new TreeSet(cacheObjs);
    this.mDescriptor.setFirst(vfirst == null ? (SequenceCacheItem)this.mSegment.first() : vfirst);
    this.mDescriptor.setLast(vlast == null ? (SequenceCacheItem)this.mSegment.last() : vlast);
  }
  
  /**
   * Get the first item in the segment
   * @return
   */
  protected SequenceCacheItem getFirst()
  {
    return this.mDescriptor.getFirst();
  }
  
  /**
   * Get the last item in the segment
   * @return
   */
  protected SequenceCacheItem getLast()
  {
    return this.mDescriptor.getLast();
  }
  
  /**
   * See if the segment contains an item
   * @param item
   * @return
   */
  protected boolean containsItem( SequenceCacheItem item )
  {
    boolean retVal = false;
    
    if( this.mSegment.contains( item ))
    {
      retVal = true;
    }
    else if( this.getFirst().compareTo(item) <= 0 && this.getLast().compareTo(item) >= 0 )
    {
      retVal = true;
    }
    
    return retVal;
  }
  
  /**
   * Get the portion of the segment such that all elements
   * are greather than or equal to the provided item.
   * @param item
   * @return
   */
  protected SortedSet tailSet( SequenceCacheItem item )
  {
    return this.mSegment.tailSet(item);
  }
  
  /**
   * Get the portion of the segment such that all elements 
   * are less than or equal to the provided item
   * @param item
   * @return
   */
  protected SortedSet headSet( SequenceCacheItem item )
  {
    SortedSet retVal = this.mSegment.headSet(item);
    return retVal;
  }
  
  /**
   * A List wrapper for headSet
   * @param item
   * @return
   */
  protected List headList( SequenceCacheItem item )
  {
    List retList = new ArrayList( this.headSet(item));
    return retList;
  }
  
  /**
   * A List wrapper for tailSet
   * @param item
   * @return
   */
  protected List tailList( SequenceCacheItem item )
  {
    List retList = new ArrayList( this.tailSet(item));
    return retList;
  }
  
  /**
   * Get the portion of the segment such that all elements are
   * greater than or equal to start, and less than or equal to end.
   * @param start
   * @param end
   * @return
   */
  protected SortedSet midSet( SequenceCacheItem start, SequenceCacheItem end )
  {
    SortedSet retVal = this.mSegment.subSet(start, end);
    return retVal;
  }
  
  /**
   * List wrapper for midset.
   * @param start
   * @param end
   * @return
   */
  protected List midList( SequenceCacheItem start, SequenceCacheItem end )
  {
    List retList = new ArrayList( this.midSet(start, end));
    return retList;
  }
  
  /**
   * The tail of this segment overlaps with the head of 
   * the argument segment.
   * @param segment
   */
  protected SortedSet mergeTail( SequenceCacheSegment segment )
  {
    this.mSegment.addAll(segment.tailSet(this.getLast()));
    
    this.adjustLast(segment.getLast());
    
    return this.mSegment;
  }
  
  /**
   * The tail of the arg segment overlaps with the head of
   * this segment.
   * @param segment
   * @return
   */
  protected SortedSet mergeHead( SequenceCacheSegment segment )
  {
    this.mSegment.addAll(segment.headSet(this.getFirst()));
    
    this.adjustFirst( segment.getFirst() );
    
    return this.mSegment;
  }
  
  protected void replaceAll( SequenceCacheSegment segment )
  {
    this.mSegment = segment.mSegment;
    this.adjustFirst(segment.getFirst());
    this.adjustLast(segment.getLast());
  }
  
  public String toString()
  {
    StringBuffer buff = new StringBuffer();
    boolean first = true;
    buff.append("[" );
    
    for( Iterator iter = this.mSegment.iterator(); iter.hasNext(); )
    {
      if( !first )
      {
        buff.append(",");
      }
      else
      {
        first = false;
      }
      
      buff.append(((SequenceCacheItem)iter.next()).toString() );
    }
    
    buff.append("] ( vf: " + this.getFirst() + ", vl: " + this.getLast() + " )");
    
    return buff.toString();
  }
  
  public void trim( int size )
  {
    // remove size items from the "top" of the tree (lowest in ordering)
    int count = 0;
    
    for( Iterator iter = this.mSegment.iterator(); iter.hasNext(); )
    {
      if( count++ < size )
      {
        iter.next();
        iter.remove();
      }
      else
      {
        break;
      }
    }
  }
  
  private void adjustFirst( SequenceCacheItem first )
  {
    if( first != null )
    {
      if( first.compareTo(this.getFirst()) < 0 )
      {
        this.mDescriptor.setFirst(first);
      }
    }      
  }

  private void adjustLast( SequenceCacheItem last )
  {
    if( last != null )
    {
      if( last.compareTo(this.getLast()) > 0 )
      {
        this.mDescriptor.setLast(last);
      }
    }  
  }
  
  public int getSize()
  {
    return this.mSegment.size();
  }

  public SequenceDescriptor getDescriptor()
  {
    return mDescriptor;
  }

  public void setDescriptor(SequenceDescriptor descriptor)
  {
    mDescriptor = descriptor;
  }
}
