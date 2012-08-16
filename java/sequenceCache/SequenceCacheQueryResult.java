import java.util.List;

public class SequenceCacheQueryResult
{
  public static final int UNDEFINED       = -1;
  public static final int NO_DATA         = 0;
  public static final int PRE_QUERY_DATA  = 1;
  public static final int POST_QUERY_DATA = 2;
  public static final int COMPLETE_DATA   = 3;

  protected SequenceCacheItem mFront = null;
  protected SequenceCacheItem mBack  = null;
  protected int mDescriptor = UNDEFINED;
  List mCachedData = null;

  public SequenceCacheItem getBack()
  {
    return mBack;
  }

  public void setBack(SequenceCacheItem back)
  {
    mBack = back;
  }

  public List getCachedData()
  {
    return mCachedData;
  }

  public void setCachedData(List cachedData)
  {
    mCachedData = cachedData;
  }

  public int getDescriptor()
  {
    return mDescriptor;
  }

  public void setDescriptor(int descriptor)
  {
    mDescriptor = descriptor;
  }

  public SequenceCacheItem getFront()
  {
    return mFront;
  }

  public void setFront(SequenceCacheItem front)
  {
    mFront = front;
  }

  public String toString()
  {
    StringBuffer buff = new StringBuffer();

    buff.append( "Data is: ");
    switch( this.mDescriptor )
    {
      case UNDEFINED:
        buff.append("undefined");
        break;
      case NO_DATA:
        buff.append("empty");
        break;
      case PRE_QUERY_DATA:
        buff.append("pre query");
        break;
      case POST_QUERY_DATA:
        buff.append("post query");
        break;
      case COMPLETE_DATA:
        buff.append("complete");
        break;
      default:
        buff.append("!!ERROR!!  No valid descriptor found");
        break;
    }

    if( this.mCachedData != null )
    {
      buff.append("\n\nData:\n");
      boolean first = true;
      for( int i = 0; i < this.mCachedData.size(); i++ )
      {
        if( !first )
        {
          buff.append(", ");
        }
        else
        {
          first = false;
        }

        buff.append(this.mCachedData.get(i).toString() );
      }
    }

    buff.append("\n\nFront of remainder query: " + this.mFront );
    buff.append("\nBack of remainder query: " + this.mBack);

    return buff.toString();
  }

}