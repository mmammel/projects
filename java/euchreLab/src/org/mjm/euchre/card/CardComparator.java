package org.mjm.euchre.card;

import java.util.Comparator;
import java.util.Map;

public class CardComparator implements Comparator<Card> {

  private Map<CardSuit, Integer> suitCounts = null;
  
  public CardComparator( Map<CardSuit, Integer> suitCounts ) {
    this.suitCounts = suitCounts;
  }
  
  @Override
  public int compare(Card arg0, Card arg1) {
    int retVal = 0;
    //System.out.println( "Comparing: " + arg0 + " to " + arg1 + ", " + this.suitCounts );
    
    if( (retVal = arg0.suit().compareTo(arg1.suit())) == 0 ) {
      retVal = arg0.compareTo(arg1);
    } else {
      retVal = this.suitCounts.get(arg0.suit()).compareTo(this.suitCounts.get(arg1.suit()));
    }
    
    return retVal;
  }

}
