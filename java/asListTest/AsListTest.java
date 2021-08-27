import java.util.List;
import java.util.Arrays;

public class AsListTest {
  public static void main( String [] args ) {
    List<String> theList = Arrays.asList(args);

    System.out.println( theList.getClass().getName() );

    for( String s : theList ) {
      System.out.println( s );
    }

    String foo = "asdf,asdf,asdf,asdf,asdf,asdf,asdf";
    String foobar = "asdfasdfasdfasdf";

    List<String> fooList = Arrays.asList( foo.split(",") );
    List<String> foobarList = Arrays.asList( foobar.split(",") );

    System.out.println( "Foo" );

    for( String f : fooList ) {
      System.out.println( f );
    }

    System.out.println( "Foobar" );

    for( String fb : foobarList ) {
      System.out.println( fb );
    }
  }
}
