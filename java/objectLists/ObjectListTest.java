import java.util.List;
import java.util.ArrayList;

public class ObjectListTest {

  public static void main( String [] args ) {
    List<Integer> nums = new ArrayList<Integer>();
    List<String> strs = new ArrayList<String>();

    nums.add(1);
    nums.add(4);
    strs.add("1");
    strs.add("4");

    processList( nums );
    processList( strs );
  }

  public static void processList( List<?> keys ) {
    for( Object o : keys ) {
      System.out.println( o.getClass().getName() );
    }
  }

}
