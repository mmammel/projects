import java.util.*;

public class MaryLoop
{
  public static void main( String [] args )
  {
    List<String> l = new ArrayList<String>();

    for( int i = 0; i < 100; i++ )
    {
      l.add("Item " + i );
    }

    Iterator<String> iter = l.iterator();

    while( iter.hasNext() )
    {
      System.out.println( iter.next() );

      if( iter.hasNext() ) System.out.println( iter.next() );

    } while( false );
  }


}
