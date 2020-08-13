import java.util.List;
import java.util.ArrayList;

public class EscapeTest {
  public static void main( String [] args ) {
    List<String> strs = new ArrayList<String>();
    strs.add( "Who's" );
    strs.add( "Who\'s" );
    strs.add( "Who\\'s" );
    strs.add( "Who\u0027s" );
    strs.add( "Who'\bs" );

    for( String s : strs ) {
      System.out.println( s.replaceAll("'","''") );
    }

    System.out.println( "Byte view: " );

    for( String s : strs ) {
      System.out.print( s.replaceAll("'","''") + " -> " );
      for( byte b : s.replaceAll("'","''").toString().getBytes() ) {
        System.out.print("[0x" + Integer.toHexString(b) + "]");
      }
      System.out.println("");
    }
  }
}
