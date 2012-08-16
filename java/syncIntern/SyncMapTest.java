import java.util.*;

public class SyncMapTest
{
  public static final int MAX_COUNT = 1000;

  public static void main( String [] args )
  {
    Thread [] threads = new Thread[MAX_COUNT];
    Map<String,String> map = Collections.synchronizedMap(new HashMap<String,String>());

    for( int i = 0; i < 10; i++ )
    {
      if( i % 7 != 0 ) map.put( ""+i,"val"+i );
    }

    for( int j = 0; j < MAX_COUNT; j++ )
    {
      threads[j] = new Thread( new MyThread( j, map ) );
      threads[j].start();
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

  public static class MyThread implements Runnable {

    private Map<String,String> mymap;
    private int id = -1;

    public MyThread( int id,Map<String,String> map )
    {
      this.id = id;
      this.mymap = map;
    }

    public void run()
    {
      String key;
      String tempStr;
      for( int i = 0; i < MAX_COUNT; i++ )
      {
        key = ""+i;
        synchronized( key.intern() ) {
          if( !this.mymap.containsKey(""+i) && i < this.id ) {
            System.out.println( "Thread " + this.id + " Adding " +i );
            this.mymap.put(""+i,"val"+i);
          }
          else if( this.id <= i )
          {
            System.out.println( "Thread " + this.id + " removing " + i );
            this.mymap.remove(""+i);
          }
        }
      }
    }
  }
}