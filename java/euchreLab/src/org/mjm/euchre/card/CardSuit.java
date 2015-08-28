package org.mjm.euchre.card;

public enum CardSuit
{
  Diamonds(CardColor.Red, "\u2662"),
  Hearts(CardColor.Red, "\u2661"),
  Spades(CardColor.Black, "\u2660"),
  Clubs(CardColor.Black, "\u2663");

  private final CardColor color;
  private final String str;
  
  public CardColor color() { return this.color; }

  CardSuit( CardColor color, String str )
  {
    this.color = color;
    this.str = str;
  }
  
  public static CardSuit fromChar( Character c ) {
    CardSuit retVal = null;
    switch( c ) {
      case '\u2662':
        retVal = Diamonds;
        break;
      case '\u2661':
        retVal = Hearts;
        break;
      case '\u2660':
        retVal = Spades;
        break;
      case '\u2663':
        retVal = Clubs;
        break;
      default:
        break;
    }
    
    return retVal;
  }

  public String toString() { return this.str; }
}
