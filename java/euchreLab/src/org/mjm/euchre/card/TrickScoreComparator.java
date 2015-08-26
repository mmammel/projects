package org.mjm.euchre.card;

import java.util.Comparator;

public class TrickScoreComparator implements Comparator<Card>{
  private CardSuit trump;
  private CardSuit led;
  
  public TrickScoreComparator( CardSuit trump, CardSuit led ) {
    this.trump = trump;
    this.led = led;
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
    } else if( arg0.suit() == this.led && arg1.suit() == this.led ){
      retVal = arg0.compareTo(arg1);
    } else if( arg0.suit() == this.led ) {
      retVal = 1;
    } else if( arg1.suit() == this.led ) {
      retVal = -1;
    } else {
      retVal = arg0.compareTo(arg1);
    }
  
    return retVal;
  }
}
