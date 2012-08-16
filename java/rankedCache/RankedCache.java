import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

public class RankedCache
{
  private static long wrapperTag = 0;
  private SortedMap cache = Collections.synchronizedSortedMap( new TreeMap() );

  /**
   *
   */
  public void insert( String key, String value )
  {
    KeyWrapper wrapper = new KeyWrapper( key );
    this.cache.put( wrapper, value );
  }

  /**
   *
   */
  public boolean containsKey( String key )
  {
    return this.cache.containsKey( new KeyWrapper( key ) );
  }

  /**
   *
   */
  public String get( String key )
  {
    String retVal = null;
    KeyWrapper newkey = null;

    if( this.containsKey( key ) )
    {
      newkey = new KeyWrapper( key );
      retVal = (String)this.cache.get( newkey );
      this.cache.put( newkey, retVal );
    }
    else
    {
      retVal = null;
    }

    return retVal;
  }

  /**
   *
   */
  public String toString()
  {
    StringBuffer buff = new StringBuffer();
    String tempString = null;
    KeyWrapper tempKey = null;

    buff.append( "[" );

    boolean first = true;

    for( Iterator iter = this.cache.keySet().iterator(); iter.hasNext(); )
    {
      if( !first )
      {
        buff.append(", ");
      }
      else
      {
        first = false;
      }

      tempKey = (KeyWrapper)iter.next();
      tempString = (String)this.cache.get( tempKey );
      buff.append( tempString );

    }

    buff.append( "]" );

    return buff.toString();
  }

  /**
   *
   */
  public class KeyWrapper implements Comparable
  {
    String value = null;
    String timestamp = null;

    /**
     *
     */
    public KeyWrapper( String str )
    {
      value = str;
      timestamp = "" + new Long(new Date().getTime()) + "_" + RankedCache.wrapperTag++;
      System.out.println( "Timestamp: " + timestamp );
    }

    /**
     *
     */
    public int compareTo( Object o )
    {
      KeyWrapper key = (KeyWrapper)o;
      if( this.value.equals( key.value ) ) return 0;
      return this.timestamp.compareTo( key.timestamp );
    }

    /**
     *
     */
    public int hashCode()
    {
      return this.value.hashCode();
    }

    /**
     *
     */
    public boolean equals( Object o )
    {
      KeyWrapper key = (KeyWrapper)o;
      return this.value.equals(key.value);
    }
  }

  /**
   *
   */
  public static void main( String [] args )
  {
    RankedCache RC = new RankedCache();

    RC.insert( "A", "max" );
    System.out.println( RC );

    RC.insert( "B", "mammel" );
    System.out.println( RC );

    RC.insert( "D", "jomama" );
    System.out.println( RC );

    if( RC.containsKey( "A" ) )
    {
      System.out.println( "Contains \"A\"" );
    }

    System.out.println( RC );
    System.out.println( "Get \"A\": " + RC.get( "A" ) );
    System.out.println( RC );
    System.out.println( "Get \"D\": " + RC.get( "D" ) );
    System.out.println( RC );
    System.out.println( "Get \"A\": " + RC.get( "A" ) );
    System.out.println( RC );
  }

}
