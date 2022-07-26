import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;

public class HmacSHA265Hash {
  public static void main( String [] args ) {
    String key = args[0];
    String payload = args[1];

    HmacSHA265Hash H = new HmacSHA265Hash();

    System.out.println( "Hashing: " + payload + "..." );
    System.out.println( "...got: " + H.hashPayload( key, payload ) );
  }

  private String hashPayload( String key, String payload ) {
    String retVal = null;
    if(key != null && payload  != null) {
      //Initialize algorithm
      try {
        final Mac hMacSHA256 = Mac.getInstance("HmacSHA256");
      //Generate Key
        byte[] keyBytes = key.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        final SecretKey signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");
      //Associate key with Mac instance
        hMacSHA256.init(signingKey);
      //Generate Hash from message
        byte[] dataBytes = payload.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        byte[] res = hMacSHA256.doFinal(dataBytes);
        retVal = new String(Hex.encodeHex(res));
      } catch( Exception e ) {
        System.out.println( "Error: " + e.toString() );
        retVal = payload;
      }
    }
    
    return retVal;
  }
}
