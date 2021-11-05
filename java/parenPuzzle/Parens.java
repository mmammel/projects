import java.util.*;
/**
  For n pairs of parentheses, generate all of the possible combinations of valid strings.

  Examples
  --------

   n = 1:
    ()

   n = 2:
    ()()
    (())

   n = 3:
    ()()()
    ()(())
    (())()
    ((()))
    (()())
  
*/

public class Parens {

  public static void main( String [] args ) {
    try {
      int n = Integer.parseInt( args[0] );
      Parens P = new Parens();
      Set<String> results = P.generateParenthesis( n );

      for( String s : results ) {
        System.out.println( s );
      }
    } catch( Exception e ) {
      System.out.println( "Error: " + e.toString() );
    }
  }

  public Set<String> generateParenthesis( int n ) {
    Set<String> retVal = new HashSet<String>();
    Set<String> temp = null;

    if( n == 1 ) {
      retVal.add( "()" );
    } else {
      temp = generateParenthesis( n - 1 );
      for( String p : temp ) {
        retVal.add( "(" + p + ")" );
        retVal.add( "()" + p );
        retVal.add( p + "()" );
      }
    }

    return retVal;
  } 
}
