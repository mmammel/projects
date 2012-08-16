public class SliceKey
{
  private static int bucketSize = 0x200;
  private long bucketID = 0L;
  private long startTime = 0L;
  private long endTime = 0L;
  private String host = null;
  private String jvm = null;

  public SliceKey( Slice slice )
  {
    this( slice.startTime, slice.endTime, slice.host, slice.jvm );
  }

  public SliceKey( long st, long et, String h, String j )
  {
    this.bucketID = et - (et & (bucketSize - 1));
    this.startTime = st;
    this.endTime = et;
    this.host = h;
    this.jvm = j;
  }

  public int hashCode()
  {
    StringBuffer hashBuff = new StringBuffer();
    hashBuff.append( this.host + this.jvm );
    hashBuff.append( this.bucketID );
    return hashBuff.toString().hashCode();
  }

  public boolean equals( Object o )
  {
    boolean retVal = false;
    SliceKey testKey = null;

    if( o instanceof SliceKey )
    {
      testKey = (SliceKey)o;
      if( testKey.host.equals( this.host ) && testKey.jvm.equals(this.jvm) )
      {
        if( this.endTime > testKey.startTime &&
            this.endTime <= testKey.endTime )
        {
          retVal = true;
        }
      }
    }

    return retVal;
  }

  public String toString()
  {
    StringBuffer buff = new StringBuffer();

    buff.append( "[" + bucketID + "," + startTime + "," + endTime + "," + host + "," + jvm + "]" );

    return buff.toString();
  }
}
