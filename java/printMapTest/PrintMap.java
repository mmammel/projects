import java.util.Map;
import java.util.HashMap;

public class PrintMap {
  public static void main( String [] args ) {
    Map<String,Integer> foobar = new HashMap<String,Integer>();
    foobar.put("Max", 1);
    foobar.put("Don", 3);
    foobar.put("Mike", 5);

    System.out.println("Contents of map: " + foobar );
  }
}
