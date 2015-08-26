package org.mjm.euchre.card;

import java.util.Comparator;
import java.util.Map;

public class TrumpCardComparator implements Comparator<Card>  {
  private CardSuit trump;
  private Map<CardSuit, Integer> suitCounts = null;
  
  public TrumpCardComparator( CardSuit trump, Map<CardSuit, Integer> suitCounts ) {
    this.trump = trump;
    this.suitCounts = suitCounts;
  }
  
  @Override
  public int compare(Card arg0, Card arg1) {
    int retVal = 0;
    //System.out.println( "Comparing: " + arg0 + " to " + arg1 + ", " + this.suitCounts );
   
    if( arg0.isTrump(this.trump) && arg1.isTrump(this.trump) ) {
      retVal = arg0.getCardScore(this.trump) - arg1.getCardScore(this.trump);
    } else if( arg0.isTrump(this.trump) ) {
      retVal = 1;      
    } else if( arg1.isTrump(this.trump) ) {
      retVal = -1;
    } else {
      if( (retVal = arg0.suit().compareTo(arg1.suit())) == 0 ) {
        retVal = arg0.compareTo(arg1);
      } else if((retVal = this.suitCounts.get(arg0.suit()).compareTo(this.suitCounts.get(arg1.suit()))) == 0 && 
                 this.suitCounts.get(arg0.suit()) == 1) {
        retVal = arg0.value().compareTo(arg1.value());       
      }
    }
    
    return retVal;
  }
}
