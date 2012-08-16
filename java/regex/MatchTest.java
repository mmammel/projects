public class MatchTest
{
  public static void main( String [] args )
  {
    String curly = "\\$\\{(.*?)\\}";
    System.out.println( args[0].replaceAll( curly, "$1" ) );
  }
}
