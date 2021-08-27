import java.util.Map;
import java.util.LinkedHashMap;

public class SynchronizedTest {

  public static Map<String,String> MYMAP = new LinkedHashMap<String,String>();

  static {
    populateMap();
  }

  public static void main( String  [] args ) {
    System.out.println( MYMAP.get("foo"));
    populateMap();
    System.out.println( MYMAP.get("foo"));
  }

  public static void populateMap() {
    synchronized( MYMAP ) {
      MYMAP = new LinkedHashMap<String,String>();
      MYMAP.put("foo","bar");
      MYMAP.put("fo","ba");
      MYMAP.put("foofo","barba");
      MYMAP.put("fooffo","barbba");
    }
  }
}
