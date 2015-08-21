package org.mjm.euchre.card;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CardGroup {
  protected Card [] cards = null;

  public Card getCard( int i )
  {
    return cards[i];
  }

  public void setCard( int i, Card c )
  {
    cards[i] = c;
  }
 
  public CardGroup( int size ) {
    this.cards = new Card [size];
    for( int i = 0; i < size; i++ ) {
      this.cards[i] = null;
    }
  }
  
  public CardGroup( List<Card> cards ) {
    this.cards = cards.toArray(new Card[0]);
  }
  
  public CardGroup( Card [] cards )
  {
    this.cards = new Card [cards.length];
    for( int i = 0; i < cards.length; i++ )
    {
      this.cards[i] = cards[i];
    }
  }
  
  public CardGroup getCardsOfSuit( CardSuit suit, CardSuit trump ) {
    List<Card> result = new ArrayList<Card>();
    for( Card c : this.cards ) {
      if( suit != trump && c.suit() == suit && !c.isTrump(trump) ) {
        result.add(c);
      } else if( c.isTrump(trump) ){
        // we're looking for trumps
        result.add(c);
      }
    }
    
    return new CardGroup( result );
  }

  public CardGroup getCardsNotOfSuit( CardSuit suit, CardSuit trump ) {
    List<Card> result = new ArrayList<Card>();
    for( Card c : this.cards ) {
      if( suit != trump && (c.suit() != suit || c.isTrump(trump)) ) {
        result.add(c);
      } else if( !c.isTrump(trump) ){
        // we're looking for trumps
        result.add(c);
      }
    }
    
    return new CardGroup( result );
  }
  
  public Set<CardSuit> getSuitSet() {
    Set<CardSuit> retVal = new HashSet<CardSuit>();
    for( Card c : this.cards ) {
      if( c != null ) {
        retVal.add(c.suit());
      }
    }
    
    return retVal;
  }
  
  public Card removeCard( Card card ) {
    Card retVal = null;
    Card c = null;
    for( int i = 0; i < this.cards.length; i++ ) {
      c = this.cards[i];
      if( c == card ) {
        this.cards[i] = null;
        retVal = card;
      }
    }
    
    return retVal;
  }
  
  /**
   * Attempt to add a card to this group. 
   * @param c
   * @return true if the card was added, false if there was no more room in this card group
   */
  public boolean addCard( Card c ) {
    int count = 0;
    for( count = 0; count < this.cards.length; count++ ) {
      if( this.cards[count] == null ) {
        this.cards[count] = c;
        break;
      }
    }
    
    return( count < this.cards.length );
  }
  
  public Card [] getCards() {
    return this.cards;
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();

    for( Card c : this.cards )
    {
      if( c != null ) {
        sb.append( "["+c+"]" );
      }
    }

    return sb.toString();
  }
}
