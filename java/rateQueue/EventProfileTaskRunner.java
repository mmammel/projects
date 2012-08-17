
import java.util.*;

/**
 * User: mammelma
 * Date: Jul 26, 2010
 * Time: 2:40:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventProfileTaskRunner {
  private Map<String,String> profileLibrary = new HashMap<String,String>();
  private RateQueue queue = null;
  private long taskIdleTime;

  public EventProfileTaskRunner( String [][] profiles, RateQueue queue, long idleTime )
  {
      this.taskIdleTime = idleTime;
      this.queue = queue;
      for( String [] temp : profiles )
      {
          this.profileLibrary.put(temp[0],temp[1]);
      }
  }

  public void runProfiles( String... profiles )
  {
    String tempProfile = null;
    ThreadHelper helper = new ThreadHelper();
    RateQueueEventProfileTask [] tasks = new RateQueueEventProfileTask [profiles.length];
    for( int i = 0; i < profiles.length; i++ )
    {
        tempProfile = this.profileLibrary.containsKey(profiles[i]) ? this.profileLibrary.get(profiles[i]) : "-";
        tasks[i] = new RateQueueEventProfileTask(tempProfile,this.queue,this.taskIdleTime);
    }

    PrintTask taskPrinter = new PrintTask(tasks, this.queue);
    Timer printDaemon = new Timer();
    printDaemon.scheduleAtFixedRate( taskPrinter, new Date(), this.taskIdleTime );
    helper.runThreads(tasks);
  }

  private class PrintTask extends TimerTask
  {
    private RateQueue queue;
    private RateQueueEventProfileTask [] tasks;
    public PrintTask( RateQueueEventProfileTask [] tasks, RateQueue queue )
    {
        this.tasks = tasks;
        this.queue = queue;
    }

    public void run()
    {
      for( int i = 0; i < this.tasks.length; i++ )
      {
          System.out.println( "[Task " + i + "]:" + this.tasks[i] );
      }

      System.out.println( "Queue: " + this.queue );
    }
  }
}
