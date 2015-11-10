public class FancySubstring {
  public static void main( String [] args ) { 
    System.out.println( args[0].replaceAll( "^(https?://).*$", "$1" ) );
  }
}
