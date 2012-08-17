
import java.text.SimpleDateFormat;
import java.util.Date;

public class RateQueueTestTask implements Runnable
{
  private static SimpleDateFormat millis = new SimpleDateFormat("S");
  private RateQueue rateQueue;
  private long number;

  public RateQueueTestTask( RateQueue rq, long num )
  {
    this.rateQueue = rq;
    this.number = num;
  }

  public RateQueueTestTask( RateQueue rq )
  {
    this(rq,1000L);
  }

    private long randomSleepTime()
  {
    return Long.parseLong(millis.format(new Date()));
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
          Thread.sleep( 10L * this.randomSleepTime() );
        }
        catch( InterruptedException ie )
        {
            System.out.println( "Thread interrupted!!!" );
        }
      }
      else
      {
        System.out.println( "" + this.toString() + ": Rate too high!  Sleeping with [" + counter + "/" + this.number + "] complete..." );
        try
        {
          Thread.sleep( 10L * this.randomSleepTime() );
        }
        catch( InterruptedException ie )
        {
            System.out.println( "Thread interrupted!!!");
        }
      }
    }
  }
}