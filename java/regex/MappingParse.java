import java.util.regex.*;

public class MappingParse {

  public static final String MAPPING_PATTERN = "^(R?MAP)\\(([^,]+?),(.*?)\\)$";

  public static final String PREFIX = "^payload\\.(.*)$";
  public static final String POSTFIX = "^(.*?)\\.(r?map)$";

  public static void main( String [] args ) {
    //String result = args[0].replaceAll( MAPPING_PATTERN, "Operation: $1, MapKey: $2, Path: $3" );
    //System.out.println( result );

    String s = args[0];
    String mode = null;
    if( s.matches( PREFIX ) ) {
      s = s.replaceAll( PREFIX, "$1" );
    }

    if( s.matches( POSTFIX ) ) {
      mode = s.replaceAll( POSTFIX, "$2");
      s = s.replaceAll( POSTFIX, "$1" );
    }

    System.out.println( "Final name = " + s + ", mode = " + mode );

    
  }
}
