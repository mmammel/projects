package org.mjm.euchre.card;

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
