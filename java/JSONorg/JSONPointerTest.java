import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONPointer;
import java.util.*;

public class JSONPointerTest
{
  /*
     {
  "foobar" : "jomama",
  "people" : [
    {
      "first" : "Max",
      "last" : "Mammel"
    },
    {
      "first" : "Jevne",
      "last" : "Bohnke"
    }
  ],
  "animals" : {
    "mammals" : [
       {
         "type" : "dog",
         "name" : "malla"
       },
       {
         "type" : "cat",
         "name" : "binky"
       }
    ],
    "reptiles" : [
      {
        "type" : "anole",
        "name" : "greeny"
      }
    ]
  }
}
*/
  public static final String JSON_STRING = "{\"foobar\" : \"jomama\", \"people\" : [{\"first\" : \"Max\", \"last\" : \"Mammel\"}, {\"first\" : \"Jevne\", \"last\" : \"Bohnke\"} ], \"animals\" : {\"mammals\" : [{\"type\" : \"dog\", \"name\" : \"malla\"}, {\"type\" : \"cat\", \"name\" : \"binky\"} ], \"reptiles\" : [{\"type\" : \"anole\", \"name\" : \"greeny\"} ] } }";
  public static void main( String [] args ) {
    JSONObject j = new JSONObject( JSON_STRING );
    JSONPointer jp = new JSONPointer( args[0] );
    Object result = jp.queryFrom( j );
    System.out.println( result.getClass().getName() );
    if( result.getClass() == java.lang.String.class ) {
      System.out.println( "It's a string!" );
    } else if( result.getClass() == org.json.JSONArray.class ) {
      System.out.println( "It's an array!" );
    }
    System.out.println( result.toString() );

    System.out.println( "Get a list: " );
    List<String> aslist = evaluateToList( args[0], j );
    for( String s : aslist ) {
      System.out.println(s);
    }
    System.out.println( "...done!");
  }

  public static List<String> evaluateToList( String path, JSONObject j ) {
    List<String> retVal = new ArrayList<String>();
    JSONPointer p = new JSONPointer( path );
    Object result = p.queryFrom( j );

    if( result != null ) {
      if( result.getClass() == java.lang.String.class ) {
        retVal.add( result.toString() );
      } else if ( result.getClass() == org.json.JSONArray.class ) {
        Object temp = null;
        JSONArray arr = (JSONArray)result;
        for( int i = 0; i < arr.length(); i++ ) {
          temp = arr.get( i );
          if( temp != null && temp.getClass() == java.lang.String.class ) {
            retVal.add( temp.toString() );
          }
        }
      }
    }

    return retVal;
    
  }
}
