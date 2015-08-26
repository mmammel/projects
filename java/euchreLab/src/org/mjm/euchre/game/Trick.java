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
  
  public void playCard( int player, Card c ) {
    Card tempCard = null;
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
}
