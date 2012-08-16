import java.io.BufferedReader;
import java.io.InputStreamReader;

public class UTFEncoder
{
  private UTF8Encoder utf8encoder = new UTF8Encoder();
  private UTF16Encoder utf16encoder = new UTF16Encoder();

  public String getUTF8( String code )
  {
    return utf8encoder.encode(code);
  }

  public String getUTF16( String code )
  {
    return utf16encoder.encode(code);
  }

  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    UTFEncoder encoder = null;
    String inputStr = null;

    try
    {
      encoder = new UTFEncoder();
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );
      System.out.println( "Welcome to mjm's UTF encoder.  Enter a UCS code in the range: ");
      System.out.println( "\n   U+0001 - U+7FFFFFFF, UTF-16 maximum: 0x10FFFF\n");
      System.out.println(" and press <return> to see the UTF-8 and UTF-16 encoded byte streams.\n");
      System.out.print("utf> ");

      while( (inputStr = input_reader.readLine()) != null )
      {
        if( !inputStr.equalsIgnoreCase("quit") )
        {
          System.out.println("\nUTF-8 : " + encoder.getUTF8(inputStr));
          System.out.println("UTF-16: " + encoder.getUTF16(inputStr));
          System.out.print( "\nutf> " );
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