package org.mjm.euchre.card;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractCardGroup {
  protected Card [] cards = null;

  public Card getCard( int i )
  {
    return cards[i];
  }

  public void setCard( int i, Card c )
  {
    cards[i] = c;
  }
 
  public AbstractCardGroup( Card [] cards )
  {
    this.cards = new Card [cards.length];
    for( int i = 0; i < cards.length; i++ )
    {
      this.cards[i] = cards[i];
    }
  }

  public Set<CardSuit> getSuiteSet() {
    Set<CardSuit> retVal = new HashSet<CardSuit>();
    for( Card c : this.cards ) {
      retVal.add(c.suit());
    }
    
    return retVal;
  }
  
  public Card [] getCards() {
    return this.cards;
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();

    for( Card c : this.cards )
    {
      sb.append( "["+c+"]" );
    }

    return sb.toString();
  }
}
