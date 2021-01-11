import java.util.List;
import java.util.ArrayList;

public class StringSplitter {

  public static void main( String [] args ) {
    int len = 0;
    String input = null;
    if( args.length != 2 ) {
      System.out.println( "Usage: java StringSplitter <string> <length>" );
    } else {
      try {
        input = args[0];
        len = Integer.parseInt( args[1] );
      } catch( NumberFormatException nfe ) {
        System.out.println( "Bogus input for length!  Must be a number!" );
        System.exit(1);
      }
    }

    String [] arr = splitString( input, len );
    for( String s : arr ) {
      System.out.println( s );
    }
  } 

  public static String [] splitString( String input, int len ) {
    List<String> retVal = new ArrayList<String>();
    while( input.length() > len ) {
      retVal.add( input.substring( 0, len ) );
      input = input.substring( len );
    }

    retVal.add( input );

    return retVal.toArray( new String [0] );
  }
}
