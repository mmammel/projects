package org.mjm.euchre.game;

import org.mjm.euchre.card.*;

public class Kitty extends CardGroup {
  
  private Card turnCard = null;
  
  public Kitty( Card [] cards )
  {
    super(cards);
    this.turnCard = cards[cards.length - 1];
  }

  public Card getTurnCard()
  {
    return this.turnCard;
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
