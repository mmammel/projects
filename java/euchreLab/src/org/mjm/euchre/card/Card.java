package org.mjm.euchre.card;

import java.util.Map;
import java.util.HashMap;

public enum Card {
  AceOfDiamonds(CardSuit.Diamonds,CardVal.Ace),
  KingOfDiamonds(CardSuit.Diamonds,CardVal.King),
  QueenOfDiamonds(CardSuit.Diamonds,CardVal.Queen),
  JackOfDiamonds(CardSuit.Diamonds,CardVal.Jack),
  TenOfDiamonds(CardSuit.Diamonds,CardVal.Ten),
  NineOfDiamonds(CardSuit.Diamonds,CardVal.Nine),
  AceOfHearts(CardSuit.Hearts,CardVal.Ace),
  KingOfHearts(CardSuit.Hearts,CardVal.King),
  QueenOfHearts(CardSuit.Hearts,CardVal.Queen),
  JackOfHearts(CardSuit.Hearts,CardVal.Jack),
  TenOfHearts(CardSuit.Hearts,CardVal.Ten),
  NineOfHearts(CardSuit.Hearts,CardVal.Nine),
  AceOfSpades(CardSuit.Spades,CardVal.Ace),
  KingOfSpades(CardSuit.Spades,CardVal.King),
  QueenOfSpades(CardSuit.Spades,CardVal.Queen),
  JackOfSpades(CardSuit.Spades,CardVal.Jack),
  TenOfSpades(CardSuit.Spades,CardVal.Ten),
  NineOfSpades(CardSuit.Spades,CardVal.Nine),
  AceOfClubs(CardSuit.Clubs,CardVal.Ace),
  KingOfClubs(CardSuit.Clubs,CardVal.King),
  QueenOfClubs(CardSuit.Clubs,CardVal.Queen),
  JackOfClubs(CardSuit.Clubs,CardVal.Jack),
  TenOfClubs(CardSuit.Clubs,CardVal.Ten),
  NineOfClubs(CardSuit.Clubs,CardVal.Nine);

  private final CardSuit suit;
  private final CardVal value;

  public CardSuit suit() { return this.suit; }
  public CardSuit suit( CardSuit trump ) { return this.isTrump(trump) ? trump : this.suit; }
  public CardVal  value() { return this.value; }

  private static final Card [] DIAMONDS_TRUMP_RANK = {
    JackOfDiamonds,
    JackOfHearts,
    AceOfDiamonds,
    KingOfDiamonds,
    QueenOfDiamonds,
    TenOfDiamonds,
    NineOfDiamonds
  };

  private static final Card [] HEARTS_TRUMP_RANK = {
    JackOfHearts,
    JackOfDiamonds,
    AceOfHearts,
    KingOfHearts,
    QueenOfHearts,
    TenOfHearts,
    NineOfHearts
  };

  private static final Card [] SPADES_TRUMP_RANK = {
    JackOfSpades,
    JackOfClubs,
    AceOfSpades,
    KingOfSpades,
    QueenOfSpades,
    TenOfSpades,
    NineOfSpades
  };

  private static final Card [] CLUBS_TRUMP_RANK = {
    JackOfClubs,
    JackOfSpades,
    AceOfClubs,
    KingOfClubs,
    QueenOfClubs,
    TenOfClubs,
    NineOfClubs
  };

  private static final Card [] DIAMONDS_LEAD_RANK = {
    AceOfDiamonds,
    KingOfDiamonds,
    QueenOfDiamonds,
    JackOfDiamonds,
    TenOfDiamonds,
    NineOfDiamonds
  };

  private static final Card [] HEARTS_LEAD_RANK = {
    AceOfHearts,
    KingOfHearts,
    QueenOfHearts,
    JackOfHearts,
    TenOfHearts,
    NineOfHearts
  };

  private static final Card [] SPADES_LEAD_RANK = {
    AceOfSpades,
    KingOfSpades,
    QueenOfSpades,
    JackOfSpades,
    TenOfSpades,
    NineOfSpades
  };


  private static final Card [] CLUBS_LEAD_RANK = {
    AceOfClubs,
    KingOfClubs,
    QueenOfClubs,
    JackOfClubs,
    TenOfClubs,
    NineOfClubs
  };

  private static Map<CardSuit,Card []> TRUMP_RANKS = new HashMap<CardSuit,Card []>();
  private static Map<CardSuit,Card []> LEAD_RANKS = new HashMap<CardSuit,Card []>();
  private static Map<CardSuit,Map<CardVal,Card>> CARD_INDEX = new HashMap<CardSuit,Map<CardVal,Card>>();

  static {
    TRUMP_RANKS.put( CardSuit.Diamonds, DIAMONDS_TRUMP_RANK );
    TRUMP_RANKS.put( CardSuit.Hearts, HEARTS_TRUMP_RANK );
    TRUMP_RANKS.put( CardSuit.Spades, SPADES_TRUMP_RANK );
    TRUMP_RANKS.put( CardSuit.Clubs, CLUBS_TRUMP_RANK );
    LEAD_RANKS.put( CardSuit.Diamonds, DIAMONDS_LEAD_RANK );
    LEAD_RANKS.put( CardSuit.Hearts, HEARTS_LEAD_RANK );
    LEAD_RANKS.put( CardSuit.Spades, SPADES_LEAD_RANK );
    LEAD_RANKS.put( CardSuit.Clubs, CLUBS_LEAD_RANK );

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

  public boolean isTrump( CardSuit trump )
  {
    return  this.suit == trump || 
            (this.value == CardVal.Jack && 
             this.suit.color() == trump.color());
  }

  /* Even if led is the same as trump, this will work. */
  public static Card [] getRankArray( CardSuit trump, CardSuit led )
  {
    Card [] retVal = new Card [ 13 ];
    Card [] trumpRanks = TRUMP_RANKS.get( trump );
    Card [] leadRanks = LEAD_RANKS.get( led );
    
    int retIdx = 0;
    for( Card t : trumpRanks )
    {
      retVal[retIdx++] = t;
    }

    for( Card l : leadRanks )
    {
      retVal[retIdx++] = l;
    }

    return retVal;
  }

  public static Card lookupCard( CardSuit suit, CardVal value )
  {
    return CARD_INDEX.get( suit ).get( value );
  }
  
}
