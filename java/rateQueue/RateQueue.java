import java.util.concurrent.atomic.AtomicLong;
import java.util.*;

/**
 * User: mammelma
 * Date: Jul 21, 2010
 * Time: 4:09:00 PM
 *
 * This class can be used as a governor for processes that perform a lot of
 * operations yet cannot exceed some specified threshold of the form executions/time.
 */
public class RateQueue
{
  private long bucketSize;  // In milliseconds
  private long buckets = -1L;
  private long limit = -1L;
  private Queue<Long> mQueue = new LinkedList<Long>();
  private Timer daemon;
  private AtomicLong currentTotal = new AtomicLong(0L);
  private long previousTotal = 0L;
  private List<Long> history = new ArrayList<Long>();
  private boolean historyEnabled = false;

  public RateQueue( long events, long minutes, long bucketSize )
  {
    this.bucketSize = bucketSize;

    if( (minutes * 60000L) < this.bucketSize )
    {
        throw new IllegalArgumentException("Bucket size of " + this.bucketSize + " is too large for the given rate of " + (events / minutes) + "e/m");
    }

    this.buckets = ((minutes * 60000L) / this.bucketSize) - 1;
    if( this.buckets < 0 ) this.buckets = 0;

    this.limit = events;

    for( int i = 0; i < this.buckets; i++ )
    {
      this.mQueue.offer( 0L );
    }

    daemon = new Timer();
    daemon.scheduleAtFixedRate( new RateQueueTask(this), new Date(), this.bucketSize );
  }

  public boolean test()
  {
    for(;;)
    {
      long curr = this.currentTotal.get();
      if( curr >= this.limit )
      {
        return false;
      }
      else if( this.currentTotal.compareAndSet(curr,curr+1) )
      {
        return true;
      }
    }
  }

  public void shutdown()
  {
    this.daemon.cancel();
  }

  private void advance()
  {
    Long last = this.mQueue.poll();

    long curr;
    long temp = this.previousTotal;

    for(;;)
    {
      curr = this.currentTotal.get();
      if( this.currentTotal.compareAndSet( curr, last == null ? 0L : curr - last ) )
      {
        this.previousTotal = last == null ? 0L : curr - last;
        if( historyEnabled ) this.history.add( last == null ? curr : last );
        break;
      }
    }

    if( last != null ) this.mQueue.offer( curr - temp );
  }

  private class RateQueueTask extends TimerTask
  {
    private RateQueue queue;
    private RateQueueTask( RateQueue queue )
    {
      this.queue = queue;
    }

    public void run()
    {
      this.queue.advance();
    }
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append( "[Limit:").append(this.limit).append("]\n[Buckets:").append(this.buckets).append("]\n");
    sb.append( "[Total:").append(this.currentTotal.get()).append("]\n");
    sb.append( "[Previous:").append(this.previousTotal).append("]\n");

    if( this.mQueue.size() > 0 )
    {
      for( Long val : this.mQueue )
      {
        sb.append("[").append(val).append("]");
      }
    }
    else
    {
        sb.append("[*]");
    }

    sb.append("+[").append(this.currentTotal.get() - this.previousTotal).append("]");

    return sb.toString();
  }

  public void setHistoryEnabled( boolean historyEnabled )
  {
      this.historyEnabled = historyEnabled;
  }

  public String getHistoryString()
  {
    String retVal = "History not enabled for this rate queue.";
    if( historyEnabled )
    {
      StringBuilder sb = new StringBuilder();
      for( Long l : this.history )
      {
        sb.append("[").append(l).append("]");
      }

      retVal = sb.toString();
    }

      return retVal;
  }

  public List<Long> getHistory()
  {
      return this.historyEnabled ? this.history : null;
  }

    public long getBuckets() {
        return buckets;
    }

    public long getBucketSize() {
        return bucketSize;
    }

    public long getLimit() {
        return limit;
    }

    public void clearHistory()
  {
      this.history.clear();
  }
}
