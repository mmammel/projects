public class MatchesTest
{
  public static void main( String [] args )
  {
    System.out.println( args[0].matches("^[A-Za-z_0-9]+%2[Cc].*$") ? "Match!" : "No Match!" );
  }
}
