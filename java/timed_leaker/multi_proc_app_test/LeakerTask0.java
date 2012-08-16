import java.util.TimerTask;
import java.util.Timer;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;

public class LeakerTask0 extends TimerTask
{

  public static final int DEFAULT_RUNS = 1000;
  public static final long DEFAULT_INTERVAL = 30000L;
  private List mLeakArray = null;
  private Map mLeakMap = null;
  private Timer mTimer = null;
  private long mInterval = DEFAULT_INTERVAL;  //default of 30 seconds
  private int mRunLimit = DEFAULT_RUNS;
  private int mRunCount = 0;

  public LeakerTask0()
  {
    mLeakArray = new ArrayList();
    mLeakMap   = new HashMap();
  }

 /**
  * Start the leaker with the default settings
  */
  public void startLeak()
  {
    this.startLeak( DEFAULT_RUNS, DEFAULT_INTERVAL );
  }

  /**
   * Start the leaker thread and stop leaking after iterations iterations.
   * If it is not the case that 0 < iterations, the thread will run the default 
   * number of times (1000).
   */
  public void startLeak( int iterations )
  {
    this.startLeak( iterations, DEFAULT_INTERVAL );
  }

  public void startLeak( int iterations, long interval )
  {
    //kill the current running Timer, if any
    if ( mTimer != null ) 
    {
      mTimer.cancel();
    }

    mTimer = new Timer();

    //reset the count
    mRunCount = 0;
    //set the limit
    mRunLimit = (iterations > 0) ? iterations : DEFAULT_RUNS;
    //set the interval
    mInterval = (interval > 5000L) ? interval : DEFAULT_INTERVAL;
    //start the leaker
    mTimer.scheduleAtFixedRate( this, new Date(), mInterval );
  }

  public void run()
  {
    String tempStr = null;
    long time = 0L;
    if( mRunCount++ >= mRunLimit )
    {
      System.out.println( "Reached the execution limit of " + mRunLimit + ", stopping." );
      mTimer.cancel();
    }
    else
    {
      System.out.print( "Loading 1000 items into two collections");
      for( int i = 0; i < 1000; i++ )
      {
        time = new Date().getTime();
        tempStr = "(" + i + ") At the tone, the time will be: " + time;
        mLeakArray.add( tempStr );
        mLeakMap.put( tempStr, new Long(time) );
        if( i%100 == 0 )
        {
          System.out.print( "." );
        }
      }
      System.out.print( "Done.\n" );
    }
  }

  public static void main( String [] args )
  {
    LeakerTask0 LT = null;
    int limit = LeakerTask0.DEFAULT_RUNS;
    long interval = LeakerTask0.DEFAULT_INTERVAL;

    for( int i = 0; i < args.length; i++ )
    {
      if( args[i].startsWith( "-i" ) )
      {
        try
        {
          interval = Long.parseLong( args[i].substring( args[i].indexOf( "i" ) + 1 ) );
        }
        catch( NumberFormatException nfe )
        {
          System.out.println( "Expected long value for interval, got: " + args[i] + ".  Using default." );
          interval = LeakerTask0.DEFAULT_INTERVAL;
        }
      }
      else if( args[i].startsWith( "-n" ) )
      {
        try
        {
          limit = Integer.parseInt( args[i].substring( args[i].indexOf( "n" ) + 1 ) );
        }
        catch( NumberFormatException nfe )
        {
          System.out.println( "Expected int value for run limit, got: " + args[i] + ".  Using default." );
          limit = LeakerTask0.DEFAULT_RUNS;
        }
      }
      else if( args[i].startsWith( "-h" ) )
      {
        System.out.println( "Java LeakerTast 1.0");
        System.out.println( "-------------------");
        System.out.println( "Usage: runLeaker.sh [-i <interval>] [-n <limit>] [-h]" );
        System.out.println( "\n-i <interval>: Wait <interval> milliseconds between Collection growths" );
        System.out.println( "-n <limit>: Stop after <limit> growth periods" );
        System.out.println( "-h: Print this help message, don't run the leaker" );
        return;
      }
      else
      {
        System.out.println( "Got bad argument: " + args[i] + ", ignorning." );
      }
    }
    
    try
    {
      LT = new LeakerTask0();
      LT.startLeak( limit, interval );
    }
    catch( Exception e )
    {
      System.out.println( "Caught an Exception: " + e.toString() );
      e.printStackTrace();
    }
  }

}
