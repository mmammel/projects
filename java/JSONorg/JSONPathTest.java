import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

public class JSONPathTest {

  public static final String JSON_STRING = "{\"foobar\" : \"jomama\", \"people\" : [{\"first\" : \"Max\", \"last\" : \"Mammel\"}, {\"first\" : \"Jevne\", \"last\" : \"Bohnke\"} ], \"animals\" : {\"mammals\" : [{\"type\" : \"dog\", \"name\" : \"malla\"}, {\"type\" : \"cat\", \"name\" : \"binky\"} ], \"reptiles\" : [{\"type\" : \"anole\", \"name\" : \"greeny\"} ] } }";

  public static void main( String [] args ) {
    Object document = Configuration.defaultConfiguration().jsonProvider().parse(JSON_STRING);

    Object result = JsonPath.read(document, args[0]);
    System.out.println( result );
  }
}
