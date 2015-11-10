import java.util.*;

public class LinkedHashMapTest {

  public static final String [] ALPHABET = {"A","B","C","D","E","F","G","H","I"};
  public static void main( String [] args ) {
    Map<String,Integer> mymap = new LinkedHashMap<String,Integer>();

    int counter = 0;
    for( String s : ALPHABET ) {
      mymap.put( s, counter++ );
    }

    for( String key : mymap.keySet() ) {
      System.out.println( key + "->" + mymap.get( key ) );
    }
  }
}
