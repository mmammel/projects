import java.util.regex.*;

public class ABPuzzle {
  public static final String AB_PATTERN = "^.*?b.*?a.*$";

  public static void main( String [] args ) {
    System.out.println( !args[0].matches( AB_PATTERN ) );
  }
}
