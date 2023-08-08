import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class AnagramFinder {

  private static final String DICTIONARY_FILE = "scrabble_words.txt";
  
  private Map<String,Set<LetterSet>> index = null;
  
  public AnagramFinder() {
    this.index = new HashMap<String,Set<LetterSet>>();
    this.loadDictionary();
  }
  
  public Set<String> findAnagrams( String word ) {
    int len = word.length();
    
    Set<String> retVal = new TreeSet<String>();    
    Set<LetterSet> rootSet = LetterSet.setForWord(word);
    
    for( String key : this.index.keySet() ) {
      if( key.length() <= len ) {
        if( rootSet.containsAll(this.index.get(key))) {
          retVal.add(key);
        }
      }
    }
    
    return retVal;
  }
  
//  private String normalize( String word ) {
//    word = word.toLowerCase();
//    word.replaceAll("[^a-z]", "");
//    char [] lets = word.toCharArray();
//    Arrays.sort(lets);
//    return new String( lets );    
//  }
  
  private void loadDictionary() {
    BufferedReader input_reader = null;
    String input_str = "";

    try
    {
      input_reader = new BufferedReader( new FileReader ( DICTIONARY_FILE ) );

      while( (input_str = input_reader.readLine()) != null )
      {
        this.index.put(input_str, LetterSet.setForWord(input_str) );
      }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
  
  public static void main(String [] args) {
    BufferedReader input_reader = null;
    String input_str = "";
    Set<String> anagrams = null;
    Set<String> pangrams = null;
    boolean first = true;
    
    AnagramFinder AF = new AnagramFinder();
    
    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( "Type a word to find all of its sub-words in the scrabble dictionary.  Type \"kwit\" to exit" );

      System.out.print("$> ");

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "kwit" ) )
        {
          break;
        }
        else
        {
          // Process Input
          anagrams = AF.findAnagrams(input_str);
          pangrams = new TreeSet<String>();
          
          for( String a : anagrams ) {
            if( a.length() == input_str.length() ) {
              pangrams.add(a);
            }
            System.out.println(a);
          }
          
          System.out.print( "\nTotal: " + anagrams.size() + ", " + pangrams.size() + " Pangram" + (pangrams.size() == 1 ? "" : "s") + ": " );
          
          for( String p : pangrams ) {
            if( first ) {
              first = false;
            } else {
              System.out.print(", ");
            }
            
            System.out.print(p);
          }
          
          first = true;
          System.out.println("");
        }

        System.out.print( "\n$> " );
      }

      System.out.println( "Seeya Later Alligator" );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}
