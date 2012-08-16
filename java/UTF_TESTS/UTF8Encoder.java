import java.io.BufferedReader;
import java.io.InputStreamReader;

public class UTF8Encoder
{
  public static final long B1_MAX = 0x7FL;
  public static final long B2_MAX = 0x7FFL;
  public static final long B3_MAX = 0xFFFFL;
  public static final long B4_MAX = 0x1FFFFFL;
  public static final long B5_MAX = 0x3FFFFFFL;
  public static final long B6_MAX = 0x7FFFFFFFL;

  public static final long B2_MASK = 0xC0L;
  public static final long B3_MASK = 0xE0L;
  public static final long B4_MASK = 0xF0L;
  public static final long B5_MASK = 0xF8L;
  public static final long B6_MASK = 0xFCL;

  public static final long B2_CHECK_MASK = -1L ^ 0x1FL;
  public static final long B3_CHECK_MASK = -1L ^ 0xFL;
  public static final long B4_CHECK_MASK = -1L ^ 0x7L;
  public static final long B5_CHECK_MASK = -1L ^ 0x2L;
  public static final long B6_CHECK_MASK = -1L ^ 0x1L;

  public static final long BN_MASK = 0x80L;
  public static final long BX_MASK = 0x3FL;

  public static final long [] MASKS = { B2_MASK, B3_MASK, B4_MASK, B5_MASK, B6_MASK };
  public static final long [] CHECK_MASKS = { B2_CHECK_MASK, B3_CHECK_MASK, B4_CHECK_MASK, B5_CHECK_MASK, B6_CHECK_MASK };


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
        if (val > B6_MAX || val <= 0L )
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


  public String encode( String code )
  {
    String retVal = "";
    int byteCount = 0;
    long value = this.getLongFromCode( code );
    long tempVal = 0L;

    if( value > -1L )
    {
      if( value <= B1_MAX )
      {
        retVal = "0x" + Long.toHexString(value).toUpperCase();
      }
      else
      {
        tempVal = value & BX_MASK;
        tempVal |= BN_MASK;
        retVal = " 0x" + Long.toHexString( tempVal ).toUpperCase() + retVal;
        value >>= 6;

        while( (value & CHECK_MASKS[byteCount]) != 0 )
        {
          tempVal = value & BX_MASK;
          tempVal |= BN_MASK;
          retVal = " 0x" + Long.toHexString( tempVal ).toUpperCase() + retVal;
          value >>= 6;
          byteCount++;
        }

        tempVal = value | MASKS[byteCount];
        retVal = "0x" + Long.toHexString( tempVal ).toUpperCase() + retVal;
      }

    }
    else
    {
      retVal = "Invalid UTF-8 code: " + code;
    }

    return retVal;
  }

  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    UTF8Encoder encoder = null;
    String inputStr = null;

    try
    {
      encoder = new UTF8Encoder();
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );
      System.out.println( "Welcome to mjm's UTF8 encoder.  Enter a UCS code in the range: ");
      System.out.println( "\n   U+0001 - U+7FFFFFFF\n");
      System.out.println(" and press <return> to see the encoded byte stream.\n");
      System.out.print("utf8> ");

      while( (inputStr = input_reader.readLine()) != null )
      {
        if( !inputStr.equalsIgnoreCase("quit") )
        {
          System.out.println("\nEncoded byte(s): " + encoder.encode(inputStr));
          System.out.print( "\nutf8> " );
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
