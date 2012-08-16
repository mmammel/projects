import java.util.*;

public class SubListTest
{
  public static void main( String [] args )
  {
    List mylist = new ArrayList();

    for( int i = 0; i < 10; i++ )
    {
      mylist.add( new Integer( i ) );
    }

    StringBuffer buff = new StringBuffer("[ ");

    for( Iterator iter = mylist.iterator(); iter.hasNext(); )
    {
      buff.append( (Integer)iter.next() );
    }

    buff.append( " ]" );
    
    System.out.println( buff );

    mylist.subList( 0, 4 ).clear();

    buff = new StringBuffer("[ ");

    for( Iterator iter2 = mylist.iterator(); iter2.hasNext(); )
    {
      buff.append( (Integer)iter2.next() );
    }

    buff.append( " ]" );

    System.out.println( buff );
  }
}
