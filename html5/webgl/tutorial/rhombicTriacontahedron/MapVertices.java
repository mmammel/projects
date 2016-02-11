import java.util.*;

/*
 The vertices of the RT are labeled in my diagram like:
  2,4,6,8,11,12,13,16,...,60

  but we want to store them in a simple array, so we just need to map these to:

  0,1,2,3,4,5,6,...,31

  I have a face definition file that shows the original vertex labels:

  2,8,17,18,
  2,4,12,11,
  ...

   and I want to produce the same thing but with the 0-based indices.
*/
public class MapVertices {
  public static String FACES = "2,8,17,18,2,4,12,11,2,4,6,8,2,11,27,20,2,20,37,18,27,11,12,28,27,28,46,45,27,45,54,38,27,38,37,20,33,16,6,23,33,23,31,41,33,41,58,50,33,50,51,34,33,34,17,16,17,8,6,16,17,34,51,36,17,36,37,18,51,36,37,52,51,52,54,60,51,60,58,50,6,4,12,13,6,13,31,23,46,30,31,47,46,47,58,56,46,56,54,45,46,28,12,30,12,13,31,30,31,41,58,47,54,38,37,52,54,56,58,60";

  public static void main( String [] args ) {
    String [] arr = FACES.split( ",");
    Integer [] iarr = new Integer [ arr.length ];
    Map<Integer,Integer> mapper = new TreeMap<Integer,Integer>();

    for( int i = 0; i < arr.length; i++ ) {
      iarr[i] = Integer.parseInt( arr[i] ); 
    }

    for( int j = 0; j < iarr.length; j++ ) {
      mapper.put( iarr[j], 99 );
    }

    int count = 0;
    for( Integer key : mapper.keySet() ) {
      mapper.put( key, count++);
    }

    for( int k = 0; k < iarr.length; k++ ) {
      if( k > 0 ) System.out.print(",");
      if( k > 0 && k%4 == 0 ) System.out.println("");
      System.out.print( mapper.get( iarr[k] ) );
    }
  }
  

}
