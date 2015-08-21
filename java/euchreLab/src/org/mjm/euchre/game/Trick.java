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
  
  public Trick() {
    super(4);
    // TODO Auto-generated constructor stub
  }

  public Card getLed() {
    return led;
  }

  public void setLed(Card led) {
    this.led = led;
  }
  
  @Override
  public void setCard( int i, Card c ) {
    super.setCard(i,c);
    if( this.led == null ) {
      this.led = c;
    }
  }
  
  @Override
  public boolean addCard( Card c ) {
    throw new UnsupportedOperationException("Must use setCard to add a card to the trick.");
  }

}
