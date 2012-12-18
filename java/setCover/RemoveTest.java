import java.util.*;

public class RemoveTest {

  public static void main( String [] args ) {
   Set<String> one = new TreeSet<String>();

   Set<String> two = new TreeSet<String>();

   one.add( "foo" );
   one.add( "bar" );
   two.add( "bar" );

   one.removeAll( two );

   for( String o : one )
   {
     System.out.println( o );
   }
   }
}