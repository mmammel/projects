import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.*;

public class ElasticQuery
{
  public static void main( String [] args ) {
    Map<String,Map<String,String>> mymap = new HashMap<String,Map<String,String>>();

    Map<String,String> termMap = new HashMap<String,String>();
    Map<String,String> matchMap = new HashMap<String,String>();

    mymap.put( "term", termMap );
    mymap.put( "match", matchMap );

    termMap.put( "parentKey", "MARC3C" );
    termMap.put( "level", "BEG" );
    //matchMap.put( "questionText", "los" );

    System.out.println( ElasticQuery.buildSimpleQueryBody( mymap ) );
  }

  public static String buildSimpleQueryBody( Map<String,Map<String,String>> criteria ) {
    
    String retVal = null;
    
    if( criteria != null && criteria.size() > 0 ) {
      JSONObject root = new JSONObject();
      JSONObject queryObj = new JSONObject();
      JSONObject boolObj = new JSONObject();
      JSONArray mustArray = new JSONArray();
      JSONObject tempMustElement = null;
      JSONObject tempQuery = null;
      JSONObject tempCriteria = null;
      Map<String,String> tempCrit = null;
      String val = null;
      
      for( String query : criteria.keySet() ) {
        // query is "term" or "match", etc.
        tempCrit = criteria.get(query);
        for( String key : tempCrit.keySet() ) {
          val = tempCrit.get(key);
          tempCriteria = new JSONObject();
          tempCriteria.put(key, val);
          tempQuery = new JSONObject();
          tempQuery.put(query, tempCriteria );
          mustArray.put(tempQuery);
        }
      }
      
      boolObj.put("must", mustArray );
      queryObj.put("bool", boolObj );
      root.put("query", queryObj);
      
      retVal = root.toString();
    }
    
    return retVal;
  }
}
