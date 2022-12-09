import java.util.HashMap;
import java.util.Map;

public class WordleTrie {
  private Letter letter;
  private Map<Letter,WordleTrie> nextMap;
  private boolean isWord = false;
  private static int WORD_COUNT = 0;
  // private Set<Integer> profileCatalog = new HashSet<Integer>();
  
  public WordleTrie( Letter let ) {
    this.letter = let; // if null, this is the root.
    this.nextMap = new HashMap<Letter, WordleTrie>();
  }
  
  public void add( String word ) {
    if( word != null ) {
      // this.recordProfile(word);
      this.add(word.toLowerCase().toCharArray(), 0);
    }
  }
  
  public void add( char [] chars, int idx ) {
    if( idx < chars.length ) {
      Letter l = Letter.getLetterFromChar(chars[idx++] );
      WordleTrie next = null;
      if( (next = this.nextMap.get(l) ) == null ) {
        next = new WordleTrie(l);
        this.nextMap.put(l, next);
      }
    
      if( idx < chars.length ) {
        next.add( chars, idx );
      } else {
        next.setIsWord();
        WordleTrie.WORD_COUNT++;
      }
    }
  }
  
  public Letter getLetter() {
    return this.letter;
  }
  
  public boolean isWord() {
    return this.isWord;
  }
  
  public void setIsWord() {
    this.isWord = true;
  }
  
  public static int getWordCount() { 
    return WORD_COUNT;
  }  
  
  public WordleTrie getNext( Letter letter ) {
    return this.nextMap.get(letter);
  }
  
  public boolean hasNext( Letter letter ) {
    return this.nextMap.containsKey(letter);
  }
  
  public int getCount() {
    int retVal = 1;
    
    WordleTrie next = null;
    for( Letter l : this.nextMap.keySet() ) {
      next = this.nextMap.get(l);
      retVal += next.getCount();
    }
    
    return retVal;
  }
}
