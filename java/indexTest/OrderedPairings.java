import java.util.List;
import java.util.ArrayList;

public class OrderedPairings {

  private static String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  public static void main( String [] args ) {
    String arg = null;
    int num = 4;

    if( args.length == 1 ) {
      try {
        num = Integer.parseInt( args[0] );
      } catch( Exception e ) {
      }
    } 

    if( num > 26 || (num % 2) != 0) {
      System.out.println( "Choose an even number less than 26.");
      System.exit(1);
    }

    String [] pairings = new String [ num ];
    for( int i = 0; i < num; i++ ) {
      pairings[i] = ""+ALPHABET.charAt(i);
    }

    OrderedPairings OP = new OrderedPairings();
    List<String> result = OP.getAllOrderedPairings( pairings );

    int count = 0;
    int roundSize = num / 2;

    for( String s : result ) {
      count++;
      System.out.print( s );
      System.out.print( " " );
      if( (count % roundSize) == 0 ) System.out.println("");
    }
  }


  public List<String> getAllOrderedPairings( String [] pairings ) {
    List<String> retVal = new ArrayList<String>();
    List<String> subPairings = null;
    int subCount = 0;

    if( pairings.length == 2 ) {
      retVal.add( pairings[0] + pairings[1] );
    } else {
        int i = 0;
        for( int j = i+1; j < pairings.length; j++ ) {
          subPairings = getAllOrderedPairings( getSubArrayExcludingIndices( pairings, i, j ) );
          for( String sub : subPairings ) {
            if( subCount++ % ((pairings.length - 2)/2) == 0 ) {
              retVal.add( pairings[i] + pairings[j] );
            }
            retVal.add( sub );
          }

          subCount = 0;
        }
    }

    return retVal;
  }

  public String [] getSubArrayExcludingIndices( String [] pairings, int p1, int p2 ) {
    String [] retVal = new String [ pairings.length - 2 ];
    int idx = 0;
    for( int i = 0; i < pairings.length; i++ ) {
      if( i != p1 && i != p2 ) {
        retVal[ idx++ ] = pairings[i];
      }
    }

    return retVal;
  }
}
