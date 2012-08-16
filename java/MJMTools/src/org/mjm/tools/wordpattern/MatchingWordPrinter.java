package org.mjm.tools.wordpattern;

import org.mjm.tools.lookuptree.WordProcessor;

public class MatchingWordPrinter implements WordProcessor
{
  private String regex;
  
  public MatchingWordPrinter( String pattern )
  {
    this.regex = pattern;
  }
  
  public void processWord( String word )
  {
    if( word.matches( this.regex ) )
    {
      System.out.println( word );
    }
  }
}
