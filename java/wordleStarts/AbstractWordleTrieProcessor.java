
public abstract class AbstractWordleTrieProcessor implements WordleTrieProcessor {
  
  protected WordleTrie root = null;
  
  public void process( WordleTrie wt ) {
    if( this.root == null ) {
      this.root = wt;
    }
    
    this.process_inner(wt, new WordleTrieProcessorContext() );
  }
  
  protected void process_inner( WordleTrie wt, WordleTrieProcessorContext context ) {
    if( wt.getLetter() != null ) {
      context.sb.append(wt.getLetter().getLet() );
      if( wt.isWord() ) {
        this.handleWord( context.sb.toString() );
      }
      
      context.idx++;
    }
    
    WordleTrie next = null;
    for( Letter l : Letter.values() ) {
      if( (next = wt.getNext(l)) != null ) {
        this.process_inner(next, context);
      }
    }
    
    if( wt.getLetter() != null ) {
      context.idx--;
      context.sb.deleteCharAt(context.idx);
    }
  }
  
  protected abstract void handleWord( String word );
}
