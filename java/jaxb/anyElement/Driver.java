import java.io.BufferedReader;
import java.io.FileReader;
import org.codehaus.jackson.map.ObjectMapper;

public class Driver {
  public static void main( String [] args ) {
    String fname = args[0];
    BufferedReader input_reader = null;
    String input_str = "";
    StringBuilder jsonString = new StringBuilder();

    try
    {
      input_reader = new BufferedReader( new FileReader ( fname ) );
      while( (input_str = input_reader.readLine()) != null )
      {
          jsonString.append( input_str );
      }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
    
    ObjectMapper mapper = null;
    InvokeRequest req = null;

    try {
      mapper = JacksonUtil.getMapper(false);
      req = mapper.readValue(jsonString.toString(), InvokeRequest.class);
    } catch( Exception e ) {
      e.printStackTrace();
      System.out.println( e.toString() );
    }

    System.out.println( req );
  }
}