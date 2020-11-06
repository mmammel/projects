public class Many {

  public static void main( String [] args ) {
    int val = Integer.parseInt( args[0] );
    System.out.println( Many.getManyValue( val ) );
  }

  public static int getManyValue( int lines ) {
    int retVal = 5;
    int temp = lines / 64;
    while( (temp = temp >> 2) > 0 ) {
      retVal *= 2;
    }

    return retVal;
  }
}

