import org.json.JSONObject;
import org.json.JSONException;
import java.util.*;

public class KeyTest
{
  public static void main( String [] args ) {
    Map<String,String> mymap = new HashMap<String,String>();
    JSONObject obj = new JSONObject( "{ \"foo\" : \"bar\", \"joe\" : 786 , \"max\" : { \"malla\" : \"mammel\" }}" );

    for( String k : obj.keySet() ) {
      System.out.println( "Key: " + k + " -> " + obj.get(k) );
      mymap.put( k, ""+obj.get(k) );
    }

    System.out.println( mymap );
  }
}
