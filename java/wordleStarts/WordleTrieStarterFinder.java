import java.util.EnumSet;

public class WordleTrieStarterFinder extends AbstractWordleTrieProcessor {

  private String [] solutions = null;
  private int solutionIdx = 0;
  private int wordLen = 0;
  private int numWords = 0;
  private EnumSet<Letter> letters = null;
  
  public WordleTrieStarterFinder( String letters, int wordLen ) {
    this.wordLen = wordLen;
    this.numWords = letters.length() / wordLen;
    
    this.solutions = new String [ this.numWords ];
    
    this.letters = EnumSet.noneOf(Letter.class);
    
    for( int i = 0; i < letters.length(); i++ ) {
      this.letters.add(Letter.getLetterFromChar(letters.charAt(i)));
    }
  }
  
  @Override
  protected void process_inner( WordleTrie wt, WordleTrieProcessorContext context ) {
    
    if( wt.getLetter() != null ) {
      context.sb.append(wt.getLetter().getLet() );
      this.letters.remove(wt.getLetter());      
      
      if( wt.isWord() ) {
        this.handleWord( context.sb.toString() );
      }
      
      context.idx++;
    }
    
    WordleTrie next = null;
    Letter prevFirstLet = null;
    for( Letter l : this.letters ) {
      if( context.idx == 0 && this.solutionIdx > 0 ) {
        // get the first letter of the previous solution word.
        prevFirstLet = Letter.getLetterFromChar(this.solutions[this.solutionIdx - 1].charAt(0));
        if( l.ordinal() <= prevFirstLet.ordinal() ) {
          // avoid combinatorial dupes by always finding solutions in 
          // alphabetical order.  Bonus of the EnumSet iteration honoring
          // "natural order"
          continue;
        }
      }
      if( (next = wt.getNext(l)) != null ) {
        this.process_inner(next, context);
      }
    }
    
    if( wt.getLetter() != null ) {
      this.letters.add(wt.getLetter());
      context.idx--;
      context.sb.deleteCharAt(context.idx);
    }
  }
  
  private void printSolution() {
    StringBuilder res = new StringBuilder();
    res.append("[");
    for( int i = 0; i < this.solutions.length; i++ ) {
      if( i > 0 ) res.append(",");
      res.append( this.solutions[i]);
    }
    res.append("]");
    
    System.out.println(res.toString());
  }
  
  @Override
  protected void handleWord( String word ) {
    if( word.length() == this.wordLen ) {
      // it's part of a potential solution
      this.solutions[ this.solutionIdx++ ] = word;

      if( this.solutionIdx == this.numWords ) {
        // It's the last word of a solution, print it
        this.printSolution();
      } else {
        // start back over.
        this.process_inner(this.root, new WordleTrieProcessorContext());
      }
      
      this.solutionIdx--;
    }
  }
  
  
}
