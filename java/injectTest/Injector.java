 import java.util.*;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;

 public class Injector {
  public static void main( String [] args ) {
    String query = "SELECT t.* FROM table t  WHERE ${CRITERIA} AND t.foobar = 84 AND t.bar = 'foo' AND ${DOIT?|t.foobar = 'Yomama' AND }t.parentKey = 'PANCAKES'";

    String val = "(t.column like $$S'%windows')";

    String result = Injector.injectUnquoted( query, "CRITERIA", val, "DOIT?", "true" );
    System.out.println( "Result: " + result );
  } 

  /**
   * We might want to inject snippets that contain quotes
   * @param query
   * @param pairs
   * @return
   */
  public static String injectUnquoted( String query, Map<String,String> pairs ) {
    String retVal = query;
    for( String key : pairs.keySet() ) {
      retVal = inject( retVal, key, pairs.get(key), false);
    }
    
    return retVal;
  }
  
  /**
   * This will quote any single quotes in the replacements
   * @param query
   * @param pairs
   * @return
   */
  public static String inject( String query, Map<String,String> pairs ) {
    String retVal = query;
    for( String key : pairs.keySet() ) {
      retVal = inject( retVal, key, pairs.get(key), true);
    }
    
    return retVal;
  }
  
  /**
   * We might want to inject snippets of query that contain quotes.
   * @param query
   * @param var
   * @param val
   * @return
   */
  public static String injectUnquoted( String query, String var, String val ) {
    return inject(query, var, val, false);
  }
  
  /**
   * Quote any single quotes in the replacement
   * @param query
   * @param var
   * @param val
   * @return
   */
  public static String inject(String query, String var, String val ) {
    return inject(query, var, val, true);
  }
  
  /**
   * We might want to inject snippets of query that contain quotes.
   * @param query
   * @param var
   * @param val
   * @return
   */
  public static String injectUnquoted( String query, String ... vars ) {
    Map<String,String> varmap = new HashMap<String,String>();
    for( int i = 0; i < vars.length; i+=2 ) {
      varmap.put( vars[i], vars[i+1] );
    }
    return injectUnquoted( query, varmap );
  }
  
  public static String inject( String query, String ... vars ) {
    Map<String,String> varmap = new HashMap<String,String>();
    for( int i = 0; i < vars.length; i+=2 ) {
      varmap.put( vars[i], vars[i+1] );
    }
    return inject( query, varmap );
  }
  
  /**
   * Allow the callers to opt out of quoting single quotes
   * @param query
   * @param var
   * @param val
   * @param doQuoting
   * @return
   */
  public static String inject( String query, String var, String val, boolean doQuoting ) {
    String retVal = null;
    String replaceVal = val == null ? "" : (doQuoting ? val.replaceAll("'","''") : val);
    
    if( var.endsWith("?") ) {
      // conditional tag
      if( replaceVal != null && replaceVal.toUpperCase().equals("TRUE") ) {
        retVal = query.replaceAll( "\\$\\{" + Pattern.quote(var) + "\\|(.*?)\\}", "$1" );
      } else {
        retVal = query.replaceAll( "\\$\\{" + Pattern.quote(var) + "\\|(.*?)\\}", "" );
      }
    } else {
      retVal = query.replaceAll("\\$\\{"+var+"\\}", Matcher.quoteReplacement(replaceVal));
    }
    
    return retVal;
  }
  
  public static String replaceNewLines( String s ) {
    if( s != null ) {
      s = s.replaceAll( "\n", "'+CHAR(10)+'" );
      s = s.replaceAll( "\r", "'+CHAR(13)+'" );
    }
    
    return s;
  }
}
