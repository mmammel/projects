
public class SequenceDescriptor implements Comparable
{
  public static final int UNDEFINED = -3;
  public static final int BEFORE = -2;
  public static final int FRONT_OVERLAP = -1;
  public static final int CONTAINED = 0;
  public static final int END_OVERLAP = 1;
  public static final int AFTER = 2;
  public static final int SUPERSET = 3;
  
  private SequenceCacheItem first = null;
  private SequenceCacheItem last   = null;
  
  private String sequenceKey = null;

  public SequenceDescriptor()
  {}
  
  public SequenceDescriptor( SequenceCacheItem start, SequenceCacheItem end )
  {
    this(start, end, null);
  }
  
  public SequenceDescriptor( SequenceCacheItem start, SequenceCacheItem end, String key )
  {
    this.first = start;
    this.last = end;
    this.sequenceKey = key;
  }
  
  public int compareTo( Object o )
  {
    int retVal = UNDEFINED;
    SequenceDescriptor other = (SequenceDescriptor)o;

    if( this.getFirst().compareTo( other.getFirst() ) < 0 )
    {
      if( this.getLast().compareTo( other.getFirst() ) < 0 )
      {
        retVal = BEFORE;
      }
      else if( this.getLast().compareTo( other.getLast() ) < 0 )
      {
        retVal = FRONT_OVERLAP;
      }
      else if( this.getLast().compareTo( other.getLast() ) >= 0 )
      {
        retVal = SUPERSET;
      }
    }
    else if( this.getFirst().compareTo( other.getFirst() ) == 0 )
    {
      if( this.getLast().compareTo( other.getLast() ) <= 0 )
      {
        retVal = CONTAINED;
      }
      else if( this.getLast().compareTo( other.getLast() ) > 0)
      {
        retVal = SUPERSET;
      }
    }
    else if( this.getFirst().compareTo( other.getFirst() ) > 0 && this.getFirst().compareTo( other.getLast() ) < 0 )
    {
      if( this.getLast().compareTo( other.getLast() ) <= 0 )
      {
        retVal = CONTAINED;
      }
      else if( this.getLast().compareTo( other.getLast() ) > 0 )
      {
        retVal = END_OVERLAP;
      }
    }
    else if( this.getFirst().compareTo( other.getLast() ) == 0 )
    {
      if( this.getLast().compareTo( other.getLast() ) == 0 )
      {
        retVal = CONTAINED;
      }
      else if( this.getLast().compareTo( other.getLast() ) > 0 )
      {
        retVal = END_OVERLAP;
      }
    }
    else
    {
      retVal = AFTER;
    }

    return retVal;
  }
  
  public static String getCompareResult( int res )
  {
    String retVal = null;

    switch( res )
    {
      case BEFORE:
        retVal = "Before";
        break;
      case FRONT_OVERLAP:
        retVal = "Front Overlap";
        break;
      case CONTAINED:
        retVal = "Contained";
        break;
      case END_OVERLAP:
        retVal = "End Overlap";
        break;
      case AFTER:
        retVal = "After";
        break;
      case SUPERSET:
        retVal = "Superset";
        break;
      case UNDEFINED:
        retVal = "Undefined";
        break;
      default:
        retVal = "Unknown";
        break;
    }

    return retVal;
  }
  
  public SequenceCacheItem getFirst()
  {
    return first;
  }

  public void setFirst(SequenceCacheItem first)
  {
    this.first = first;
  }

  public SequenceCacheItem getLast()
  {
    return last;
  }

  public void setLast(SequenceCacheItem last)
  {
    this.last = last;
  }

  public String getSequenceKey()
  {
    return sequenceKey;
  }

  public void setSequenceKey(String sequenceKey)
  {
    this.sequenceKey = sequenceKey;
  }
  
}
