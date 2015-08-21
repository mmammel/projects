package org.mjm.euchre.game;

import org.mjm.euchre.card.*;

public class Hand extends CardGroup {
  public Hand( Card [] cards )
  {
    super(cards);
  }

  public int getTrumpScore( CardSuit trump ) {
    int retVal = 0;
    for( Card c : cards ) {
      retVal += c.getCardScore( trump, trump );
    }

    return retVal;
  }
  
}
