import java.util.regex.*;

public class ClassName {
  public static void main(String [] args ) {
    Pattern p = Pattern.compile(".*");
    System.out.println( p.getClass().getName() );
  }
}
