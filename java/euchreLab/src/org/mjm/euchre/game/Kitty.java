package org.mjm.euchre.game;

import org.mjm.euchre.card.*;

public class Kitty extends CardGroup {
  
  private Card turnCard = null;
  private Card [] initOrder = null;
  
  public Kitty( Card [] cards )
  {
    super(cards);
    this.initOrder = cards;
    this.turnCard = cards[cards.length - 1];
  }

  public Card getTurnCard()
  {
    return this.turnCard;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    for( Card c : this.initOrder ) {
      sb.append("[").append(c).append("]");
    }
    return sb.toString();
  }
  
  public Card discard( Card discard )
  {
    Card retVal = this.turnCard;
    this.removeCard(this.turnCard);
    this.turnCard = discard;
    this.addCard(this.turnCard);
    return retVal; 
  }
}
