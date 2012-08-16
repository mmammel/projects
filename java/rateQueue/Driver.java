public class Driver
{
  public static void main( String [] args )
  {
    if( args.length == 0 )
    {
      System.out.println( "Usage: java Driver <#events> <(per)minutes> [ <#threads> [ <threadevents> ] ]" );
      return;
    }

    long events = Long.parseLong( args[0] );
    long minutes = Long.parseLong( args[1] );
    long taskVal = -1L;
    int threads = 3;
    if( args.length > 2 )
    {
      threads = Integer.parseInt( args[2] );
    }

    if( args.length > 3 )
    {
      taskVal = Long.parseLong( args[3] );
    }

    RateQueue queue = new RateQueue(events,minutes);

    RateQueueTask tempTask = null;
    Thread [] thrs = new Thread [ threads ];
    for( int i = 0; i < threads; i++ )
    {
      if( taskVal > 0 )
      {
        tempTask = new RateQueueTask( queue, taskVal );
      }
      else
      {
        tempTask = new RateQueueTask( queue );
      }

      thrs[i] = new Thread( tempTask );
      thrs[i].start();
    }

    for( int j = 0; j < threads; j++ )
    {
      try
      {
        thrs[j].join();
      }
      catch( InterruptedException ie )
      {
        System.out.println( "InterruptedThread!! " + ie.toString() );
      }
    }

    queue.shutdown();

  }

}
