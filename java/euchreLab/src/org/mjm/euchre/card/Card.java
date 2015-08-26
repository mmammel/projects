package org.mjm.euchre.card;

import java.util.Map;
import java.util.HashMap;

public enum Card {
  NineOfDiamonds(CardSuit.Diamonds,CardVal.Nine),
  TenOfDiamonds(CardSuit.Diamonds,CardVal.Ten),
  JackOfDiamonds(CardSuit.Diamonds,CardVal.Jack),
  QueenOfDiamonds(CardSuit.Diamonds,CardVal.Queen),
  KingOfDiamonds(CardSuit.Diamonds,CardVal.King),
  AceOfDiamonds(CardSuit.Diamonds,CardVal.Ace),
  NineOfHearts(CardSuit.Hearts,CardVal.Nine),
  TenOfHearts(CardSuit.Hearts,CardVal.Ten),
  JackOfHearts(CardSuit.Hearts,CardVal.Jack),
  QueenOfHearts(CardSuit.Hearts,CardVal.Queen),
  KingOfHearts(CardSuit.Hearts,CardVal.King),
  AceOfHearts(CardSuit.Hearts,CardVal.Ace),
  NineOfSpades(CardSuit.Spades,CardVal.Nine),
  TenOfSpades(CardSuit.Spades,CardVal.Ten),
  JackOfSpades(CardSuit.Spades,CardVal.Jack),
  QueenOfSpades(CardSuit.Spades,CardVal.Queen),
  KingOfSpades(CardSuit.Spades,CardVal.King),
  AceOfSpades(CardSuit.Spades,CardVal.Ace),
  NineOfClubs(CardSuit.Clubs,CardVal.Nine),
  TenOfClubs(CardSuit.Clubs,CardVal.Ten),
  JackOfClubs(CardSuit.Clubs,CardVal.Jack),
  QueenOfClubs(CardSuit.Clubs,CardVal.Queen),
  KingOfClubs(CardSuit.Clubs,CardVal.King),
  AceOfClubs(CardSuit.Clubs,CardVal.Ace);

  private final CardSuit suit;
  private final CardVal value;

  public CardSuit suit() { return this.suit; }
  public CardSuit suit( CardSuit trump ) { return this.isTrump(trump) ? trump : this.suit; }
  public CardVal  value() { return this.value; }
  public CardColor color() { return this.suit.color(); }

  private static Map<CardSuit,Map<CardVal,Card>> CARD_INDEX = new HashMap<CardSuit,Map<CardVal,Card>>();

  static {
    Map<CardVal,Card> tempMap = null;
    for( Card c : Card.values() )
    {
      if( (tempMap = CARD_INDEX.get(c.suit())) == null )
      {
        tempMap = new HashMap<CardVal,Card>();
        CARD_INDEX.put( c.suit(), tempMap );
      }

      tempMap.put( c.value(), c );
    }
  }

  Card( CardSuit suit, CardVal value )
  {
    this.suit = suit;
    this.value = value;
  }

  public static CardSuit getNextSuit( Card c ) {
    CardSuit retVal = null;
    switch(c.suit) {
      case Diamonds:
        retVal = CardSuit.Hearts;
        break;
      case Hearts:
        retVal = CardSuit.Diamonds;
        break;
      case Clubs:
        retVal = CardSuit.Spades;
        break;
      case Spades:
        retVal = CardSuit.Clubs;
        break;
      default:
        retVal = null;
        break;
    }
    
    return retVal;
  }
  
  public static Card getNextBower( Card c ) {
    Card retVal = null;
    if( c.value() == CardVal.Jack ) {
      switch(c) {
        case JackOfDiamonds:
          retVal = JackOfHearts;
          break;
        case JackOfHearts:
          retVal = JackOfDiamonds;
          break;
        case JackOfSpades:
          retVal = JackOfClubs;
          break;
        case JackOfClubs:
          retVal = JackOfSpades;
          break;
        default:
          retVal = null;
          break;
      }
    }
    
    return retVal;
  }
  
  /**
   * e.g. hearts:
   * J-h: 7
   * J-d: 6
   * Ace-h: 5
   * King-h: 4
   * Queen-h: 3
   * 10-h: 2
   * 9-h: 1
   * @param trump
   * @return
   */
  public int getOldCardScore( CardSuit trump ) {
    int retVal = 0;
    int rawRank = this.ordinal() % 6; // 9:0, 10:1, J:2, ...
    if( this.suit == trump ) {
      retVal = this.value == CardVal.Jack ? 7 : (rawRank > 1 ? rawRank : rawRank + 1);
    } else if( this.value == CardVal.Jack && this.suit.color() == trump.color() ) {
      retVal = 6;
    }
    
    System.out.println( "Returning " + retVal);
    
    return retVal;
  }
  
  public int getCardScore( CardSuit trump ) {
    //System.out.println( "Checking " + this + " against " + trump + "...");
    int retVal = 1;
    int rawRank = this.ordinal() % 6; // 9:0, 10:1, J:2, ...
    if( this.suit == trump ) {
      rawRank = this.value == CardVal.Jack ? 7 : (rawRank > 1 ? rawRank - 1 : rawRank);
    } else if( this.value == CardVal.Jack && this.suit.color() == trump.color() ) {
      rawRank = 6;
    } else {
      retVal = 0;
    }
        
    //System.out.println( "...returning " + retVal + " << " + rawRank + " = " + (retVal << rawRank));
    return retVal << rawRank;
  }
  
  public String toString() { return this.value.toString() + this.suit.toString(); }

  public boolean isTrump( CardSuit trump )
  {
    return  this.suit == trump || 
            (this.value == CardVal.Jack && 
             this.suit.color() == trump.color());
  }

  public static Card lookupCard( CardSuit suit, CardVal value )
  {
    return CARD_INDEX.get( suit ).get( value );
  }
  
}
