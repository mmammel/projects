package org.mjm.euchre.game;

import org.mjm.euchre.card.*;

public class Kitty extends CardGroup {
  public Kitty( Card [] cards )
  {
    super(cards);
  }

  public Card getTurnCard()
  {
    return cards[3];
  }

  public Card discard( Card discard )
  {
    Card retVal = cards[3];
    cards[3] = discard;
    return retVal; 
  }
}
