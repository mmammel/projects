
/**
 * Created upon request by the SliceCache and given to the data access
 * layer when it needs to cache new slices.
 * TODO: Make poolable?
 * @author Max_Mammel
 *
 */
public class SliceCacheWriter
{
  //private static Trace trace = Trace.getInstance(SliceCacheWriter.class);
  private SliceCache mSliceCache = null;
  private SliceBucket mCurrentBucket = null;

  public SliceCacheWriter( SliceCache cache )
  {
    this.mSliceCache = cache;
  }

  /**
   * As slices should only be queried over some multiple of
   * bucket size, this needs to be called when the owner of the writer
   * is done writing to indicate the end of the last bucket.
   */
  public void close()
  {
    if( this.mCurrentBucket != null )
    {
      this.writeBucket(this.mCurrentBucket);
      this.mCurrentBucket = null;
    }
  }

  /**
   * To be called when a new slice is inserted into the system.
   * @param slice
   */
  public void writeOnInsert( Slice slice )
  {
    Slice proto = null;
    SliceCacheReader reader = null;
    SliceBucket bucket = this.mSliceCache.getBucketBySlice(slice);

    if( bucket.isNull())
    {
      // Trying to create a new bucket.  See if this would actually
      // be the first slice.
      if( slice.getStartTime() - bucket.getStartTime() > 29 )
      {
        // another slice could potentially fit in front of this slice.
        // in case the lastest bucket was dropped by the cache, let's fill
        // the last few buckets.
        proto = new Slice( slice.getHostName(), slice.getJvmID(), (bucket.getStartTime() - 5*SliceBucket.bucketSize), bucket.getEndTime() );
        reader = new SliceCacheReader( proto, this.mSliceCache );

        try
        {
          while( reader.read() != null );
        }
        catch( DataAccessException dae )
        {
          //trace.log(TraceLevel.WARNING, "Caught an exception: " + dae.toString() + " while reading last 5 buckets on insert." );
        }

        // now get the bucket again and add the slice.
        bucket = this.mSliceCache.getBucketBySlice(slice);
      }
      else
      {
        bucket = new SliceBucket(slice);
      }
    }

    bucket.add(slice);
    bucket.setState( SliceBucket.FILLED );
    this.writeBucket(bucket);
  }

  public void write( Slice slice )
  {
    long tempBucketNum = 0L;
    long sliceBucketNum = SliceBucket.getBucketNumber(slice);

    if( this.mCurrentBucket == null )
    {
      // first write, see if a bucket already exists in the cache.
      this.mCurrentBucket = this.getBucket(slice);
    }
    else
    {
      // We have an existing bucket.  Check to see if this slice should be in it.
      if( this.mCurrentBucket.getBucketNumber() != sliceBucketNum )
      {
        // we're done filling this bucket, write it to the cache.
        this.writeBucket(this.mCurrentBucket);
        tempBucketNum = SliceBucket.getNextBucketNumber(this.mCurrentBucket.getBucketNumber());

        // Loop past buckets that have no slices and write them to the cache.
        while( tempBucketNum != sliceBucketNum)
        {
          this.writeBucket(new SliceBucket(tempBucketNum, slice.getHostName(), slice.getJvmID(), slice.getArchiveFolder()));
          tempBucketNum = SliceBucket.getNextBucketNumber(tempBucketNum);
        }

        // get the appropriate bucket either from the cache or make a new one.
        this.mCurrentBucket = this.getBucket(slice);
      }
    }

    // Might be a new insert, so even if the bucket is filled, check for pre-existence
    // of the slice.
    if( !this.mCurrentBucket.isFilled() || !this.mCurrentBucket.containsSlice(slice) )
    {
      this.mCurrentBucket.add(slice);
    }
  }

  /**
   * Set the given bucket to FILLED and add it to the cache.
   * @param bucket
   */
  private void writeBucket( SliceBucket bucket )
  {
    bucket.setState(SliceBucket.FILLED );
    this.mSliceCache.insertBucket( bucket );
  }

  /**
   * If the bucket already exists, return it.  Otherwise make a new one that includes
   * the slice.
   * @param slice
   * @return
   */
  private SliceBucket getBucket( Slice slice )
  {
    SliceBucket bucket = null;
    SliceBucket retVal = null;

    bucket = this.mSliceCache.getBucketBySlice(slice);

    if( !bucket.isNull() )
    {
      if( !bucket.isFilled() )
      {
        // should never happen
        //trace.log(TraceLevel.WARNING, "A bucket exists for key: " + new SliceBucketKey(slice) + " but is not filled");
      }

      // don't actually do anything to the bucket, it is already filled.
      // We'll set the current bucket so that it is re-inserted and bumped
      // back to the top of the LFU chain.
      retVal = bucket;
    }
    else
    {
      retVal = new SliceBucket( slice );
    }

    return retVal;
  }
}
