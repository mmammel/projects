import java.util.regex.*;
import java.util.TreeMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

class Solution {
    private static Pattern DIG_PATTERN = Pattern.compile("^([^ ]+?) ([0-9 ]+)$");
    private static Pattern LET_PATTERN = Pattern.compile("^([^ ]+?) ([a-z ]+)$");
    private static Pattern REAL_ID_PATTERN = Pattern.compile("^(.*?)__[0-9]+$");
    public String[] reorderLogFiles(String[] logs) {
      List<String> finalList = new ArrayList<String>();
      List<String> digLogs = new ArrayList<String>();
      Map<String,String> letLogMap = new HashMap<String,String>();
      Map<String,Set<String>> sortedContentMap = new TreeMap<String,Set<String>>();
      String id, content;
      Set<String> tempContentSet = null;
      int letIdId = 0;
      for( String log : logs ) {
          if( log.matches( DIG_PATTERN.pattern()) ) {
            digLogs.add(log);      
          } else {
              Matcher m = LET_PATTERN.matcher(log);
              if( m.find() ) {
                  id = m.group(1) + "__" + (letIdId++);
                  content = m.group(2);
                  letLogMap.put( id, log );
                  if( (tempContentSet = sortedContentMap.get(content)) == null ) {
                      tempContentSet = new TreeSet<String>();
                      sortedContentMap.put( content, tempContentSet );
                  }
                  
                  tempContentSet.add( id );
              }
          }
      }
    
      for( String key : sortedContentMap.keySet() ) {
          tempContentSet = sortedContentMap.get(key);
          for( String letId : tempContentSet ) {
              finalList.add( letLogMap.get(letId) );
          }
      }
      finalList.addAll( digLogs );
        
      String [] retVal = finalList.toArray( new String [0] );
        
      return retVal;
    }
}

