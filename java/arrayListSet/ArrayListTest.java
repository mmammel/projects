import java.util.List;
import java.util.ArrayList;

public class ArrayListTest {

  public static void main( String [] args )
  {
    List<String> foo = new ArrayList<String>(5);
    for( int i = 0; i < 5; i++ ) foo.add("");
    foo.set(0,"0");
    foo.set(3,"3");
  }
}
