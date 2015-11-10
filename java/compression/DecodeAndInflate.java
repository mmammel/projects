import java.io.BufferedReader;
import java.io.FileReader;
import org.apache.commons.codec.binary.Base64;

public class DecodeAndInflate
{
  public static void main( String [] args )
  {
    String fname = args[0];
    BufferedReader input_reader = null;
    String input_str = "";
    StringBuilder fileContent = new StringBuilder();
    int rowCount = 0;

    try
    {
      input_reader = new BufferedReader( new FileReader ( fname ) );

      while( (input_str = input_reader.readLine()) != null )
      {
        // fileContent.append( input_str ).append("\n");
        // Now we have the file content.
        //byte [] decoded =  Base64.decodeBase64(fileContent.toString().getBytes());
        byte [] decoded =  Base64.decodeBase64(input_str.getBytes());
        byte [] decompressed = CompressionUtil.decompress(decoded );
        System.out.println( "Row: " + ++rowCount );
        System.out.println( new String( decompressed ) );
      }

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

