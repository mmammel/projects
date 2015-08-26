package org.mjm.euchre.card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CardGroup {
  protected Set<Card> cards = null;
  protected Set<CardSuit> suits = null;
  protected Map<CardSuit,Integer> rawSuitCount = null;
  protected Map<CardSuit,Map<CardSuit,Integer>> trumpSuitCounts = null;
  protected Map<CardSuit,Integer> trumpScores = null;
  
  /**
   * guarantee our suit counters start with 0's for all suits.
   */
  public CardGroup() {
    this.cards = EnumSet.noneOf(Card.class);
    this.rawSuitCount = this.initializeSuitCountMap();
    this.trumpSuitCounts = new EnumMap<CardSuit,Map<CardSuit,Integer>>(CardSuit.class);
    
    for( CardSuit suit : CardSuit.values() ) {
      this.trumpSuitCounts.put(suit, this.initializeSuitCountMap());
    }
    
    this.trumpScores = this.initializeSuitCountMap();
  }
  
  private Map<CardSuit,Integer> initializeSuitCountMap() {
    Map<CardSuit,Integer> retVal = new EnumMap<CardSuit,Integer>(CardSuit.class);
    for( CardSuit suit : CardSuit.values() ) {
      retVal.put(suit, 0);
    }
    
    return retVal;
  }
  
  public CardGroup( List<Card> cards ) {
    this();
    for( Card c : cards ) {
      this.addCard(c);
    }
  }
  
  public CardGroup( Card [] cards )
  {
    this();
    for( Card c : cards ) {
      this.addCard(c);
    }
  }
  
  public Map<CardSuit,Integer> getRawSuitCount() {
    return this.rawSuitCount;
  }
  
  public Map<CardSuit,Integer> getTrumpSuitCount( CardSuit trump ) {
    return this.trumpSuitCounts.get(trump);
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
    Card [] retVal = this.cards.toArray(new Card[ this.cards.size()]);
    Arrays.sort(retVal, new TrumpCardComparator(trump, this.getTrumpSuitCount(trump)));
    return retVal;
  }
  
  public Card [] getTrickScoreOrder( CardSuit trump, CardSuit led ) {
    Card [] retVal = this.cards.toArray(new Card[ this.cards.size()]);
    Arrays.sort(retVal, new TrickScoreComparator(trump, led));
    return retVal;
  }
  
  public Card removeCard( Card card ) {
    Card retVal = null;
    int tempVal = 0;
    CardSuit suit = card.suit();
    CardSuit next = Card.getNextSuit(card);
    if( card != null ) {
      tempVal = this.rawSuitCount.get(suit);
      this.rawSuitCount.put(suit, tempVal - 1);

      for( CardSuit s : CardSuit.values() ) {

        tempVal = this.trumpScores.get(s);
        this.trumpScores.put(s, tempVal - card.getCardScore(s));
        
        if( card.value() == CardVal.Jack && s == next ) {
          tempVal = this.trumpSuitCounts.get(s).get(next);
          this.trumpSuitCounts.get(s).put(next, tempVal - 1);
        } else {        
          tempVal = this.trumpSuitCounts.get(s).get(suit);
          this.trumpSuitCounts.get(s).put(suit, tempVal - 1);
        }
      }

      retVal =  this.cards.remove(card) ? card : null;
    }
    
    return retVal;
  }
  
  /**
   * Attempt to add a card to this group. 
   * @param c
   * @return true if the card was added, false if the card was already in this card group
   */
  public boolean addCard( Card card ) {
    boolean retVal = false;
    int tempVal = 0;
    CardSuit suit = card.suit();
    CardSuit next = Card.getNextSuit(card);
    if( card != null ) {
      tempVal = this.rawSuitCount.get(suit);
      this.rawSuitCount.put(suit, tempVal + 1);

      for( CardSuit s : CardSuit.values() ) {

        tempVal = this.trumpScores.get(s);
        this.trumpScores.put(s, tempVal + card.getCardScore(s));
        
        //System.out.println( "Adding card " + card + " bumped " + s + " trumpscore from " + tempVal + " to " + this.trumpScores.get(s));
        
        if( card.value() == CardVal.Jack && s == next ) {
          tempVal = this.trumpSuitCounts.get(s).get(next);
          this.trumpSuitCounts.get(s).put(next, tempVal + 1);
        } else {        
          tempVal = this.trumpSuitCounts.get(s).get(suit);
          this.trumpSuitCounts.get(s).put(suit, tempVal + 1);
        }
      }

      retVal = this.cards.add(card);
    }
    
    return retVal;
  }
  
  public int getTrumpScore( CardSuit trump ) {
    return this.trumpScores.get(trump);
  }
  
  public Set<Card> getCards() {
    return this.cards;
  }
  
  public int getNumCards() {
    return this.cards.size();
  }
  
  public static String cardArrayToString(Card [] cards ) {
    StringBuilder sb = new StringBuilder();
    for( Card c : cards ) {
      sb.append("[").append(c).append("]");
    }
    
    return sb.toString();
  }
  
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    Card [] sorted = this.cards.toArray(new Card[this.cards.size()]);
    Arrays.sort(sorted,new CardComparator( this.getRawSuitCount()) );
    for( Card c : sorted )
    {
      if( c != null ) {
        sb.append( "["+c+"]" );
      }
    }

    return sb.toString();
  }
}
