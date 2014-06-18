public class MatchesTest
{
  public static void main( String [] args )
  {
    //System.out.println( args[0].matches("(?i).*?Loss.*") ? "Match!" : "No Match!" );
    //System.out.println( args[0].matches("^(?:(?:[-a-zA-Z0-9!#$%&'*+/=?^_`{|}~])+\\.)*[-a-zA-Z0-9!#$%&'*+/=?^_`{|}~]+@\\w(?:(?:-|\\w)*\\w)*\\.(?:\\w(?:(?:-|\\w)*\\w)*\\.)*\\w{2,4}$") ? "Match!" : "No Match!" );
    System.out.println( args[0].matches("^(?:[-0-9A-z_() ]+,[0-9A-z ]*,?)+$") ? "Match!" : "No Match!" );
  }
}
