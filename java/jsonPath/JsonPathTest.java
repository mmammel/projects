import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.mapper.*;
import com.jayway.jsonpath.spi.json.*;
import java.util.*;

public class JsonPathTest {

  public static void main( String [] args ) {

Configuration.setDefaults(new Configuration.Defaults() {

    private final JsonProvider jsonProvider = new JsonOrgJsonProvider();
    private final MappingProvider mappingProvider = new JsonOrgMappingProvider();
      
    @Override
    public JsonProvider jsonProvider() {
        return jsonProvider;
    }

    @Override
    public MappingProvider mappingProvider() {
        return mappingProvider;
    }
    
    @Override
    public Set<Option> options() {
        return EnumSet.noneOf(Option.class);
    }
});

    String json = "{ \"store\": { \"book\": [ { \"category\": \"reference\", \"author\": \"Nigel Rees\", \"title\": \"Sayings of the Century\", \"price\": 8.95 }, { \"category\": \"fiction\", \"author\": \"Evelyn Waugh\", \"title\": \"Sword of Honour\", \"price\": 12.99 }, { \"category\": \"fiction\", \"author\": \"Herman Melville\", \"title\": \"Moby Dick\", \"isbn\": \"0-553-21311-3\", \"price\": 8.99 }, { \"category\": \"fiction\", \"author\": \"J. R. R. Tolkien\", \"title\": \"The Lord of the Rings\", \"isbn\": \"0-395-19395-8\", \"price\": 22.99 } ], \"bicycle\": { \"color\": \"red\", \"price\": 19.95 } }, \"expensive\": 10 }"; 
  
    JsonPath.read(json, "$.store.book[*].author");

  }

}
