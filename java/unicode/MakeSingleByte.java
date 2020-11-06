import java.util.Map;
import java.util.HashMap;

public class MakeSingleByte {

  public static final String SINGLES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

  public static void main( String [] args ) {
    String input = args[0];

    Map<Character,Integer> charMap = new HashMap<Character,Integer>();

    System.out.println( "Num chars: " + input.length() );
    System.out.println( "Num bytes: " + input.getBytes().length );

    Character ch = null;
    int codePoint = 0;
    Integer singlesIdx = 0;
    int counter = 0;
    StringBuilder singleByteStr = new StringBuilder();
    
    for( int c = 0; c < input.length(); c++ ) {
      ch = Character.valueOf( input.charAt(c) );
      codePoint = Character.codePointAt( input, c );

      if( codePoint >= 128 ) {
        if( (singlesIdx = charMap.get(ch)) == null ) {
           singlesIdx = counter;
          charMap.put( ch, counter++ );
        }
        singleByteStr.append( SINGLES.charAt( singlesIdx ) );
      } else {
        singleByteStr.append( ch );
      }
    }

    String newStr = singleByteStr.toString();
    System.out.println( newStr );
    System.out.println( "Num chars: " + newStr.length() );
    System.out.println( "Num bytes: " + newStr.getBytes().length );
  }
}
