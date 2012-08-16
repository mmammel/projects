
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import java.util.StringTokenizer;

public class SetGame implements TupleProcessor
{
  private static final int ATTR_MASK       = 0x7;

  private static final int RED             = 0x1;
  private static final char REDC           = 'R';
  private static final int GREEN           = 0x2;
  private static final char GREENC         = 'G';
  private static final int PURPLE          = 0x4;
  private static final char PURPLEC        = 'P';

  private static final int LIGHT           = 0x8;
  private static final char LIGHTC         = 'L';
  private static final int MEDIUM          = 0x10;
  private static final char MEDIUMC        = 'M';
  private static final int FILLED          = 0x20;
  private static final char FILLEDC        = 'F';

  private static final int ONE             = 0x40;
  private static final char ONEC           = '1';
  private static final int TWO             = 0x80;
  private static final char TWOC           = '2';
  private static final int THREE           = 0x100;
  private static final char THREEC         = '3';

  private static final int OVAL            = 0x200;
  private static final char OVALC          = 'O';
  private static final int SQUIGGLE        = 0x400;
  private static final char SQUIGGLEC      = 'S';
  private static final int DIAMOND         = 0x800;
  private static final char DIAMONDC       = 'D';

  private static final String CARD_SEP     = ":";

  private Map mCardMap                     = null;
  NTuple mTupleHandler                     = null;

  public SetGame()
  {
    mCardMap = new HashMap();
    mTupleHandler = new NTuple();
    mTupleHandler.setProcessor( this );
  }

  public boolean isSet( int first, int second, int third )
  {
    int result;
    int temp, tempCheck = 0;
    int [] attrs = new int [4];
    boolean isset = true;

    result = first | second | third;

    for( int i = 0; i < attrs.length; i++ )
    {
      tempCheck = 0;
      temp = attrs[i] = result & ATTR_MASK;
      result >>= 3;
      for( int j = 0; j < 3; j++ )
      {
        tempCheck += temp & 1;
        temp >>= 1;
      }

      if( (tempCheck & 1) == 0 )
      {
        isset = false;
        break;
      }
    }

    return isset;
  }

  private int readCard( String card )
      throws SetGameException
  {
    char tempChar = 0;
    int cardInt   = 0;
    int colorMask = 0x1;
    int shapeMask = 0x2;
    int shadeMask = 0x4;
    int numberMask = 0x8;
    int validTest = 0xf;
    int validator = 0;

    if( card.length() != 4 )
    {
      throw new SetGameException( "Invalid Card representation: " + card );
    }

    for( int i = 0; i < card.length(); i++ )
    {
      tempChar = card.charAt( i );

      switch( tempChar )
      {
        case REDC:
          cardInt |= RED;
          validator |= colorMask;
          break;
        case GREENC:
          cardInt |= GREEN;
          validator |= colorMask;
          break;
        case PURPLEC:
          cardInt |= PURPLE;
          validator |= colorMask;
          break;
        case ONEC:
          cardInt |= ONE;
          validator |= numberMask;
          break;
        case TWOC:
          cardInt |= TWO;
          validator |= numberMask;
          break;
        case THREEC:
          cardInt |= THREE;
          validator |= numberMask;
          break;
        case LIGHTC:
          cardInt |= LIGHT;
          validator |= shadeMask;
          break;
        case MEDIUMC:
          cardInt |= MEDIUM;
          validator |= shadeMask;
          break;
        case FILLEDC:
          cardInt |= FILLED;
          validator |= shadeMask;
          break;
        case OVALC:
          cardInt |= OVAL;
          validator |= shapeMask;
          break;
        case SQUIGGLEC:
          cardInt |= SQUIGGLE;
          validator |= shapeMask;
          break;
        case DIAMONDC:
          cardInt |= DIAMOND;
          validator |= shapeMask;
          break;
        default:
          throw new SetGameException("Invalid character: " + tempChar );
      }
    }

    if( validator != validTest )
    {
      throw new SetGameException( "Invalid card representation: " + card );
    }

    return cardInt;
  }

  public void clearAndFillCards( String cardSet )
      throws SetGameException
  {
    mCardMap.clear();
    StringTokenizer strtok = null;
    String tempTok = null;

    strtok = new StringTokenizer( cardSet, CARD_SEP );

    while( strtok.hasMoreTokens() )
    {
      tempTok = strtok.nextToken();
      mCardMap.put( tempTok, new Integer( this.readCard( tempTok ) ) );
    }

  }

  public void findSets()
     throws Exception
  {
    int first  = 0;
    int second = 0;
    int third  = 0;
    Set cards = null;
    String [] cardArray = null;

    cards = mCardMap.keySet();
    cardArray = new String [ cards.size() ];
    cards.toArray( cardArray );

    if( cardArray.length >= 3 )
    {
      mTupleHandler.processTuples( cardArray, 3 );
    }
    else
    {
      System.out.println( "Need 3 cards to play" );
    }

  }

  public void processTuple( Object [] array )
  {
    if( this.isSet( ((Integer)mCardMap.get( array[ 0 ] )).intValue(),
                    ((Integer)mCardMap.get( array[ 1 ] )).intValue(),
                    ((Integer)mCardMap.get( array[ 2 ] )).intValue() ) )
    {
      System.out.println( "Set Found!!\nCards:\n" +
                          array[ 0 ] + "\n" +
                          array[ 1 ] + "\n" +
                          array[ 2 ] );
    }
  }

  public class SetGameException extends Exception
  {
    public SetGameException( String msg )
    {
      super(msg);
    }

    public SetGameException( String msg, Exception e )
    {
      super( msg + "\nUnderlying Cause: \n" + e.toString() );
    }
  }

  public static void main( String[] args )
  {
    SetGame SG = null;

    try
    {
      if( args.length != 1 )
      {
        System.out.println( "Need a cardSet string argument" );
      }
      else
      {
        SG = new SetGame();
        SG.clearAndFillCards( args[ 0 ] );
        SG.findSets();
      }
    }
    catch( Exception e )
    {
      e.printStackTrace();
      System.out.println( e.toString() );
    }
  }

}
