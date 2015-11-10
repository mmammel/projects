import java.io.BufferedReader;
import java.io.FileReader;
import org.apache.commons.codec.binary.Base64;

public class CompressAndEncode
{
  public static void main( String [] args )
  {
    String fname = args[0];
    BufferedReader input_reader = null;
    String input_str = "";
    StringBuilder fileContent = new StringBuilder();

    try
    {
      input_reader = new BufferedReader( new FileReader ( fname ) );

      while( (input_str = input_reader.readLine()) != null )
      {
        fileContent.append( input_str ).append( "\n" );
      }

      // Now we have the file content.
      byte [] compressed = CompressionUtil.compress( fileContent.toString().getBytes() );
      String result = new String( Base64.encodeBase64(compressed)  );
      System.out.println( result );
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

