package org.mjm.tools.wordpattern;

import org.mjm.tools.lookuptree.WordProcessor;
import java.util.List;
import java.util.ArrayList;

public class WordCollector implements WordProcessor
{
  private String regex;
  private List<String> words;
  
  public WordCollector( String pattern )
  {
    this();
    this.regex = pattern;
  }
 
  public WordCollector()
  {
    this.words = new ArrayList<String>();
  }
 
  public void processWord( String word )
  {
    if( this.regex == null || word.matches( this.regex ) )
    {
      this.words.add( word );
    }
  }

  public List<String> getWords()
  {
    return this.words;
  }
}
