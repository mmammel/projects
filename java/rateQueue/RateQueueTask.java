public class RateQueueTask implements Runnable
{
  private RateQueue rateQueue;
  private long number;

  public RateQueueTask( RateQueue rq, long num )
  {
    this.rateQueue = rq;
    this.number = num;
  }

  public RateQueueTask( RateQueue rq )
  {
    this(rq,1000L);
  }

  public void run()
  {
    long counter = 0L;
    while( counter < this.number )
    {
      if( this.rateQueue.test() )
      {
        System.out.println( "" + this.toString() + ": incrementing!" );
        counter++;
        try
        {
          Thread.sleep( 500 );
        }
        catch( InterruptedException ie )
        {
        }
      }
      else
      {
        System.out.println( "" + this.toString() + ": Rate too high!  Sleeping..." );
        try
        {
          Thread.sleep( 10000 );
        }
        catch( InterruptedException ie )
        {
        }
      }
    }
  }
}
