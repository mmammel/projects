import java.util.List;
import java.util.ArrayList;

public class QuoteTokenizer {

  public static void main( String [] args ) {
    QuoteTokenizer QT = new QuoteTokenizer();

    List<String> tokens = QT.tokenizeSearchKeywords(args[0]);
    System.out.println( tokens );
  }

  private List<String> tokenizeSearchKeywords(String q) {
    // We want to split the string by space, BUT honor double quotes - so a double-quoted
    // string will be a single token.
    List<String> retVal = new ArrayList<String>();
    boolean inQuote = false;
    String currToken = "";
    // first just split, then we cycle through them and glue together a token if need be.
    String [] rawTokens = q.split("\\s");
    for( String t : rawTokens ) {
      if( t.startsWith("\"") && t.endsWith("\"") ) {
        if( inQuote ) {
          // weird nested quote... just flush and carry on.
          if( currToken.length() > 0 ) {
            retVal.add(currToken);
          }
          currToken = "";
          inQuote = false;
        }
        // it's an "atomic" quoted string, just add it, we are NOT in a quote.
        retVal.add(t);
        currToken = "";
      } else if( t.startsWith("\"") ) {
        if( inQuote ) {
          // someone is nesting quotes on us...
          // just flush the currtoken and start fresh.
          retVal.add(currToken);
          currToken = "";
        }

        inQuote = true;
        currToken = t;
      } else if( t.endsWith("\"") ) {
        if( inQuote ) {
          currToken += (" " + t);
          inQuote = false;
          retVal.add(currToken);
          currToken = "";
        } else {
          // someone screwed the pooch, just add this as a token, it will get literally searched
          retVal.add(t);
          currToken = "";
        }
      } else {
        if( inQuote ) {
          currToken += (" " + t);
        } else {
          retVal.add(t);
          currToken = "";          
        }
      }         
    }
    
    return retVal;
  }

}
