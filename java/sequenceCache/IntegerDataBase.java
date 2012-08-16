import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Iterator;

/**
 * Simple, fake number database for testing
 * the sequence cache.
 */
public class IntegerDataBase
{
  List database = new ArrayList();
  // takes a space delimited string of ints
  public IntegerDataBase( String initStr )
  {
    StringTokenizer st = new StringTokenizer( initStr );

    try
    {
      while( st.hasMoreTokens() )
      {
        database.add( new Integer( st.nextToken() ) );
      }
    }
    catch( Exception e )
    {
      System.out.println( "Found a bogus character in init string: " + initStr );
      System.out.println( "Creating empty database" );
      database = new ArrayList();
    }
  }

  /**
   * select int from database where int >= first and int <= last
   */
  public List query( int first, int last )
  {
    List retList = new ArrayList();
    int tempInt = 0;
    Integer tempInteger = null;

    for( Iterator iter = database.iterator(); iter.hasNext(); )
    {
      tempInteger = (Integer)iter.next();
      tempInt = tempInteger.intValue();

      if( tempInt >= first && tempInt <= last )
      {
        retList.add( new Integer( tempInt ) );
      }
    }

    return retList;
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