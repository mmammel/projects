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
  protected CardSuit trump = null;
  protected Set<Card> cards = null;
  protected Set<CardSuit> suits = null;
  protected Map<CardSuit,Integer> rawSuitCount = null;
  protected Map<CardSuit,Map<CardSuit,Integer>> trumpSuitCounts = null;
  protected Map<CardSuit,Integer> trumpScores = null;
  
  private Map<CardSuit,Card []> cachedTrumpRanks = null;

  /**
   * guarantee our suit counters start with 0's for all suits.
   */
  public CardGroup() {
    this.cards = EnumSet.noneOf(Card.class);
    this.rawSuitCount = this.initializeSuitCountMap();
    this.trumpSuitCounts = new EnumMap<CardSuit,Map<CardSuit,Integer>>(CardSuit.class);
    this.cachedTrumpRanks = new EnumMap<CardSuit,Card []>(CardSuit.class);
    
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
  
  /**
   * Get the highest valued card
   * @return
   */
  public Card getHighestCard() {
    return this.getHighestCard(this.trump);
  }
  
  public Card getHighestCard( CardSuit trump ) {
    Card retVal = null;
    Card [] ranked = this.getTrumpOrder(trump);
    if( ranked != null && ranked.length > 0 ) {
      retVal = ranked[ ranked.length - 1 ];
    }
    
    return retVal;
  }
  
  /**
   * Get the highest card of a particular suit.
   * @param suit
   * @return
   */
  public Card getHighestCardOfSuit( CardSuit suit ) {
    return this.getHighestCardOfSuit(suit, this.trump);
  }
  
  public Card getHighestCardOfSuit( CardSuit suit, CardSuit trump ) {
    Card retVal = null;
    
    Card [] ranked = this.getTrumpOrder( trump );
    if( ranked != null && ranked.length > 0 ) {
      for( int i = ranked.length - 1; i >= 0; i-- ) {
        if( ranked[i].suit(trump) == suit ) {
          retVal = ranked[i];
          break;
        }
      }
    }
    
    return retVal;
  }
  
  /**
   * The lowest card possible, and the one with the fewest suit mates.
   * @return
   */
  public Card getLowestCard() {
    return this.getLowestCard(this.trump);
  }
  
  public Card getLowestCard( CardSuit trump ) {
    Card retVal = null;
    Card [] ranked = this.getTrumpOrder(trump);
    if( ranked != null && ranked.length > 0 ) {
      retVal = ranked[0];
    }
    
    return retVal;
  }
  
  /**
   * Get the lowest ranked card of a particular suit.
   * @param suit
   * @return
   */
  public Card getLowestCardOfSuit( CardSuit suit ) {
    return this.getLowestCardOfSuit(suit, this.trump);
  }
  
  public Card getLowestCardOfSuit( CardSuit suit, CardSuit trump ) {
    Card retVal = null;
    
    Card [] ranked = this.getTrumpOrder( trump );
    if( ranked != null && ranked.length > 0 ) {
      for( int i = 0; i < ranked.length; i++ ) {
        if( ranked[i].suit(trump) == suit ) {
          retVal = ranked[i];
          break;
        }
      }
    }
    
    return retVal;
  }
  
  public Map<CardSuit,Integer> getRawSuitCount() {
    return this.rawSuitCount;
  }
  
  public Map<CardSuit,Integer> getTrumpSuitCount() {
    return this.getTrumpSuitCount(this.trump);
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
  public Card [] getTrumpOrder() {
    return this.getTrumpOrder(this.trump);
  }
  
  public Card [] getTrumpOrder(CardSuit trump) {
    Card [] retVal = null;
    if( (retVal = this.cachedTrumpRanks.get(trump)) == null ) {
      retVal = this.cards.toArray(new Card[ this.cards.size()]);
      Arrays.sort(retVal, new TrumpCardComparator(trump, this.getTrumpSuitCount(trump)));
      this.cachedTrumpRanks.put(trump, retVal);
    }
    
    return retVal;
  }
  
  public Card [] getTrickScoreOrder( CardSuit led ) {
    return this.getTrickScoreOrder(this.trump, led);
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
    
    if( retVal != null ) {
      this.cachedTrumpRanks.clear();
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
    
    if( retVal ) {
      this.cachedTrumpRanks.clear();
    }
    
    return retVal;
  }
  
  public int getTrumpScore() {
    return this.getTrumpScore(this.trump);
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
  
  public CardSuit getTrump() {
    return trump;
  }

  public void setTrump(CardSuit trump) {
    this.trump = trump;
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
