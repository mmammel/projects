package org.mjm.euchre.game;

import java.util.ArrayList;
import java.util.List;

import org.mjm.euchre.card.Card;
import org.mjm.euchre.card.CardGroup;
import org.mjm.euchre.card.CardSuit;
import org.mjm.euchre.card.CardVal;

public class PlayFactory {
  private PlayFactory() { }
  private static PlayFactory theFactory = new PlayFactory();
  
  public static PlayFactory getInstance() {
    return theFactory;
  }
  
  public List<Play> getPlays( Game g, Play previous ) {
    List<Play> retVal = new ArrayList<Play>();
    
    switch( previous.getPlayType() ) {
      case Round1Pass:
        retVal = this.handleRound1Pass(g, previous);
        break;
      case Round1Order:
        retVal = this.handleRound1Order(g, previous);
        break;
      case Discard:
        retVal = this.handleLead(g, 0);
        break;
      case Round2Call:
        retVal = this.handleLead(g, 0);
        break;
      case Round2Pass:
        retVal = this.handleRound2Pass(g, previous);
        break;
      case PlayTrickCard:
        retVal = this.handleNormalPlay(g, previous);
        break;
      case TrickWon:
        retVal = this.handleLead(g, previous.getPlayer());
        break;
      case GameWon:
        break;
    }
    
    return retVal;
  }
  
  /**
   * The previous play was a round 1 pass:
   *   - if the dealer turned it down, produce round 2 pass or call plays
   *   - if a non-dealer passed, should I order up or pass?
   * @param g
   * @param previous
   * @return
   */
  private List<Play> handleRound1Pass( Game g, Play previous ) {
    List<Play> retVal = null;
    if( previous.getPlayer() == 3 ) {
      retVal = this.passOrCall(g, 0);
    } else {
      retVal = this.passOrOrder(g, previous.getPlayer() + 1);
    }
  
    return retVal;
  }
  
  /**
   * Based on the strength of my hand, should I pass or order up the turn?
   * @param g
   * @param player
   * @return
   */
  private List<Play> passOrOrder( Game g, int player ) {
    List<Play> retVal = new ArrayList<Play>();
    
    // Everyone can pass
    retVal.add(new Play( player, PlayType.Round1Pass) );
    
    // see if we can afford to order it
    Hand h = g.getPlayerHand(player);
    if( h.getTrumpScore(g.getTurnCard().suit()) > 60 ) {
      retVal.add(new Play( player, PlayType.Round1Order, g.getTurnCard()));
    }
    
    return retVal;
  }
  
  /**
   * 
   * @param g
   * @param previous
   * @return
   */
  private List<Play> handleRound2Pass( Game g, Play previous ) {
    List<Play> retVal = null;
    if( previous.getPlayer() == 3 ) {
      throw new RuntimeException( "Dealer is screwed - they can't pass!!");
    } else {
      retVal = this.passOrCall(g, previous.getPlayer() + 1);
    }
    
    return retVal;
  }
  
  /**
   * 
   * @param g
   * @param player
   * @return
   */
  private List<Play> passOrCall( Game g, int player ) {
    List<Play> retVal = new ArrayList<Play>();
    // can *always* pass, unless you're the dealer.
    if( player != 3 ) {
      retVal.add( new Play(player, PlayType.Round2Pass ) );
    }
    
    // Now see if we should call it anything
    Hand h = g.getPlayerHand(player);
    Card turn = g.getTurnCard();
    for( CardSuit suit : CardSuit.values() ) {
      // Can't call the suit the turn card was.
      if( suit != turn.suit() ) {
        // The dealer has to call it something, doesn't get to discriminate
        if( h.getTrumpScore(suit) > 60 || player == 3 ) {
          retVal.add( new Play( player, PlayType.Round2Call, suit) );
        }
      }
    }
    
    return retVal;
  }
  
  /**
   * 
   * @param g
   * @param previous
   * @return
   */
  private List<Play> handleRound1Order( Game g, Play previous ) {
    List<Play> retVal = new ArrayList<Play>();
    // previous play will contain the turn card.  Determine what we should discard as dealer.
    Hand h = g.getPlayerHand(3);
    Card pickup = previous.getCardPlayed();
    
    Card [] orderedCards = h.getTrumpOrder(pickup.suit());
    
    // always discard the lowest card, after that check for trump;
    retVal.add( new Play( 3, PlayType.Discard, orderedCards[0]));
    
    for( int i = 1; i < orderedCards.length; i++ ) {
      if( orderedCards[i].suit() != pickup.suit() ) {
        retVal.add( new Play( 3, PlayType.Discard, orderedCards[i]));
      }
    }
    
    return retVal;
  }
  
  private List<Play> handleLead( Game g, int player ) {
    List<Play> retVal = new ArrayList<Play>();
    
    CardSuit trump = g.getTrump();
    // boolean calledIt = g.didPlayerCallTrump(0);
    // I can lead anything!
    // TODO: add some common sense lead rules:
    //   don't lead low trump...and probably others.
    Hand h = g.getPlayerHand(player);
    Card [] ranked = h.getTrumpOrder(trump);
    Card c = null;

    for( int i = ranked.length - 1; i >= 0; i--) {
      c = ranked[i];
      if( (c.suit() == trump && c.value() == CardVal.Jack) || c.suit(trump) != trump || i == 0 ) {
        retVal.add(new Play(player, PlayType.PlayTrickCard, c, c.suit(trump)));
      }
    }
    
    return retVal;
  }
  
  /**
   * Normal game play, follow suit, play off, or trump;
   * @param g
   * @param previous
   * @return
   */
  private List<Play> handleNormalPlay( Game g, Play previous ) {
    List<Play> retVal = new ArrayList<Play>();
    CardSuit suitLed = previous.getSuitPlayed();
    CardSuit trump = g.getTrump();
    int player = previous.getPlayer() == 3 ? 0 : previous.getPlayer() + 1;
    Hand h = g.getPlayerHand(player);
    Card [] trumpRank = h.getTrumpOrder(trump);
    if( h.getTrumpSuitCount(trump).get(suitLed) > 0 ) {
      // we have the suit led, generate plays of those.
      for( Card c : trumpRank ) {
        if( c.suit(trump) == suitLed ) {
          retVal.add( new Play( player, PlayType.PlayTrickCard, c, suitLed ) );
        }
      }
    } else {
      // we don't have what's led, just play everything.
      for( Card c : trumpRank ) {
        retVal.add( new Play( player, PlayType.PlayTrickCard, c, suitLed ) );
      }
    }
    
    return retVal;
  }

}
