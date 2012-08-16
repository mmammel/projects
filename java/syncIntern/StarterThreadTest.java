import java.util.*;
import java.io.*;

public class StarterThreadTest
{
  public static final int MAX_COUNT = 10;

  public static void main( String [] args )
  {
    Thread [] threads = new Thread[MAX_COUNT];
    CueObject mycue = new CueObject();
    Thread starter = new Thread( new StarterThread(mycue) );

    starter.start();

    for( int j = 0; j < MAX_COUNT; j++ )
    {
      threads[j] = new Thread( new MyThread( j, starter ) );
      threads[j].start();
    }

    BufferedReader input_reader = null;
    String input_str = "";

    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );
      while( (input_str = input_reader.readLine()) != null )
      {
  if( input_str.length() > 0)
  {
    mycue.start = true;
    break;
  }
      }
    }
    catch( IOException ioe )
    {
      System.out.println( "Caught an IOException!!" );
    }


    for( int k = 0; k < MAX_COUNT; k++ )
    {
      try
      {
        threads[k].join();
      }
      catch( InterruptedException ie )
      {
        System.out.println( "Caught an ie!!" );
      }
    }
  }

  public static class CueObject {
    public boolean start = false;
  }

  public static class StarterThread implements Runnable {
    private CueObject cue;

    public StarterThread( CueObject cue )
    {
      this.cue = cue;
    }

    public void run()
    {
      while( !this.cue.start )
      {
        System.out.println( "Waiting to start...");
        try
        {
          Thread.sleep( 1000 );
        }
        catch( InterruptedException ie )
        {
          System.out.println( "Caught an ie!!" );
        }
      }

      System.out.println( "Go!!" );
    }
  }

  public static class MyThread implements Runnable {

    private int id = -1;
    private Thread starterThread;

    public MyThread( int id, Thread starter )
    {
      this.id = id;
      this.starterThread = starter;
    }

    public void run()
    {
      try
      {
        this.starterThread.join();
      }
      catch( InterruptedException ie )
      {
        System.out.println( "Caught an ie!!" );
      }

      System.out.println( "Thread " + this.id + " GO!!!" );
    }
  }
}