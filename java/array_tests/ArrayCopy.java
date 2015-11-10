import java.util.Arrays;

public class ArrayCopy {
  public static final String [] TESTARRAY = { "a","b","c","d","e","f","g","h","i","j","k","l","m" };

  public static void main( String [] args ) {
    String [] tempArray = null;

    int from = 0, to = 0;

    while( to < TESTARRAY.length ) {
      to = (from + 3 > TESTARRAY.length) ? TESTARRAY.length : from + 3;
      tempArray = Arrays.copyOfRange( TESTARRAY, from, to );
      printArray( tempArray );
      from = from + 3;
    } 
  }

  public static void printArray( String [] array ) {
    StringBuilder sb = new StringBuilder();
    sb.append( "[" );
    boolean first = true;

    for( String s : array ) {
      if( !first ) {
        sb.append(",");
      } else {
        first = false;
      }

      sb.append( s );
    }

    sb.append( "]" );
    System.out.println( sb.toString() );
  }

}
