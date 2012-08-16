import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public class LockingSets
{
  private List<Set<String>> mSets = null;
  private int mNumSets = 0;
  private int mRunningThreads = 0;
  private double mVariable = 10.0;

  public LockingSets( int num )
  {
    this.mNumSets = num;
    this.mSets = new ArrayList<Set<String>>();

    Set<String> tempSet = null;

    for( int i = 0; i < this.mNumSets; i++ )
    {
      tempSet = new HashSet<String>();
      this.mSets.add( tempSet );
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

  public void dumpSets()
  {
    int count = 0;
    for( Set<String> tempSet : this.mSets )
    {
      System.out.print("Set " + count++ + ": [ ");
      
      for( String str : tempSet )
      {
        System.out.print( str + " " );
      }
     
      System.out.println( "]" );
    }
  }

  public void execute( int poolSize )
  {
    SetWriter tempWriter = null;

    for( int i = 0; i < poolSize; i++ )
    {
      tempWriter = new SetWriter( i % this.mNumSets );
      tempWriter.start(); 
    }

    while( this.mRunningThreads > 0 )
    {
      System.out.println( "### Runner: Waiting for " + this.mRunningThreads + " more." );

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


  public class SetWriter extends Thread
  {
    public int setnum = -1;
    public String id = null;

    public SetWriter( int num )
    {
      setnum = num;
      id = "_setLock_" + setnum;
    }

    public void write( String write_me )
    {
      Set<String> tempSet = mSets.get(setnum);

      synchronized( this.id.intern() )
      {
        tempSet.add( write_me );
      }
    } 

    public void run()
    {
      long tempLong = 0L;
      SetReader reader = new SetReader( this.setnum );
      incrementRunningThreads();

      while( true )
      {
        try
        {
          Thread.sleep(1000);
        }
        catch( Exception e )
        {
          System.out.println( "Caught an exception trying to sleep, exiting." );
          break;
        }

        tempLong = Math.round( Math.random() * mVariable );
        if( reader.setContains( "" + tempLong ) )
        {
          System.out.println( "A Reader of Set " + setnum + " is exiting." );
          break;
        }
        else
        {
          System.out.println( "Writing " + tempLong + " to set " + setnum );
          this.write( "" + tempLong );
        }
      }
      decrementRunningThreads();
    }
  }

  public class SetReader
  {
    private int setnum = 0;
    private String id = null;

    public SetReader( int num )
    {
      this.setnum = num;
      this.id = "_setLock_" + this.setnum;
    }

    public boolean setContains( String str )
    {
      boolean retVal = false;
      Set<String> tempSet = mSets.get( setnum );

      synchronized( this.id.intern() )
      {
        retVal = tempSet.contains( str );
      }

      return retVal;
    }
  }

  public static void main( String [] args )
  {
    int numSets = 5;
    int poolSize = 10;
    double var = 10.0;

    try
    {
   
      for( int i = 0; i < args.length; i++ )
      {
        if( i == 0 ) numSets = Integer.parseInt( args[i] );
        if( i == 1 ) poolSize = Integer.parseInt( args[i] );
        if( i == 2 ) var = Double.parseDouble( args[i] );
      }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception trying to set params: " + e.toString() );
    }

    LockingSets LS = new LockingSets(numSets);
    LS.setVariable( var );
    LS.execute( poolSize );
    LS.dumpSets();
  } 

}
