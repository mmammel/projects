public class Alphabet {

  public static void main( String [] args ) {
    String input = args[0];

    for( int i = 0; i < input.length(); i++ ) {
      for( int j = i+1; j < input.length(); j++ ) {
        System.out.println( ""+input.charAt(i)+input.charAt(j) );
      }
    }
  }
}
