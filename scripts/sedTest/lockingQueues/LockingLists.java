import java.util.List;
import java.util.ArrayList;

public class LockingLists
{
  private List<List<String>> mLists = null;
  private int mNumLists = 0;
  private int mRunningThreads = 0;
  private double mVariable = 10.0;

  public LockingLists( int num )
  {
    this.mNumLists = num;
    this.mLists = new ArrayList<List<String>>();

    List<String> tempList = null;

    for( int i = 0; i < this.mNumLists; i++ )
    {
      tempList = new ArrayList<String>();
      this.mLists.add( tempList );
    }
  }

  public void setVariable( double d )
  {
    this.mVariable = d;
  }

  public synchronized void incrementRunningThreads()
  {
    this.mRunningThreads++;
  }

  public synchronized void decrementRunningThreads()
  {
    this.mRunningThreads--;
  }

  public void dumpLists()
  {
    int count = 0;
    for( List<String> tempList : this.mLists )
    {
      System.out.print("List " + count++ + ": [ ");
      
      for( String str : tempList )
      {
        System.out.print( str + " " );
      }
     
      System.out.println( "]" );
    }
  }

  public void execute( int poolSize )
  {
    ListWriter tempWriter = null;

    for( int i = 0; i < poolSize; i++ )
    {
      tempWriter = new ListWriter( i % this.mNumLists );
      tempWriter.start(); 
    }

    while( this.mRunningThreads > 0 )
    {
      System.out.println( "Waiting for " + this.mRunningThreads + " more." );

      try
      {
        Thread.sleep(5000);
      }
      catch( Exception e )
      {
        System.out.println( "Caught an exception trying to sleep, exiting." );
        break;
      }
    }
  }


  public class ListWriter extends Thread
  {
    public int listnum = -1;
    public String id = null;

    public ListWriter( int num )
    {
      listnum = num;
      id = "_listLock_" + listnum;
    }

    public void write( String write_me )
    {
      List<String> tempList = mLists.get(listnum);

      synchronized( this.id.intern() )
      {
        tempList.add( write_me );
      }
    } 

    public void run()
    {
      long tempLong = 0L;
      ListReader reader = new ListReader( this.listnum );
      incrementRunningThreads();

      while( true )
      {
        try
        {
          Thread.sleep(5000);
        }
        catch( Exception e )
        {
          System.out.println( "Caught an exception trying to sleep, exiting." );
          break;
        }

        tempLong = Math.round( Math.random() * mVariable );
        if( reader.listContains( "" + tempLong ) )
        {
          System.out.println( "A Reader of List " + listnum + " is exiting." );
          break;
        }
        else
        {
          System.out.println( "Writing " + tempLong + " to list " + listnum );
          this.write( "" + tempLong );
        }
      }
      decrementRunningThreads();
    }
  }

  public class ListReader
  {
    private int listnum = 0;
    private String id = null;

    public ListReader( int num )
    {
      this.listnum = num;
      this.id = "_listLock_" + this.listnum;
    }

    public boolean listContains( String str )
    {
      boolean retVal = false;
      List<String> tempList = mLists.get( listnum );

      synchronized( this.id.intern() )
      {
        for( String readStr : tempList )
        {
          if( readStr.equals( str ) )
          {
            retVal = true;
          }
        }
      }

      return retVal;
    }
  }

  public static void main( String [] args )
  {
    int numLists = 5;
    int poolSize = 10;
    double var = 10.0;

    try
    {
   
      for( int i = 0; i < args.length; i++ )
      {
        if( i == 0 ) numLists = Integer.parseInt( args[i] );
        if( i == 1 ) poolSize = Integer.parseInt( args[i] );
        if( i == 2 ) var = Double.parseDouble( args[i] );
      }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception trying to set params: " + e.toString() );
    }

    LockingLists LQ = new LockingLists(numLists);

    LQ.setVariable( var );

    LQ.execute( poolSize );
  } 

}
