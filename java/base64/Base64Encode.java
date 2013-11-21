import sun.misc.BASE64Encoder;

public class Base64Encode {
  public static void main( String [] args ) {
    String str = null;
    String enc = null;

    if( args.length == 2 ) {
      enc = args[1];
    }

    str = args[0];

    BASE64Encoder b64 = new BASE64Encoder();
    byte [] toEncode = null;
    try {
      if( enc == null ) {
        toEncode = str.getBytes();
      } else {
        toEncode = str.getBytes( enc );
      }
    }
    catch( Exception e ) {
      System.out.println( "Exception: " + e.toString() );
    }

    System.out.println( b64.encode( toEncode ) );
  }

}
