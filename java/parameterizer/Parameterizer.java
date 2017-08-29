import java.util.*;

public class Parameterizer {

  public static void main( String [] args ) {
    System.out.println( "Parameterizing: [" + args[0] + "]..." );

    List<String> result = Parameterizer.parameterize( args[0] );

    for( String s : result ) {
      System.out.println( s );
    }

    System.out.println( "...Done!" );
  }

  public static List<String> parameterize( String query ) {
    List<String> retVal = new ArrayList<String>();
    retVal.add( "Query placeholer" );
    StringBuilder newQuery = new StringBuilder();
    StringBuilder tempVal = null;

    boolean inString = false;
    int position = 0;

    char current = 0;
    char next = 0;
    char descriptor = 0; // S or D (string or date)

    while( position < query.length() ) {
      current = query.charAt( position );
      next = position < query.length() - 1 ? query.charAt( position + 1 ) : 0;

      if( inString ) {
        // We are at the first character after the opening '
        if( current == '\'' && next == '\'' ) {
          // We found a quoted single quote.
          tempVal.append( '\'' );
          position += 2;
        } else if( current == '\'' ) {
          // we are done with the string!
          position++;
          inString = false;
          retVal.add( replaceEntities( tempVal.toString() ) );
        } else {
          tempVal.append( current );
          position++;
        }
      } else {
        if( current == '$' && next == '$' ) {
          // We found the start of a candidate.
          position += 2; // point at the descriptor;
          descriptor = query.charAt( position++ ); // descriptor is S or D, pointing at single quote or N.
          current = query.charAt( position );
          if( current == 'N' ) { // some string literals are like : WHERE foo = N'hello' AND ...
            current = query.charAt(++position);
          }

          if( current != '\'' ) {
            throw new RuntimeException( "Bad placement of parameterizer marker" );
          } else {
            position++;
            newQuery.append( '?' );
            inString = true;
            tempVal = new StringBuilder();
          }
        } else {
          newQuery.append( current );
          position++;
        }
      }
    }

    if( inString ) {
      throw new RuntimeException( "Unclosed String literal in query!" );
    } else {
      retVal.set( 0, newQuery.toString() );
    }

    return retVal;
  }


  private static String replaceEntities( String s ) {
    return s != null ? s.replaceAll( "&#39;", "'" ) : null;
  }
}
