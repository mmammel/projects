public class Underscore {
  static String KEY_PATTERN = "^.*?_(.*?)_[^_]+$";
  static String REALKEY_PATTERN = "^(\\D+?)\\d+$";
  public static void main( String [] args )
  {
    String key = args[0].replaceAll( KEY_PATTERN, "$1");
    System.out.println( "Key: " + key );
    System.out.println( "Real Key: " + key.replaceAll( REALKEY_PATTERN, "$1") );
    
  }
}
