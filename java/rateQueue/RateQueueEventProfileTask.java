import java.util.TimerTask;

/**
 * User: mammelma
 * Date: Jul 23, 2010
 * Time: 4:18:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class RateQueueEventProfileTask implements Runnable  {

  private int idx;
  private String profile;
  private RateQueue queue;
  private long idleTime;
  private boolean blocked = false;

  public RateQueueEventProfileTask( String profile, RateQueue queue, long idleTime )
  {
      this.idx = 0;
      this.profile = profile;
      this.queue = queue;
      this.idleTime = idleTime;
  }

  public RateQueueEventProfileTask( String profile, RateQueue queue )
  {
      this(profile,queue,3000L);
  }

  public void run()
  {
      // To be continued, have to do a refresh merge.
      while( this.idx < this.profile.length() )
      {
          this.blocked = false;
          if( this.profile.charAt( this.idx ) == '*' && this.queue.test() )
          {
              this.idx++;
              this.goToSleep(this.idleTime);
          }
          else if( this.profile.charAt( this.idx ) == '-' )
          {
              this.idx++;
              this.goToSleep(this.idleTime);
          }
          else
          {
              this.blocked = true;
              this.goToSleep(this.idleTime);
          }
      }
  }

  private void goToSleep( long millis )
  {
      try
      {
          Thread.sleep( millis );
      }
      catch( InterruptedException ie )
      {
          System.out.println( "Sleeping thread interrupted!!!" );
      }
  }

  public String toString()
  {
      char c = 0;
      if( this.blocked ) c = 'B';
      else if( this.idx < this.profile.length() && this.profile.charAt(this.idx) == '*' ) c = 'O';
      else c = 'o';

      StringBuilder sb = new StringBuilder();
      for( int i = 0; i < this.profile.length(); i++ )
      {
          sb.append( i == this.idx ? c : this.profile.charAt(i) );
      }

      return sb.toString();
  }
}
