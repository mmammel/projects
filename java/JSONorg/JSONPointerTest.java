import org.json.JSONObject;
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
    System.out.println( result.toString() );
  }
}
