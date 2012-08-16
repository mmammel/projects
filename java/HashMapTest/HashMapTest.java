import java.util.*;

public class HashMapTest
{
  public static final int BEFORE = -2;
  public static final int FRONT_OVERLAP = -1;
  public static final int CONTAINED = 0;
  public static final int END_OVERLAP = 1;
  public static final int AFTER = 2;
  public static final int SUPERSET = 3;

  public TestRangeObject getTestRangeObject()
  {
    return new TestRangeObject();
  }

  public static void main( String [] args )
  {
    HashMapTest HMT = new HashMapTest();

    TestRangeObject source = HMT.getTestRangeObject();
    source.start = 5;
    source.end = 10;

    TestRangeObject tester = HMT.getTestRangeObject();

    tester.start = 1;
    tester.end = 1;
    System.out.println( HashMapTest.getCompareResult( tester.compareTo( source ) ) );

    tester.start = 1;
    tester.end = 3;
    System.out.println( HashMapTest.getCompareResult( tester.compareTo( source ) ) );

    tester.start = 1;
    tester.end = 5;
    System.out.println( HashMapTest.getCompareResult( tester.compareTo( source ) ) );

    tester.start = 1;
    tester.end = 7;
    System.out.println( HashMapTest.getCompareResult( tester.compareTo( source ) ) );

    tester.start = 1;
    tester.end = 10;
    System.out.println( HashMapTest.getCompareResult( tester.compareTo( source ) ) );

    tester.start = 1;
    tester.end = 12;
    System.out.println( HashMapTest.getCompareResult( tester.compareTo( source ) ) );

    tester.start = 5;
    tester.end = 5;
    System.out.println( HashMapTest.getCompareResult( tester.compareTo( source ) ) );

    tester.start = 5;
    tester.end = 7;
    System.out.println( HashMapTest.getCompareResult( tester.compareTo( source ) ) );

    tester.start = 5;
    tester.end = 10;
    System.out.println( HashMapTest.getCompareResult( tester.compareTo( source ) ) );

    tester.start = 5;
    tester.end = 12;
    System.out.println( HashMapTest.getCompareResult( tester.compareTo( source ) ) );

    tester.start = 7;
    tester.end = 7;
    System.out.println( HashMapTest.getCompareResult( tester.compareTo( source ) ) );

    tester.start = 7;
    tester.end = 8;
    System.out.println( HashMapTest.getCompareResult( tester.compareTo( source ) ) );

    tester.start = 7;
    tester.end = 10;
    System.out.println( HashMapTest.getCompareResult( tester.compareTo( source ) ) );

    tester.start = 7;
    tester.end = 12;
    System.out.println( HashMapTest.getCompareResult( tester.compareTo( source ) ) );

    tester.start = 10;
    tester.end = 10;
    System.out.println( HashMapTest.getCompareResult( tester.compareTo( source ) ) );

    tester.start = 10;
    tester.end = 12;
    System.out.println( HashMapTest.getCompareResult( tester.compareTo( source ) ) );

    tester.start = 12;
    tester.end = 12;
    System.out.println( HashMapTest.getCompareResult( tester.compareTo( source ) ) );

    tester.start = 12;
    tester.end = 15;
    System.out.println( HashMapTest.getCompareResult( tester.compareTo( source ) ) );

    source.start = 27;
    source.end = 134;

    Map testMap = new HashMap();

    testMap.put( source, "FOOBAR" );

    tester.start = 45;
    tester.end   = 100;

    System.out.println( (String)testMap.get( tester ) );
  }

  public class TestRangeObject implements Comparable
  {
    public int start;
    public int end;

    public int compareTo( Object o )
    {
      int retVal = -3;
      TestRangeObject other = (TestRangeObject)o;

      if( this.start < other.start )
      {
        if( this.end < other.start )
        {
          retVal = BEFORE;
        }
        else if( this.end < other.end )
        {
          retVal = FRONT_OVERLAP;
        }
        else if( this.end >= other.end )
        {
          retVal = SUPERSET;
        }
      }
      else if( this.start == other.start )
      {
        if( this.end <= other.end )
        {
          retVal = CONTAINED;
        }
        else if( this.end > other.end )
        {
          retVal = SUPERSET;
        }
      }
      else if( this.start > other.start && this.start < other.end )
      {
        if( this.end <= other.end )
        {
          retVal = CONTAINED;
        }
        else if( this.end > other.end )
        {
          retVal = END_OVERLAP;
        }
      }
      else if( this.start == other.end )
      {
        if( this.end == other.end )
        {
          retVal = CONTAINED;
        }
        else if( this.end > other.end )
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

    public boolean equals( Object o )
    {
      TestRangeObject other = (TestRangeObject)o;

      return (this.compareTo( other ) == 0 );
    }

    public int hashCode()
    {
      return 1;
    }
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
      default:
        retVal = "Unknown";
        break;
    }

    return retVal;
  }
}