package org.mjm.euchre.game;

import org.mjm.euchre.card.CardGroup;
import org.mjm.euchre.card.Card;

/**
 * Card group of size 4, it represents a trick that is played.
 * The player's cards played are always in the same spot of the array.
 * @author mmammel
 *
 */
public class Trick extends CardGroup {

  private Card led = null;
  private Card [] playedByArray = new Card[4];
  
  public Trick() {
  }

  public Card getLed() {
    return led;
  }

  public void setLed(Card led) {
    this.led = led;
  }
  
  public void playCard( int player, Card c ) {
    if( this.led == null ) {
      this.led = c;
    }
    
    if( this.playedByArray[player] != null ) {
      this.unplayCard(player);
    }
    
    this.playedByArray[player] = c;
    this.addCard(c);
    
  }
  
  public Card unplayCard( int player ) {
    Card c = this.playedByArray[player];
    
    if( c != null ) {
      this.removeCard(c);
      this.playedByArray[player] = null;
    }
    
    return c;
  }
  
  @Override
  public boolean addCard( Card c ) {
    throw new UnsupportedOperationException("Must use playCard to add a card to the trick.");
  }

}
