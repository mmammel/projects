public class SubString {
  public static void main( String [] args ) {
    System.out.println( args[0].substring(0,5) );

    System.out.println( args[0].substring(0, args[0].indexOf(".com") + 4 ));
  }
}
