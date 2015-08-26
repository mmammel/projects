package org.mjm.euchre;

import org.mjm.euchre.game.*;
import org.mjm.euchre.card.*;

public class TestDriver
{

  public static Card getCardFromCode( String code )
  {
    if( !code.matches( "^[DHSC][AKQJTN]$" ) )
    {
      throw new RuntimeException( "Invalid code: " + code );
    }

    CardSuit suit = CardSuit.Diamonds;
    CardVal value = CardVal.Ace; 

    switch( code.charAt(0) )
    {
      case 'D':
        suit = CardSuit.Diamonds;
        break;
      case 'H':
        suit = CardSuit.Hearts;
        break;
      case 'S':
        suit = CardSuit.Spades;
        break;
      case 'C':
        suit = CardSuit.Clubs;
        break;
      default:
        suit = CardSuit.Diamonds;
        break;
    }

    switch( code.charAt(1) )
    {
      case 'A':
        value = CardVal.Ace;
        break;
      case 'K':
        value = CardVal.King;
        break;
      case 'Q':
        value = CardVal.Queen;
        break;
      case 'J':
        value = CardVal.Jack;
        break;
      case 'T':
        value = CardVal.Ten;
        break;
      case 'N':
        value = CardVal.Nine;
        break;
      default:
        value = CardVal.Ace;
        break;
    }

    return Card.lookupCard( suit, value );
  }

  public static CardSuit getSuitFromCode( String code )
  {
    if( !code.matches("^[DHSC]$") )
    {
      throw new RuntimeException( "Invalid suit code: " + code );
    }
  
    CardSuit suit = CardSuit.Diamonds;

    switch( code.charAt(0) )
    {
      case 'D':
        suit = CardSuit.Diamonds;
        break;
      case 'H':
        suit = CardSuit.Hearts;
        break;
      case 'S':
        suit = CardSuit.Spades;
        break;
      case 'C':
        suit = CardSuit.Clubs;
        break;
      default: 
        suit = CardSuit.Diamonds;
        break;
    }

    return suit;
  } 

  public static void main( String [] args )
  {
    TestDriver TD = new TestDriver();
  }
}
