package org.mjm.euchre.card;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CardGroup {
  protected Set<Card> cards = null;
  
  public CardGroup() {
    this.cards = EnumSet.noneOf(Card.class);
  }
  
  public CardGroup( List<Card> cards ) {
    this();
    for( Card c : cards ) {
      this.cards.add(c);
    }
  }
  
  public CardGroup( Card [] cards )
  {
    this();
    for( Card c : cards ) {
      this.cards.add(c);
    }
  }
  
  public CardGroup getCardsOfSuit( CardSuit suit ) {
    return this.getCardsOfSuit(suit, suit);
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

  public CardGroup getCardsNotOfSuit( CardSuit suit ) {
    return this.getCardsNotOfSuit(suit, suit);
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
  
  /**
   * Order the group such that all trump are at the start of the array, and order the rest by 
   * descending order of number of cards in suit, and card value.
   * 
   * e.g. trump is clubs:
   * 
   * 9-heart, jack-heart, 10-diamond, jack-spades, 10-clubs
   * 
   * should come back as:
   * 
   * jack-spades, 10-clubs, jack-heart, 9-heart, 10-diamond
   * 
   * @param trump
   * @return
   */
  public Card [] getTrumpOrder( CardSuit trump ) {
    return null;
  }
  
  public Card removeCard( Card card ) {
    return this.cards.remove(card) ? card : null;
  }
  
  /**
   * Attempt to add a card to this group. 
   * @param c
   * @return true if the card was added, false if the card was already in this card group
   */
  public boolean addCard( Card c ) {
    return this.cards.add(c);
  }
  
  public Set<Card> getCards() {
    return this.cards;
  }
  
  public int getNumCards() {
    return this.cards.size();
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
