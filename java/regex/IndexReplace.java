import java.util.regex.*;
import java.util.*;

public class IndexReplace  {
  public static void main( String [] args ) {
    List<Integer> indexList = new ArrayList<Integer>();
    // count instances of '*' in the string
    int idx = 0;
    int count = 0;
    byte [] bytes = args[0].getBytes();

    for( int i = 0; i < bytes.length; i++ ) {
      if( bytes[i] == '*' ) {
        count++;
        indexList.add( i );
      }
    }

    int [] indices = new int [ count ];

    

    System.out.println( "Found " + count + " asterisks" );

    for( int j = 0; j < count; j++ ) {
      for( int k = 0; k < count; k++ ) {
      
      }
    }
    
  }

  
}
