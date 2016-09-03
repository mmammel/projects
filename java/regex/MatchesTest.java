public class MatchesTest
{
  public static void main( String [] args )
  {
    //System.out.println( args[0].matches("(?i).*?Loss.*") ? "Match!" : "No Match!" );
    //System.out.println( args[0].matches("^(?:(?:[-a-zA-Z0-9!#$%&'*+/=?^_`{|}~])+\\.)*[-a-zA-Z0-9!#$%&'*+/=?^_`{|}~]+@\\w(?:(?:-|\\w)*\\w)*\\.(?:\\w(?:(?:-|\\w)*\\w)*\\.)*\\w{2,4}$") ? "Match!" : "No Match!" );
    //System.out.println( args[0].matches("^(?:[-0-9A-z_() ]+,[0-9A-z ]*,?)+$") ? "Match!" : "No Match!" );

    //String matchStr = "foo|b\\(a\\)r|jo mama|123\\|456";
    //matchStr = "(?i)" +  matchStr.replaceAll(" ","\\\\s+");
    //String matchStr = "^BULLHORN_CANDIDATE\\.[0-9]+?\\.[0-9]+?\\.[0-9]+$";
    String matchStr = "^WITH\\s+(?:.*?AS\\s+\\(.*?\\),?)+";
    
    System.out.println( "Match Pattern: " + matchStr );

    System.out.println( args[0].matches(matchStr) ? "Match!" : "No Match!" );
    System.out.println( args[0].replaceFirst(matchStr,"") );
    //System.out.println( args[0].matches("^(?:[-0-9A-z_() ]+,[0-9A-z ]*,?)+$") ? "Match!" : "No Match!" );
  }
}
