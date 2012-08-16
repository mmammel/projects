
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SliceCacheReader
{
  private SliceCache mSliceCache = null;
  //  Still keep the timeframe separate, in case the proto slice changes.
  private long startTime = 0L;
  private long endTime = 0L;

  private Iterator mCurrentIterator = null;
  private SliceBucket mCurrentBucket = null;
  private Slice mProtoSlice = null;
  private SliceCacheWriter mCacheWriter = null;

  private int mState = STATE_NEW_BUCKET;

  //private static Trace trace = Trace.getInstance(SliceCacheReader.class);

  private static final int STATE_NEW_BUCKET = -1;
  private static final int STATE_CACHE_READ = 0;
  private static final int STATE_DB_READ = 1;
  private static final int STATE_DONE = 2;

  public SliceCacheReader( Slice proto, SliceCache cache )
  {
    this.mSliceCache = cache;
    this.mProtoSlice = proto;
    this.startTime = this.mProtoSlice.getStartTime();
    this.endTime = this.mProtoSlice.getEndTime();
    this.mCurrentBucket = this.mSliceCache.getBucketByTime(this.mProtoSlice.getStartTime(), this.mProtoSlice.getHostName(), this.mProtoSlice.getJvmID(), this.mProtoSlice.getArchiveFolder());
  }

  public Slice read() throws DataAccessException
  {
    return this.readInner();
  }

  /**
   * Recursive method that returns either null if we're done,
   * a slice from the DB, or a slice from the cache.
   * @return
   * @throws DataAccessException
   */
  public Slice readInner() throws DataAccessException
  {
    Slice retVal = null;

    switch( this.mState )
    {
      case STATE_NEW_BUCKET:
        retVal = this.handleNewBucket();
        break;
      case STATE_CACHE_READ:
        retVal = this.handleCacheRead();
        break;
      case STATE_DB_READ:
        retVal = this.handleDBRead();
        break;
      case STATE_DONE:
        retVal = null;
        break;
      default:
        //trace.log(TraceLevel.WARNING, "Cache reader in invalid state: " + this.mState );
        retVal = null;
        break;
    }

    return retVal;
  }

  /**
   * The current bucket has been assigned a new bucket.  Process it to figure out what state
   * we are in.
   * @throws DataAccessException
   */
  private Slice handleNewBucket() throws DataAccessException
  {
    Slice tempProto = null;
    long queryStart = 0L, queryEnd = 0L;

    if( this.mCurrentBucket.getStartTime() > this.endTime )
    {
      this.setState(STATE_DONE);
    }
    else if( this.mCurrentBucket.isNull() )
    {
      queryStart = this.mCurrentBucket.getStartTime();
      // this increment currentBucket to the appropriate place.
      this.mCurrentBucket = this.skipNulls(this.mCurrentBucket);
      queryEnd   = this.mCurrentBucket.getStartTime() - 1;  // the end of the previous bucket.
      tempProto = this.mProtoSlice.createProtoSlice();
      tempProto.setStartTime(queryStart);
      tempProto.setEndTime(queryEnd);
      //this.mCurrentIterator = new SQLDataAccessImpl().getSlicesByPrototype( tempProto );
      this.mCurrentIterator = SliceDataBase.getInstance().query( queryStart, queryEnd );
      this.mCacheWriter = new SliceCacheWriter( this.mSliceCache );
      this.setState(STATE_DB_READ);
    }
    else
    {
      if( this.mCurrentBucket.getSliceCount() > 0 )
      {
        this.mCurrentIterator = this.mCurrentBucket.getSliceIterator();
        this.setState(STATE_CACHE_READ);
      }
      else
      {
        // this increments currentBucket to the appropriate place.
        this.mCurrentBucket = this.skipEmpties(this.mCurrentBucket);
        this.setState(STATE_NEW_BUCKET);
      }
    }

    return this.readInner();
  }

  /**
   * handleNewBucket has found a bucket with slices in it and set up the
   * iterator for us.  Now just cycle through the iterator.
   * @return
   * @throws DataAccessException
   */
  private Slice handleCacheRead() throws DataAccessException
  {
    Slice retVal = null;
    Slice tempSlice = null;

    if( this.mCurrentIterator.hasNext() )
    {
      tempSlice = (Slice)this.mCurrentIterator.next();
      //could be first slice, check to see that it ends after the start time
      if( tempSlice.getEndTime() >= this.startTime && tempSlice.getStartTime() <= this.endTime )
      {
        System.out.println( "CACHE HIT!!!" );
        retVal = tempSlice;
      }
      else
      {
        retVal = this.readInner();
      }
    }
    else
    {
      this.mCurrentBucket = this.mSliceCache.getNextBucket(this.mCurrentBucket);
      this.setState(STATE_NEW_BUCKET);
      retVal = this.readInner();
    }

    return retVal;
  }

  /**
   * handleNewBucket has found a run of null buckets and set up the
   * SQLAccessImpl for us, set up a writer, and started the iterator.
   * Just cycle through all of the slices and fill the cache along the way.
   * @return
   * @throws DataAccessException
   */
  private Slice handleDBRead() throws DataAccessException
  {
    Slice retVal = null;
    Slice tempSlice = null;

    if( this.mCurrentIterator.hasNext() )
    {
      tempSlice = (Slice)this.mCurrentIterator.next();
      this.mCacheWriter.write(tempSlice);
      //could be first slice, check to see that it ends after the start time
      if( tempSlice.getEndTime() >= this.startTime && tempSlice.getStartTime() <= this.endTime )
      {
        System.out.println( "DB Hit.");
        retVal = tempSlice;
      }
      else
      {
        /**
         * We're either in the start of the first bucket, or at the end of the last bucket.
         * Important to keep cycling to fill the cache.
         * We'll eventually run out of slices and move on to the next bucket, which,
         * if this is the end of the last bucket will end up in a DONE state.
         */
        retVal = this.readInner();
      }
    }
    else
    {
      this.mCacheWriter.close();
      this.setState(STATE_NEW_BUCKET);
      retVal = this.readInner();
    }

    return retVal;
  }

  /**
   * the passed bucket is null, find all of the consecutive null
   * buckets until a non-null one is found, or the end of the requested
   * time frame is passed.
   * @param bucket
   * @return
   */
  private SliceBucket skipNulls( SliceBucket bucket )
  {
    SliceBucket searchBucket = this.mSliceCache.getNextBucket(bucket);

    while( searchBucket.isNull() && searchBucket.getStartTime() <= endTime )
    {
      searchBucket = this.mSliceCache.getNextBucket(searchBucket);
    }

    return searchBucket;
  }

  /**
   * The passed bucket is empty, iterate buckets until a null
   * or non-empty bucket is found.
   * @param bucket
   * @return
   */
  private SliceBucket skipEmpties( SliceBucket bucket )
  {
    SliceBucket searchBucket = this.mSliceCache.getNextBucket(bucket);

    while( !searchBucket.isNull() && searchBucket.getSliceCount() == 0 && searchBucket.getStartTime() <= endTime )
    {
      searchBucket = this.mSliceCache.getNextBucket(searchBucket);
    }

    return searchBucket;
  }

  private void setState( int state )
  {
    this.mState = state;
  }

}
