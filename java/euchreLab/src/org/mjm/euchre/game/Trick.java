package org.mjm.euchre.game;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.mjm.euchre.card.CardGroup;
import org.mjm.euchre.card.Card;
import org.mjm.euchre.card.CardSuit;

/**
 * Card group of size 4, it represents a trick that is played.
 * The player's cards played are always in the same spot of the array.
 * @author mmammel
 *
 */
public class Trick extends CardGroup {

  private Card led = null;
  private Map<Card,Integer> playedByMap = new EnumMap<Card,Integer>(Card.class);
  private Map<Integer,Card> playedMap = new HashMap<Integer,Card>();
  
  public Trick() {
  }

  public Card getLed() {
    return led;
  }

  public void setLed(Card led) {
    this.led = led;
  }
  
  /**
   * Player wants to know if his team already has it
   * @param player
   * @return
   */
  public boolean doWeHaveIt( int player ) {
    boolean retVal = false;
    int teammate = Team.getPartner(player);
    Card highest = this.getHighestCard();
    if( highest != null ) {
      retVal = this.playedByMap.get( highest ) == teammate;
    }
    
    return retVal;
  }
  
  /**
   * What did my partner play?
   * @param player
   * @return
   */
  public Card myPartnerPlayed( int player ) {
    return this.playedMap.get(Team.getPartner(player));
  }
  
  /**
   * Find out what card a player played.
   * @param player
   * @return
   */
  public Card getCardPlayedByPlayer( int player ) {
    return this.playedMap.get(player);
  }
  
  /**
   * Find out who played a card
   * @param c
   * @return
   */
  public Integer getPlayerThatPlayedCard( Card c ) {
    return this.playedByMap.get(c);
  }
  
  /**
   * Test to see if a card would be the highest card in the trick.
   * @param c
   * @return
   */
  public boolean wouldWin( Card c ) {
    return c.getCardScore(this.trump) > this.getHighestCard().getCardScore(this.trump);
  }
  
  public void playCard( int player, Card c ) {
    if( this.led == null ) {
      this.led = c;
    }
    
    if( this.playedMap.containsKey(player) ) {
      this.unplayCard(player);
    }
    
    this.playedMap.put(player, c);
    this.playedByMap.put(c, player);
    this.addCard(c);
  }
  
  public Card unplayCard( int player ) {
    Card c = this.playedMap.get(player);
    
    if( c != null ) {
      this.removeCard(c);
      this.playedMap.remove(player);
      this.playedByMap.remove(c);
    }
    
    if( this.getNumCards() == 0 ) {
      this.led = null;
    }
    
    return c;
  }
  
  public int getWinner(CardSuit trump) {
    int retVal = -1; // no winner.
    Card winner = null;
    if( this.getNumCards() == 4 ) {
      winner = this.getTrickScoreOrder(trump, this.led.suit(trump))[3];
      retVal = this.playedByMap.get(winner);
    }
    
    return retVal;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    Card c = null;
    for( int i = 0; i < 4; i++ ) {
      c = this.getCardPlayedByPlayer(i);
      sb.append("[").append(c == null ? "-" : (c == this.led ? ""+c+"*" : c)).append("]");
    }
    
    return sb.toString();
  }
}
