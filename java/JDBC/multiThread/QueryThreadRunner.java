/**
 * User: mammelma
 * Date: Jul 23, 2010
 * Time: 1:09:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class QueryThreadRunner
{
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
}

