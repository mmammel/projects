import java.util.*;

public class ListRemove
{
  public static void main( String [] args )
  {
    List myList = new ArrayList();

    myList.add( "foobar0" );
    myList.add( "foobar1" );
    myList.add( "foobar2" );
    myList.add( "foobar3" );
    
    System.out.println( "List size: " + myList.size() );

    String val = null;

    for( Iterator iter = myList.iterator(); iter.hasNext(); )
    {
      val = (String)iter.next();
      if( val.equals("foobar2") )
      {
        iter.remove();
      }
    }

    System.out.println( "List size: " + myList.size() );

  }
}

