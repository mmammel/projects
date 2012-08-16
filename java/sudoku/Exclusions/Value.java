public enum Value
{
  ONE,
  TWO,
  THREE,
  FOUR,
  FIVE,
  SIX,
  SEVEN,
  EIGHT,
  NINE;
  
  public static Value fromInt( int n )
  {
    Value retVal = ONE;
    switch( n )
    {
      case 1:
        retVal = ONE;
        break;
      case 2:
        retVal = TWO;
        break;
      case 3:
        retVal = THREE;
        break;
      case 4:
        retVal = FOUR;
        break;
      case 5:
        retVal = FIVE;
        break;
      case 6:
        retVal = SIX;
        break;
      case 7:
        retVal = SEVEN;
        break;
      case 8:
        retVal = EIGHT;
        break;
      case 9:
        retVal = NINE;
        break;
      default:
        throw new IllegalArgumentException( "Value must be 1-9" );
      }
      
      return retVal;        
    }
}

