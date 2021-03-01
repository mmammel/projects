import java.util.*;

public class TrimListTest {
  public static final String [] ARRAY = {
    "foobar",
    "mammel",
    "weasels",
    "foo",
    "bar",
    "elephant",
    "zippity-do-dah"
  };

  public static void main( String [] args ) {
    List<String> result = new ArrayList<String>();

    for( String s : ARRAY ) {
      while( s.length() > 3 ) {
        result.add( s.substring(0,3) );
        s = s.substring(3);
      }
      result.add( s );
    }

    for( String s : result ) {
      System.out.println( s );
    }
  }
}


