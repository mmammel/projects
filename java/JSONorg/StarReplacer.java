public class StarReplacer {
  public static final String JSON_STRING = "{\"foobar\" : \"jomama\", \"people\" : [{\"first\" : \"Max\", \"last\" : \"Mammel\"}, {\"first\" : \"Jevne\", \"last\" : \"Bohnke\"} ], \"animals\" : {\"mammals\" : [{\"type\" : \"dog\", \"name\" : \"malla\"}, {\"type\" : \"cat\", \"name\" : \"binky\"} ], \"reptiles\" : [{\"type\" : \"anole\", \"name\" : \"greeny\"} ] } }";

  private static final int MAX_IDX = 3;

  public static void main( String [] args ) {
    List<String> result = evaluate( args[0], new JSONObject( JSON_STRING ) );
    for( String s : result ) {
      System.out.println( s );
    }
  }

  private static void increment( int idx, int [] indices, String [] arr, JSONObject j, List<String> result ) {
    if( idx == indices.length ) {
      System.out.println( stitchArray( arr, indices ) );
      return;
    } else {
      for( int i = 0; i < MAX_IDX; i++ ) {
        indices[idx] = i;
        increment( idx + 1, indices, arr );
      }
    }
  }

  private static String stitchArray( String [] a, int [] s ) {
    StringBuilder sb = new StringBuilder( a[0] );
    for( int i = 0; i < s.length; i++ ) {
      sb.append( s[i] ).append( a[i+1] );
    }
    return sb.toString();
  }

  public static boolean doQuery( JSONPointer pointer, JSONObject j, List<String> results ) {
    boolean retVal = false;
    Object temp = null;
    if( pointer != null && j != null && results != null ) {
      try {
      } catch( JSONPointerException jpe ) {
        retVal = false;
      }
    }
  }

  public static List<String> evaluate( String path, JSONObject j ) {
    List<String> retVal = new ArrayList<String>();
    String [] segs = path.split("\\*");
    int [] indices = new int [ segs.length - 1 ];
    increment( 0, indices, segs, j,  );
  }
}
