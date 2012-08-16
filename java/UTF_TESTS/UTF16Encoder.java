import java.io.BufferedReader;
import java.io.InputStreamReader;

public class UTF16Encoder
{
  public static long UTF16_MAX = 0x10FFFFL;
  public static long UTF16_LARGE = 0xFFFFL;
  public static long HIGH_SURROGATE = 0xD800L;
  public static long LOW_SURROGATE  = 0xDC00L;

  /**
   * Returns the long value of a valid UTF-16 code, or -1L
   * @param in String
   * @return long
   */
  private long getLongFromCode( String code )
  {
    long val = 0L;

    if( code == null || (!code.startsWith( "U+") && !code.startsWith("u+")) )
    {
      val = -1L;
    }
    else
    {
        try
        {
            val = Long.parseLong(code.substring(2), 16);
            if (val > UTF16_MAX || val <= 0L )
            {
                val = -1L;
            }
        }
        catch( NumberFormatException nfe )
        {
            val = -1L;
        }
    }

    return val;
  }

  public String encode( String val )
  {
      long value = 0L;
      long hv = 0L;
      long lv = 0L;
      int len = 0;
      String retVal = null;

      if( (value = getLongFromCode( val )) != -1L )
      {
        if( value <= UTF16_LARGE )
        {
           retVal = Long.toHexString(value).toUpperCase();
           len = retVal.length();
           if( len < 4 )
           {
             for( int i = 0; i < (4 - len); i++ )
             {
               retVal = "0" + retVal;
             }
           }

           retVal = "0x" + retVal;
        }
        else
        {
            hv = (value - 0x10000L) >> 10;
            hv |= HIGH_SURROGATE;
            lv = (value & 0x3FF) | LOW_SURROGATE;
            retVal = "0x" + Long.toHexString(hv).toUpperCase() + " 0x" + Long.toHexString(lv).toUpperCase();
        }
      }
      else
      {
          retVal = "Invalid UTF-16 Code: " + val;
      }

      return retVal;
  }

  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    UTF16Encoder encoder = null;
    String inputStr = null;

    try
    {
      encoder = new UTF16Encoder();
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );
      System.out.println( "Welcome to mjm's UTF16 encoder.  Enter a UCS code in the range: ");
      System.out.println( "\n   U+0001 - U+10FFFF\n");
      System.out.println(" and press <return> to see the encoded byte stream.\n");
      System.out.print("utf16> ");

      while( (inputStr = input_reader.readLine()) != null )
      {
          if( !inputStr.equalsIgnoreCase("quit") )
          {
              System.out.println("\nEncoded data(s): " + encoder.encode(inputStr));
              System.out.print( "\nutf16> " );
          }
          else
          {
              break;
          }
      }

      System.out.println( "Goodbye." );
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }

  }

}
