public class TestDriver
{
  public int determineWinner( Card c1, Card c2, Card c3, Card c4, CardSuit trump, CardSuit led )
  {
    int retVal = -1;
    Card [] rankArray = Card.getRankArray( trump, led );

    for( int i = 0; i < rankArray.length; i++ )
    {
      if( c1 == rankArray[i] ) retVal = 1;
      else if( c2 == rankArray[i] ) retVal = 2;
      else if( c3 == rankArray[i] ) retVal = 3;
      else if( c4 == rankArray[i] ) retVal = 4;

      if( retVal > 0 ) break;
    }

    return retVal;
  }

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

    if( args.length != 6 )
    {
      throw new RuntimeException( "Usage: TestDriver c1 c2 c3 c4 trump led" );
    }

    System.out.println( TD.determineWinner( 
      getCardFromCode(args[0]),
      getCardFromCode(args[1]),
      getCardFromCode(args[2]),
      getCardFromCode(args[3]),
      getSuitFromCode(args[4]),
      getSuitFromCode(args[5])
    ) );
  }
}
