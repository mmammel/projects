package org.mjm.euchre.game;

import java.util.ArrayList;
import java.util.List;

import org.mjm.euchre.card.Card;
import org.mjm.euchre.card.CardSuit;

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
        break;
      case Discard:
        break;
      case Round2Call:
        break;
      case Round2Pass:
        retVal = this.handleRound2Pass(g, previous);
        break;
      case LeadTrickCard:
        break;
      case PlayTrickCard:
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
  
  private List<Play> passOrOrder( Game g, int player ) {
    List<Play> retVal = new ArrayList<Play>();
    
    // Everyone can pass
    retVal.add(new Play( player, PlayType.Round1Pass) );
    
    // see if we can afford to order it
    Hand h = g.getPlayerHand(player);
    if( h.getTrumpScore(g.getTurnCard().suit()) > 2 ) {
      retVal.add(new Play( player, PlayType.Round1Order, g.getTurnCard()));
    }
    
    return retVal;
  }
  
  private List<Play> handleRound2Pass( Game g, Play previous ) {
    List<Play> retVal = null;
    if( previous.getPlayer() == 3 ) {
      throw new RuntimeException( "Dealer is screwed - they can't pass!!");
    } else {
      retVal = this.passOrCall(g, previous.getPlayer() + 1);
    }
    
    return retVal;
  }
  
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
        if( h.getTrumpScore(suit) > 2 || player == 3 ) {
          retVal.add( new Play( player, PlayType.Round2Call, suit) );
        }
      }
    }
    
    return retVal;
  }
  
  private List<Play> handleRound1Order( Game g, Play previous ) {
    List<Play> retVal = new ArrayList<Play>();
    // previous play will contain the turn card.  Determine what we should discard as dealer.
    Hand h = g.getPlayerHand(3);
    Card discard = null;
    Card pickup = previous.getCardPlayed();
    for( Card c : h.getCards() ) {
      
    }
    
    return retVal;
  }
}
