public class LetterShift
{
  public String shift( String str, int shift )
  {
    int tempChar = 0;
    int adjustShift = 0;
    char [] array = str.toCharArray();
    String retVal = null;

    adjustShift = shift % 26;

    for( int i = 0; i < array.length; i++ )
    {
      if( array[i] >= 65 && array[i] <= 90 )
      {
        tempChar = (int)array[i] + adjustShift;

        if( tempChar > 90 )
        {
          tempChar = 64 + tempChar - 90;
        }

        array[i] = (char)tempChar;
      }
      else if( array[i] >= 97 && array[i] <= 122 )
      {
        tempChar = (int)array[i] + adjustShift;

        if( tempChar > 122 )
        {
          tempChar = 96 + tempChar - 122;
        }

        array[i] = (char)tempChar;
      }
    }

    retVal = new String( array );

    return retVal;
    
  }

  public static void main( String [] args )
  {
    try
    { 
      String arg = args[0];
      LetterShift LS = new LetterShift();
      for( int i = 0; i < 27; i++ )
      {
        System.out.println( arg + ": " + LS.shift( arg, i ) );
      }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }

}
