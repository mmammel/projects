import org.json.*;
import java.util.*;
import java.io.*;

public class JSONConverter {

  public static void main( String [] args ) {
    if( args.length != 2 ) {
      System.err.println( "Usage: java JSONConverter <tableName> <inputJsonFileName>" );
      System.exit(1);
    }

    int outputFileCount = 1;
    FileInputStream fis = null;
    String tableName = args[0];

    JSONObject finalObj = null;
    JSONArray items = null;
    JSONObject tempPutRequest = null;

    try {
      fis = new FileInputStream( args[1] );

      String inputJson = StreamUtil.stringFromStream( fis ).trim();

      if( inputJson.startsWith( "[" ) ) {
        //bulk
        JSONArray arr = new JSONArray( inputJson );
        JSONObject temp = null;
        int itemCount = 0;
        for( Object o : arr ) {
          if( itemCount % 25 == 0 ) {
            if( itemCount > 0 ) {
              finalObj.put(tableName, items);
              JSONConverter.writeOutputFile(finalObj, tableName + outputFileCount++);
            }
            items = new JSONArray();
            finalObj = new JSONObject();
          }
          temp = (JSONObject)o;
          tempPutRequest = new JSONObject();
          tempPutRequest.put( "PutRequest", JSONConverter.convertObject( temp ) );
          items.put( tempPutRequest );
          itemCount++;
        }

        if( items.length() > 0 ) {
          finalObj.put(tableName, items);
          JSONConverter.writeOutputFile(finalObj, tableName + outputFileCount);
        }
      } else {
        // single
        items = new JSONArray();
        finalObj = new JSONObject();
        tempPutRequest = new JSONObject();
        tempPutRequest.put( "PutRequest", JSONConverter.convertObject( new JSONObject( inputJson ) ) );
        items.put( tempPutRequest );
        finalObj.put( tableName, items );
        JSONConverter.writeOutputFile(finalObj, tableName + outputFileCount);
      }      
    } catch( Exception e ) {
      System.out.println( "Error: " + e.toString() );
    } finally {
      try {
        if( fis != null ) fis.close();
      } catch( IOException ioe ) {
        System.out.println( "Error closing: " + ioe.toString() );
      }
    }
  }

  /**
   * Expects a flat object with strings or ints.
   */
  public static JSONObject convertObject( JSONObject obj ) {
    JSONObject retVal = new JSONObject();
    JSONObject item = new JSONObject();
    JSONObject tempField = null;

    Map<String,Object> objMap = obj.toMap();

    Object o = null;
    for( String k : objMap.keySet() ) {
      o = objMap.get(k);
      tempField = new JSONObject();
      if( o.getClass().getName().equals( "java.lang.String" ) ) {
        tempField.put( "S", o.toString() );
      } else {
        // number.
        tempField.put( "N", o.toString() );
      }

      item.put( k, tempField );
    }

    retVal.put( "Item", item );

    return retVal;
  }

  public static void writeOutputFile( JSONObject obj, String fname ) {
    OutputStream os = null;
    try {
      os = new FileOutputStream(fname + ".json");
      StreamUtil.stream(os, obj.toString(2) );
    } catch( IOException ioe ) {
      System.out.println( "Error writing output file: " + ioe.toString());
    } finally {
      if( os != null ) {
        try {
          os.close();
        } catch( IOException ioe2 ) {
          System.out.println( "Error closing stream : " + ioe2.toString() );
        }
      }
    }
  }
}
