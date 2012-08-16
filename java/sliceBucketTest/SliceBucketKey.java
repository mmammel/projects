import java.io.Serializable;


public class SliceBucketKey implements Serializable
{
    long bucketNumber;
    String name;

    public SliceBucketKey( Slice slice )
    {
      this(slice.getEndTime(), slice.getHostName() + slice.getJvmID() + slice.getArchiveFolder());
    }

    public SliceBucketKey( long endTime, String host, String jvm, String archiveFolder )
    {
      this( endTime, host + jvm + archiveFolder );
    }

    public SliceBucketKey( long endTime, String name)
    {
      this.name = name;
      this.bucketNumber = SliceBucket.getBucketNumber(endTime);
    }

    public boolean equals(Object aThat)
    {
      boolean retVal = false;

      // check for self-comparison
      if (this == aThat)
      {
        retVal = true;
      }
      else
      {
        // use instanceof instead of getClass here for two reasons
        // 1. if need be, it can match any supertype, and not just one
        // class;
        // 2. it renders an explict check for "that == null" redundant,
        // since
        // it does the check for null already - "null instanceof [type]"
        // always
        // returns false. (See Effective Java by Joshua Bloch.)
        if (!(aThat instanceof SliceBucketKey))
        {
          retVal = false;
        }
        else
        {
          // Alternative to the above line :
          // if ( aThat == null || aThat.getClass() != this.getClass() )
          // return false;

          // cast to native object is now safe
          SliceBucketKey that = (SliceBucketKey) aThat;
          retVal = (this.bucketNumber == that.bucketNumber && this.name.equals(that.name));
        }
      }

      return retVal;
    }

    public int hashCode()
    {
      StringBuffer hashBuf = new StringBuffer();
      hashBuf.append(name);
      hashBuf.append(bucketNumber);

      return hashBuf.toString().hashCode();
    }

    public String toString()
    {
      StringBuffer buf = new StringBuffer();
      buf.append(this.bucketNumber);
      buf.append(", ");
      buf.append(this.name);

      return buf.toString();
    }

    public SliceBucketKey getNextKey( )
    {
      return new SliceBucketKey(this.bucketNumber + SliceBucket.bucketSize, this.name);
    }
}