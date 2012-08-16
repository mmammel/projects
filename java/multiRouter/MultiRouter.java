import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collections;
import java.util.Stack;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MultiRouter
{
  public static final String PREFIX = "_thread_lockString__";
  public static final String COUNT_PREFIX = "__Count_string:";

  public static Map<String,Stack<String>> mailBox = Collections.synchronizedMap(new HashMap<String,Stack<String>>());

  private Map<String,Integer> itemCounts = new HashMap<String, Integer>();
  private Map<String,RouteHandler> handlers = new HashMap<String,RouteHandler>();

  /**
   * Convenience method to get the intern of the lock string.
   */
  public static String getLockString( String str )
  {
    return (PREFIX + str).intern();
  }

  /**
   * Process a String.  Use the first letter of the string to generate
   * the id of the handler. (max -> MMM).
   */
  public void readString( String str )
  {
    RouteHandler handler = null;
    int tempCount = 0;
    String firstLet = str.length() > 1 ? str.substring( 0, 1 ).toUpperCase() : str.toUpperCase();
    String tempId = firstLet + firstLet + firstLet;

    if( (handler = this.handlers.get( tempId ) ) == null )
    {
      /*
       * the handler does not exist.  Create it,
       * put it in the map and set the count to 0.
       */
      handler = new RouteHandler( tempId );
      this.handlers.put( tempId, handler );
      handler.start();
      this.itemCounts.put( tempId, new Integer(0) );
    }

    /*
     * The handler exists, put the word in its mailbox
     * and wake it up.
     */
    synchronized( getLockString( tempId ) )
    {
      Stack<String> tempStack = null;

      if( (tempStack = mailBox.get( tempId )) == null )
      {
        tempStack = new Stack<String>();
        mailBox.put( tempId, tempStack );
      }

      mailBox.get( tempId ).push( str );
      getLockString( tempId ).notify();
    }

    tempCount = this.itemCounts.get( tempId ).intValue() + 1;
    this.itemCounts.put( tempId, new Integer( tempCount ) );
  }

  /**
   * Run through all of the handlers and send them their counts.
   * This will stop all of the threads because the word adding is
   * synchronous and we don't know the count until after we've
   * sent all of the words.  It would still work if we sent the count first
   * though.
   */
  public void finish()
  {
    RouteHandler handler = null;
    String tempId = null;

    for( Iterator<String> iter = this.handlers.keySet().iterator(); iter.hasNext(); )
    {
      tempId = iter.next();
      handler = this.handlers.get( tempId );

      if( handler != null )
      {
        synchronized( getLockString( tempId ) )
        {
          mailBox.get( tempId ).push( COUNT_PREFIX + this.itemCounts.get( tempId ).intValue() );
          getLockString( tempId ).notify();
        }
      }

    }
  }

  /**
   * Read a list of words in a file, the name of which is passed as the
   * only parameter.  Dump the words into the router, which will then
   * find the proper handler thread for the word and send it along.
   */
  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    String input_str = "";

    MultiRouter MR = new MultiRouter();

    try
    {
      input_reader = new BufferedReader( new FileReader ( args[0] ) );

      while( (input_str = input_reader.readLine()) != null )
      {
        MR.readString( input_str.trim() );
      }

      MR.finish();
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }

  /**
   * The handler class simply sleeps, wakes up when told, and looks in
   * its mailbox for a word.  If it finds one (it always should) it adds
   * the word to its list.
   */
  public class RouteHandler extends Thread
  {
    private String id = null;
    private int itemCount = Integer.MAX_VALUE;

    private List<String> items = new ArrayList<String>();

    public RouteHandler( String id )
    {
      this.id = id;
    }

    /**
     * Until it has been determined that no more strings will be
     * showing up, just wait on my lock and grab a string out of my mailbox
     * when I wake up.
     */
    public void run()
    {
      String lockStr = getLockString(this.id);
      String tempItem = null;
      Stack<String> tempStack = null;

      synchronized( lockStr )
      {
        while( this.items.size() < this.itemCount )
        {
          try
          {
            lockStr.wait();
          }
          catch( InterruptedException ie )
          {
            continue;
          }

          tempStack = mailBox.get( this.id );

          while( !tempStack.empty() )
          {
            tempItem = tempStack.pop();

            if( tempItem.startsWith( COUNT_PREFIX ) )
            {
              this.itemCount = Integer.parseInt( tempItem.substring( tempItem.indexOf(":") + 1 ) );
            }
            else
            {
              this.items.add( tempItem );
            }
          }
        }
      }

      printItems(this.id, this.items);
    }

  }

  /**
   * Called by the handlers when they are finished running.
   * It is synchronized on the MultiRouter class object so
   * the output is uninterrupted for each handler.
   */
  private static synchronized void printItems(String id, List<String> items )
  {
    System.out.println( "\n++++++++++" + id + "++++++++++" );

    for( int i = 0; i < items.size(); i++ )
    {
      System.out.println( id + "[" + i + "] -> " + items.get(i) );
    }

    System.out.println( "----------" + id + "----------\n" );
  }

}
