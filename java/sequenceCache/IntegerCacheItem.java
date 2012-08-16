public class IntegerCacheItem implements SequenceCacheItem
{
  private Integer mInt = null;

  public IntegerCacheItem(int n)
  {
    this.mInt = new Integer(n);
  }

  public int compareTo( Object o )
  {
    int retVal = 0;
    IntegerCacheItem item = (IntegerCacheItem)o;
    retVal = this.mInt.compareTo( item.getInt() );

    return retVal;
  }

  public boolean equals( Object o )
  {
    boolean retVal = false;
    IntegerCacheItem other = null;

    try
    {
      other = (IntegerCacheItem)o;
      retVal = this.mInt.equals( other.getInt() );
    }
    catch( Exception e )
    {
      retVal = false;
    }

    return retVal;
  }

  public Integer getInt()
  {
    return this.mInt;
  }

  public SequenceCacheItem successor()
  {
    return new IntegerCacheItem( this.mInt.intValue() + 1 );
  }
  
  public String toString()
  {
    return this.mInt.toString();
  }

}