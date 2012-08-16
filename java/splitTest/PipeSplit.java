public class PipeSplit
{
  public static void main( String [] args )
  {
    String [] splits = args[0].split("\\|");
    for( int i = 0; i < splits.length; i++ )
    {
      System.out.println( splits[i] );
    }
  }
}
