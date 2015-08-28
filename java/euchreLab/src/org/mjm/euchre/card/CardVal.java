package org.mjm.euchre.card;

public enum CardVal {
  Nine("9"),
  Ten("10"),
  Jack("J"),
  Queen("Q"),
  King("K"),
  Ace("A");

  private final String str;

  CardVal( String str ) {
    this.str = str;
  }

  public static CardVal fromString( String str ) {
    CardVal retVal = null;
    
    if( str.equals("9") ) {
      retVal = Nine;
    } else if( str.equals("10") ) {
      retVal = Ten;
    } else if( str.equals("J") ) {
      retVal = Jack;
    } else if( str.equals("Q") ) {
      retVal = Queen;
    } else if( str.equals("K") ) {
      retVal = King;
    } else if( str.equals("A") ) {
      retVal = Ace;
    }
    
    return retVal;
  }
  public String toString() { return this.str; }
}
