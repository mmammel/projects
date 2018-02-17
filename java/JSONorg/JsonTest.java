import org.json.JSONObject;
import org.json.JSONException;
import java.util.*;

public class JsonTest
{
  public static void main( String [] args ) {
    Map<String,Object> testObj = new HashMap<String,Object>();
    List<String> someArray = new ArrayList<String>();
    someArray.add( "foo" );
    someArray.add( "bar" );
    Map<String,String> someObj = new HashMap<String,String>();
    someObj.put( "max", "mammel" );
    someObj.put( "jevne", "bohnke" );
    testObj.put( "name", "Jomama" );
    testObj.put( "myobj", someObj );
    testObj.put( "myarr", someArray );

    JSONObject j = new JSONObject( testObj );
    System.out.println( j.toString() );
  }
}
