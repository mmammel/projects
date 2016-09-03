import java.security.MessageDigest;

public class SHAHashString {

  private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

  public static void main( String [] args ) {
    System.out.println( SHAHashString.getSHAHashString( args[0] ) );
  }

  public static String getSHAHashString( String input ) {
    String retVal = null;
    MessageDigest sha = null;
    
    try {
      sha = MessageDigest.getInstance("SHA");
      sha.update(input.getBytes());
      retVal = bytesToHex( sha.digest() );
    }
    catch( Exception e ) {
      retVal = "";
    }
    
    return retVal.toLowerCase();
  }

  public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for ( int j = 0; j < bytes.length; j++ ) {
        int v = bytes[j] & 0xFF;
        hexChars[j * 2] = HEX_ARRAY[v >>> 4];
        hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
    }
    return new String(hexChars).toLowerCase();
  }
}
