import net.sf.json.JSONObject;
import java.util.*;

public class NetSfJsonTest
{
  public static void main( String [] args ) {
    Map<String,Object> testObj = new HashMap<String,Object>();
    List<String> someArray = new ArrayList<String>();
    someArray.add( "foo" );
    someArray.add( "bar" );
    StringBuilder sb = new StringBuilder();
    sb.append("<this>\n").append("  <is \"attr\">a</is>\n").append("  <multi>line</multi>\n").append("</this>");
    testObj.put("payload", sb.toString() );
    Map<String,String> someObj = new HashMap<String,String>();
    someObj.put( "max", "mammel" );
    someObj.put( "jevne", "bohnke" );
    someObj.put( "jomama", "foo" );
    testObj.put( "name", "Jomama" );
    testObj.put( "myobj", someObj );
    testObj.put( "myarr", someArray );

    JSONObject j = new JSONObject();
    j.accumulateAll( someObj );
    System.out.println( j.toString() );
    System.out.println( j.toString(2) );
  }
}
