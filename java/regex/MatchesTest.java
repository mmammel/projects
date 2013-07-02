public class MatchesTest
{
  public static void main( String [] args )
  {
    System.out.println( args[0].matches("(?i).*?Loss.*") ? "Match!" : "No Match!" );
  }
}
