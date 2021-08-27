import org.json.JSONStringer;

public class QuoteJSON {
  public static void main( String [] args ) {
    System.out.println( escapeString( args[0] ) );
  }

  public static String escapeString( String input ) {
    String retVal = new JSONStringer()
     .object()
         .key("QUOTED")
         .value(input)
     .endObject()
     .toString();

    System.out.println( retVal );
    retVal = retVal.replaceAll("\\{\"QUOTED\":\"(.*?)\"\\}", "$1" );

    return retVal;
  }
}
