import java.security.MessageDigest;

public class GenerateHash {

  public static void main( String [] args ) {
    try {
    String seed = "foobar";
    if( args.length > 0 ) seed = args[0];
    MessageDigest md5 = MessageDigest.getInstance("MD5");
    MessageDigest sha = MessageDigest.getInstance("SHA");
    md5.update(seed.getBytes());
    sha.update(seed.getBytes());
    System.out.println( "md5: " + bytesToHex( md5.digest() ) );
    System.out.println( "sha: " + bytesToHex( sha.digest() ) );
    } catch( Exception e ) {
     System.out.println( "Exception!!: " + e.toString() );
    }
  }
final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for ( int j = 0; j < bytes.length; j++ ) {
        int v = bytes[j] & 0xFF;
        hexChars[j * 2] = hexArray[v >>> 4];
        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars).toLowerCase();
}
}
