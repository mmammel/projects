
public class ReplaceNewLine {
  public static void main( String [] args ) {
    String testString = "Hello,\nmy name is\nMax.\r\nWhat is yours?";
    System.out.println("Before replace: \n" + testString );
    testString = testString.replaceAll( "[\r\n]+", " ");
    System.out.println("After replace: \n" + testString );
  }
}
