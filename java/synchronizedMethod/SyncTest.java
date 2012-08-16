import java.io.*;

public class SyncTest
{

  public synchronized void method1()
  {
    System.out.println( Thread.currentThread().getName() + " in method1." );
    BufferedReader input_reader = new BufferedReader( new InputStreamReader ( System.in ) );
    String input_str = null;

    try
    {
      while( (input_str = input_reader.readLine()) != null )
      {
        if( input_str.equals( "continue" ) ) break;
      }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
    }
  }

  public synchronized void method2()
  {
    System.out.println( Thread.currentThread().getName() + " in method2." );
    BufferedReader input_reader = new BufferedReader( new InputStreamReader ( System.in ) );
    String input_str = null;

    try
    {
      while( (input_str = input_reader.readLine()) != null )
      {
        if( input_str.equals( "continue" ) ) break;
      }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
    }
  }

  private static class OneCaller implements Runnable
  {
    private SyncTest ST = null;

    public OneCaller( SyncTest st )
    {
      ST = st;
    }

    public void run()
    {
      ST.method1();
      ST.method2();
    }
  }

  private static class TwoCaller implements Runnable
  {
    private SyncTest ST = null;

    public TwoCaller( SyncTest st )
    {
      ST = st;
    }

    public void run()
    {
      ST.method2();
      ST.method1();
    }
  }

  public static void main( String [] args )
  {
    SyncTest st = new SyncTest();

    Thread t1 = new Thread( new OneCaller( st ) );
    Thread t2 = new Thread( new TwoCaller( st ) );

    t1.start();
    t2.start();
  }

}
