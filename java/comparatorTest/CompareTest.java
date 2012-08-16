import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.util.Comparator;

public class CompareTest
{
  public static Comparator theComparator = new TestObjComparator();

  public static class TestObjComparator implements Comparator
  {
    public int compare( Object a, Object b )
    {
      TestObj toa = (TestObj)a;
      TestObj tob = (TestObj)b;
      
      return toa.mName.compareTo( tob.mName );
    }

  }

  public static void main( String [] args )
  {
    List testList = new ArrayList();
    for( int i = 0; i < 35; i++ )
    {
      testList.add( new TestObj( "myName" + i ) );
    }

    Collections.shuffle( testList );

    for( Iterator iter1 = testList.iterator(); iter1.hasNext(); )
    {
      System.out.println( "Element: " + ((TestObj)iter1.next()).mName );
    }

    Collections.sort( testList, CompareTest.theComparator );

    for( Iterator iter2 = testList.iterator(); iter2.hasNext(); )
    {
      System.out.println( "Element: " + ((TestObj)iter2.next()).mName );
    }
  }

}
