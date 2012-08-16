import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Iterator;

/**
 * Simple, fake number database for testing
 * the sequence cache.
 */
public class SliceDataBase
{
  public static final String HOST = "myhost";
  public static final String JVM = "myjvm";
  private static SliceDataBase theDataBase = new SliceDataBase( "slices.txt");
  
  List database = new ArrayList();
  
  public static SliceDataBase getInstance()
  {
    return theDataBase; 
  }
  
  // takes a space delimited string of ints
  private SliceDataBase( String filename )
  {
    BufferedReader reader = null;
    String readStr = null;
    long startTime;
    long endTime;
    Slice tempSlice = null;
    
    try
    {
    reader = new BufferedReader( new FileReader( filename ) );

    while( (readStr = reader.readLine()) != null )
    { 
      if( readStr.startsWith("#")) continue;
      
      startTime = Long.parseLong(readStr.substring(0, readStr.indexOf(" ")));
      endTime = Long.parseLong(readStr.substring(readStr.lastIndexOf(" ") + 1));
      tempSlice = new Slice( HOST, JVM, startTime, endTime );
      database.add(tempSlice);
    }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      System.out.println( "Creating an empty database");
      database = new ArrayList();
    }
  }

  /**
   * select int from database where int >= first and int <= last
   */
  public Iterator query( long start, long end )
  {
    List retList = new ArrayList();
    Slice tempSlice = null;

    for( Iterator iter = database.iterator(); iter.hasNext(); )
    {
      tempSlice = (Slice)iter.next();

      if( tempSlice.getEndTime() >= start && tempSlice.getEndTime() <= end )
      {
        retList.add( tempSlice );
      }
      else if( tempSlice.getEndTime() > end )
      {
        break;
      }
    }

    return retList.iterator();
  }

  public String toString()
  {
    StringBuffer buff = new StringBuffer();
    boolean first = true;
    buff.append("[" );

    for( Iterator iter = this.database.iterator(); iter.hasNext(); )
    {
      if( !first )
      {
        buff.append(",");
      }
      else
      {
        first = false;
      }

      buff.append(((Integer)iter.next()).toString() );
    }

    buff.append("]");

    return buff.toString();
  }

}