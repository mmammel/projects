import java.util.List;
import java.util.ArrayList;

public class ArrayTests
{
  public static void main( String [] args )
  {
    List<String> testlist = new ArrayList<String>();

    testlist.add( "one" );
    testlist.add( "two" );
    testlist.add( "three" );

    System.out.println( testlist.size() );
    System.out.println( testlist.remove(1) );
    System.out.println( testlist.size() );
    System.out.println( testlist.remove(1) );
  }

}
