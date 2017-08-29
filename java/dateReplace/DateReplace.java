import java.util.*;
import java.util.regex.*;

public class DateReplace {
  public static void main( String [] args ) {
    Pattern datePattern = Pattern.compile( "(.*?)/(.*?)/(.*)" );

    System.out.println( args[0].replaceAll( "(.*?)/(.*?)/(.*)", "$3$1$2" ) );
  }
}
