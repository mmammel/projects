import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class IntegerDAO
{
  IntegerDataBase mDatabase = null;
  SequenceCache mCache = new SequenceCache( "IntegerDAOCache" );

  public IntegerDAO( IntegerDataBase db )
  {
    mDatabase = db;
  }

  public IntegerDAO( String dbInit )
  {
    mDatabase = new IntegerDataBase( dbInit );
  }

  public List query( int first, int last )
  {
    boolean cacheHit = false;
    List retVal = null;
    SequenceCacheQueryResult cacheResult = null;

    IntegerCacheItem fitem = new IntegerCacheItem( first );
    IntegerCacheItem litem = new IntegerCacheItem( last );

    cacheResult = mCache.query( new SequenceDescriptor( fitem, litem ) );

    if( cacheResult.getDescriptor() == SequenceCacheQueryResult.COMPLETE_DATA )
    {
       System.out.println( "Cache hit!" );
       retVal = cacheResult.getCachedData();
       cacheHit = true;
    }
    else
    {
      System.out.println( "Cache miss: " + cacheResult.toString());
    }

    if( !cacheHit )
    {
      retVal = this.cacheItemizeIntegerList(mDatabase.query( first, last ));
      mCache.insert(retVal, fitem, litem);
      System.out.println( "Inserted actual query into cache." );
    }

    return retVal;
  }

  public String toString()
  {
    String retVal = "   DB: " + mDatabase.toString() + "\n" +
                    "Cache: " + mCache.toString();
    return retVal;
  }

  private List cacheItemizeIntegerList( List ints )
  {
    Integer tempInt = null;
    List retVal = new ArrayList();

    for( Iterator iter = ints.iterator(); iter.hasNext(); )
    {
      tempInt = (Integer)iter.next();

      retVal.add( new IntegerCacheItem( tempInt.intValue() ) );
    }

    return retVal;
  }

}