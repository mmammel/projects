import java.util.*;
import org.json.*;

public class JSONAddress {

  public static final String JSON_STRING = "{\"foobar\" : \"jomama\", \"people\" : [{\"first\" : \"Max\", \"last\" : \"Mammel\"}, {\"first\" : \"Jevne\", \"last\" : \"Bohnke\"} ], \"animals\" : {\"mammals\" : [{\"type\" : \"dog\", \"name\" : \"malla\"}, {\"type\" : \"cat\", \"name\" : \"binky\"} ], \"reptiles\" : [{\"type\" : \"anole\", \"name\" : \"greeny\"}] } }";

  private final String [] tokens;

  public JSONAddress( String path ) {
    if( path != null ) {
      if( path.indexOf("/") == 0 ) path = path.substring(1);
      this.tokens = path.split( "/" );
    } else {
      this.tokens = new String [0];
    }
  }

  public List<String> evaluate( Object json ) {
    List<String> retVal = new ArrayList<String>();
    this.evaluateInner( json, retVal, 0 );
    return retVal;
  }

  private void evaluateInner( Object json, List<String> results, int tokenIdx ) {
    Object curr = json;
    List<Object> currs = null;
    if( tokenIdx < this.tokens.length ) {
      if( curr instanceof JSONObject ) {
        curr = ((JSONObject)curr).opt( this.tokens[tokenIdx] );
        evaluateInner( curr, results, tokenIdx + 1 );
      } else if( curr instanceof JSONArray ) {
        currs = this.readFromArray( this.tokens[tokenIdx], (JSONArray)curr );
        for( Object o : currs ) {
          evaluateInner( o, results, tokenIdx + 1 );
        }
      }
    } else {
      if( curr instanceof java.lang.String ) {
        results.add( curr.toString() );
      } else if( curr instanceof JSONArray ) {
        Object temp = null;
        JSONArray arr = (JSONArray)curr;
        for( int i = 0; i < arr.length(); i++ ) {
          temp = arr.get(i);
          if( temp instanceof java.lang.String ) {
            results.add( temp.toString() );
          }
        }
      }
    }
  }

  private List<Object> readFromArray( String token, JSONArray arr ) {
    List<Object> retVal = new ArrayList<Object>();
    if( token.equals("*") ) {
      for( int i = 0; i < arr.length(); i++ ) {
        retVal.add( arr.get(i) );
      }
    } else {
      try {
        int idx = Integer.parseInt(token);
        if( idx >= arr.length() || idx < 0 ) {
          throw new RuntimeException( "Index out of bounds while processing JSONAddress: " + idx );
        } else {
          retVal.add( arr.get( idx ) );
        }
      } catch( NumberFormatException nfe ) {
        throw new RuntimeException( "Invalid index passed in JSONAddress: " + token );
      }
    }

    return retVal;
  }

  public static void main( String [] args ) {
    JSONAddress JA = new JSONAddress( args[0] );
    JSONObject json = new JSONObject( JSON_STRING );
    List<String> res = JA.evaluate( json );
    for( String s : res ) {
      System.out.println( s );
    }
  }
}
