public class DelimTest
{
  public static final String OUTER_DELIM = "\\^\\^";
  public static final String INNER_DELIM = "\\|\\|";

  public static void main( String [] args )
  {
    String [] array = args[0].split( OUTER_DELIM );
    int count = 0;
    for( String str : array )
    {
      System.out.println( "Splits " + count++ + ":"  );
      String [] inner = str.split( INNER_DELIM );
      for( String str2 : inner )
      {
        System.out.println( "  " + str2 );
      }
    }
  }

}
