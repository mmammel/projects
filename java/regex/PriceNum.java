public class PriceNum {
  public static void main( String [] args ) {
    String input = args[0];

    if( input.matches( "^[0-9]+-[0-9]+$" ) ) {
      System.out.println( "Matches!" );
      String [] minMax = input.split("-");
      System.out.println( "Min: " + minMax[0] + ", Max: " + minMax[1] );
    } else {
      System.out.println( "No match" );
    }
  }
}
