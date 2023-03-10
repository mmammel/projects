import java.util.regex.*;
import java.util.*;

public class MetaRegex {

  private static Pattern REROUTE_CONDITION_PATTERN = Pattern.compile("^(.*?)~(.*?)~(.*?)$");

  public static void main( String [] args) {
    Map<String,Object> props = new HashMap<String,Object>();
    props.put("name", "Max");
    props.put("id","X123:45");

    String condition = args[0];
    boolean matched = false;

    String fieldName = null, pattern = null, integrationId = null, fieldValue = null;;

    if( props != null ) {
      Matcher m = REROUTE_CONDITION_PATTERN.matcher(condition);
      if( m.matches() ) {
        fieldName = m.group(1);
        pattern = m.group(2);
        integrationId = m.group(3);

        
        if( props.containsKey(fieldName) && (fieldValue = (String)props.get(fieldName)) != null ) {
          if( fieldValue.matches(pattern) ) {
            if( integrationId != null ) {
              matched = true;
              if( integrationId.toLowerCase().equals("ignore") ) {
                System.out.println( "IGNORE!" );
              } else {
                System.out.println( "Route to: " + integrationId );
              }
            }
          }
        }
      }
    }

    if( !matched ) System.out.println( "No match!" );

  }

}
