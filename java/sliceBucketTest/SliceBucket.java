import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;


public class SliceBucket implements Serializable
{
  /**
   *
   */
  private static final long serialVersionUID = 0L;
  public static int FILLED = 0; //in the cache, with slices
  public static int NULL = 1;   //not in the cache - nothing is known about it.

  /**
   * Keep as a power of 2 for fast remainder calculations.
   * 0x400 -> ~17 minutes
   * 0x800 -> ~34 minutes
   * etc.
   *
   * Keep in mind that the interval choices are 30, 60, 120, and 300 seconds.
   * We want to keep the buckets small, but not so small that it is one bucket per slice.
   */
  static final int bucketSize = 0x400;

  SortedSet sequence = new TreeSet();
  long bucketNumber;
  String host;
  String jvm;
  String archiveFolder;
  int state = NULL;

  public static long getBucketNumber( long time )
  {
    return time - (time & (bucketSize - 1));
  }

  public static long getBucketNumber( Slice slice )
  {
    return getBucketNumber( slice.getEndTime() );
  }

  public static long getNextBucketNumber( long time )
  {
    return (time - (time & (bucketSize - 1))) + bucketSize;
  }

  public static long getNextBucketNumber( Slice slice )
  {
    return getNextBucketNumber( slice.getEndTime() );
  }

  public static long getBucketEndTime( Slice slice )
  {
    return getBucketEndTime(slice.getEndTime());
  }

  public static long getBucketEndTime( long time )
  {
    return getBucketNumber(time) + bucketSize - 1;
  }

  public SliceBucket( long endTime, String h, String j, String a )
  {
    this.bucketNumber = getBucketNumber(endTime);
    this.host = h;
    this.jvm = j;
    this.archiveFolder = a;
  }

  /**
   * Don't insert the slice by default
   * @param slice
   */
  public SliceBucket( Slice slice )
  {
    this( slice, false );
  }

  /**
   * Create a new bucket with a slice, with an option to
   * add the slice at the same time.
   * @param slice
   * @param insert
   */
  public SliceBucket( Slice slice, boolean insert )
  {
    this( slice.getEndTime(), slice.getHostName(), slice.getJvmID(), slice.getArchiveFolder() );

    if( insert )
    {
      this.add(slice);
    }
  }

  /**
   * Add a single slice to the bucket.
   * @param slice
   */
  public void add(Slice slice)
  {
    sequence.add(slice);
  }

  public SliceBucketKey getKey()
  {
    return new SliceBucketKey( this.bucketNumber, this.host + this.jvm + this.archiveFolder );
  }

  public boolean isFilled()
  {
    return state == FILLED;
  }

  public boolean isNull()
  {
    return state == NULL;
  }

  public int getState()
  {
    return state;
  }

  public void setState(int state)
  {
    this.state = state;
  }

  public long getEndTime()
  {
    return bucketNumber + bucketSize - 1;
  }

  public String getHost()
  {
    return host;
  }

  public void setHost(String host)
  {
    this.host = host;
  }

  public String getJvm()
  {
    return jvm;
  }

  public void setJvm(String jvm)
  {
    this.jvm = jvm;
  }

  public long getStartTime()
  {
    return this.bucketNumber;
  }

  public long getBucketNumber()
  {
    return bucketNumber;
  }

  public void setBucketNumber(long bucketNumber)
  {
    this.bucketNumber = bucketNumber;
  }

  public int getSliceCount()
  {
    return sequence.size();
  }

  public Iterator getSliceIterator()
  {
    return this.sequence.iterator();
  }

  public boolean containsSlice( Slice slice )
  {
    return this.sequence.contains(slice);
  }

  public String getArchiveFolder()
  {
    return archiveFolder;
  }

  public void setArchiveFolder(String archiveFolder)
  {
    this.archiveFolder = archiveFolder;
  }
}
