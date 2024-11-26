import java.util.*;
import org.json.*;

public class JSONAddress {

//  public static final String JSON_STRING = "{\"foobar\" : \"jomama\", \"people\" : [{\"first\" : \"Max\", \"last\" : \"Mammel\"}, {\"first\" : \"Jevne\", \"last\" : \"Bohnke\"} ], \"animals\" : {\"mammals\" : [{\"type\" : \"dog\", \"name\" : \"malla\"}, {\"type\" : \"cat\", \"name\" : \"binky\"} ], \"reptiles\" : [{\"type\" : \"anole\", \"name\" : \"greeny\"}] } }";
//  public static final String JSON_STRING = "     {\r\n  \"foobar\" : \"jomama\",\r\n  \"people\" : [\r\n    {\r\n      \"first\" : \"Max\",\r\n      \"last\" : \"Mammel\"\r\n    },\r\n    {\r\n      \"first\" : \"Jevne\",\r\n      \"last\" : \"Bohnke\"\r\n    }\r\n  ],\r\n  \"objects\" : [\r\n    { \"cars\" : [\r\n        \"porsche\", \"mazda\", \"honda\", \"toyota\"\r\n      ]\r\n    },\r\n   { \"cars\" : [\r\n      \"cessna\", \"f-15\", \"SR-71 Blackbird\", \"C-5 Galaxy\"\r\n     ]\r\n   }\r\n  ],\r\n  \"animals\" : {\r\n    \"mammals\" : [\r\n       {\r\n         \"type\" : \"dog\",\r\n         \"name\" : \"malla\"\r\n       },\r\n       {\r\n         \"type\" : \"cat\",\r\n         \"name\" : \"binky\"\r\n       }\r\n    ],\r\n    \"reptiles\" : [\r\n      {\r\n        \"type\" : \"anole\",\r\n        \"name\" : \"greeny\"\r\n      }\r\n    ]\r\n  }\r\n}\r\n";

public static final String JSON_STRING = "{"+
"  \"auth\" : {"+
"    \"accountID\" : \"MARC3C\","+
"    \"username\" : \"administrator\","+
"    \"password\" : \"jomama\""+
"  },"+
"  \"testKeys\" : ["+
"    \"EN_W13ST\","+
"    \"EN_BAR_F\""+
"  ],"+
"  \"candidates\" : ["+
"    {"+
"      \"firstName\" : \"Bob\","+
"      \"lastName\" : \"Smith\","+
"      \"email\" : \"bsmith@email.com\","+
"      \"id\" : \"12345\""+
"    },"+
"    {"+
"      \"firstName\" : \"Jim\","+
"      \"lastName\" : \"Richalds\","+
"      \"email\" : \"jr@email.com\","+
"      \"id\" : \"2345\""+
"    },"+
"    {"+
"      \"firstName\" : \"Max\","+
"      \"lastName\" : \"Mammel\","+
"      \"email\" : \"max@symphonytalent.com.com\","+
"      \"id\" : \"9393939\""+
"    },"+
"    {"+
"      \"firstName\" : \"Steve\","+
"      \"lastName\" : \"Martin\","+
"      \"email\" : \"smartin@email.com\","+
"      \"id\" : \"02002\""+
"    },"+
"    {"+
"      \"firstName\" : \"Ann\","+
"      \"lastName\" : \"Landers\","+
"      \"email\" : \"alanders@email.com\","+
"      \"id\" : \"73738\""+
"    }"+
"  ],"+
"  \"idiotic/propertyName\" : \"idiot!\"" +
"}";

  private final String [] tokens;

  private static final String VALID_ADDRESS_PATTERN = "^(?:/[A-z0-9* ]+)+$";

  public JSONAddress( String path ) {
    if( path != null ) {
      if( this.validate(path) ) {
        path = path.substring(1);
        this.tokens = path.split( "(?<!\\\\)/" );
        for( int i = 0; i < this.tokens.length; i++ ) {
          this.tokens[i] = this.unescape( this.tokens[i] );
        }
      } else {
        throw new RuntimeException( "Bad address!" );
      }
    } else {
      this.tokens = new String [0];
    }
  }

  private boolean validate( String path ) {
    // first replace any escaped slashes
    String pre = path.replaceAll("\\\\/", "SLASH" );
    return pre.matches( VALID_ADDRESS_PATTERN );
  }

  private String unescape( String token ) {
    return token.replaceAll("\\\\/","/");
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
