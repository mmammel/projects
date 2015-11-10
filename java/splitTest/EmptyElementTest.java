public class EmptyElementTest {
  public static void main( String [] args ) {
    String foo = "foo=";
    String [] splits = foo.split("=");

    for( String split : splits ) {
      System.out.println( "[" + split + "]" );
    }
  }
}
