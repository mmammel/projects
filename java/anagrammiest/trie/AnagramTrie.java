import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AnagramTrie {
  private char letter = 0;
  private Map<Character,AnagramTrie> nextMap;
  private int count = 0;

  public AnagramTrie( char c ) {
    this.letter = c;
    this.nextMap = new HashMap<Character, AnagramTrie>();
    this.count = 0;
  }

  public void addWord( String w ) {
    w = w.toUpperCase();
    char [] lets = w.toCharArray();
    Arrays.sort(lets);
    this.addCharArray( lets, 0 ); 
  }

  public void addCharArray( char [] arr, int idx ) {
    char c = arr[idx];
    AnagramTrie tempNext = null;
    if( nextMap.containsKey( c ) ) {
      tempNext = this.nextMap.get( c );
    } else {
      tempNext = new AnagramTrie( c );
    }
    
    if( ++idx == arr.length ) {
      // end of the array.
      this.count++;
    } else {
      tempNext.addCharArray( arr, ++idx );
    }
  }

  public void printTrie() {
  }

  public static void main( String [] args ) {
    String s = args[0];

    char [] lets = s.toCharArray();
    Arrays.sort(lets);
    String foo = new String( lets );
    System.out.println( foo );
    System.out.println(foo.replaceAll( "([A-Z]{2,})", " $1 " ));
  }
}
