
import java.util.Calendar;

public class Driver {

  public static void main( String [] args )
  {
    Driver D = new Driver();
    ReadWriteCache cache = new ReadWriteCache();

    Runnable [] threads = new Runnable [10];

    threads[0] = new CacheWriter( cache );
    threads[1] = new CacheWriter( cache );

    for( int i = 2; i < 10; i++ )
    {
      threads[i] = new CacheReader(cache);
    }

    D.runThreads( threads );
  }

  public void runThreads( Runnable [] tasks )
  {
    if( tasks != null && tasks.length > 0)
    {
      Thread [] thrs = new Thread [ tasks.length ];
      for( int i = 0; i < thrs.length; i++ )
      {
        thrs[i] = new Thread(tasks[i]);
        thrs[i].start();
      }

      for(;;)
      {
        try
        {
          for( Thread thr : thrs )
          {
            thr.join();
          }

          break;
        }
        catch( InterruptedException e )
        {
            System.out.println( "Thread interrupted!  Joining again..." );
        }
      }

      System.out.println( "Done running threads.");
    }
    else
    {
      System.out.println( "No threads provided, exiting without doing anything.");
    }
  }

  public static class CacheReader implements Runnable {
    ReadWriteCache cache = null;

    public CacheReader( ReadWriteCache cache )
    {
      this.cache = cache;
    }

    public void run()
    {
      String key = "";
      int rand = 0;

      for( int i = 0; i < 100; i++ )
      {
        rand = ((int)(Math.random() * i))%5;
        key = "key_"+rand;
        System.out.println( "cache.get(" + key +") = " + this.cache.get( key ) );
        try
        {
          Thread.sleep(rand*400L);
        }
        catch( InterruptedException ie )
        {
          System.out.println( "Thread interrupted!" );
        }
      }
    }
  }

  public static class CacheWriter implements Runnable {
    ReadWriteCache cache = null;

    public CacheWriter( ReadWriteCache cache )
    {
      this.cache = cache;
    }

    public void run()
    {
      String key = "";
      String value = "";
      int rand = 0;

      for( int i = 0; i < 100; i++ )
      {
        rand = ((int)(Math.random() * i))%5;
        key = "key_"+rand;
        value = "value_" + Calendar.getInstance().getTime().getTime();
        this.cache.put( key, value );
        try
        {
          Thread.sleep(rand*400L);
        }
        catch( InterruptedException ie )
        {
          System.out.println( "Thread interrupted!" );
        }
      }
    }
  }


}
