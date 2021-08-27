import java.util.*;

public class ToStringTests {
  public static void main( String [] args ) {
    List<Integer> foobar = new ArrayList<Integer>();
    foobar.add(3);
    foobar.add(6);
    System.out.println( foobar.toString() );
  }
}
