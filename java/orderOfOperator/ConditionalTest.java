public class ConditionalTest
{
  public static void main( String [] args )
  {
    if( true && true || false )
    {
      System.out.println( "T and T or F -> T" );
    }

    if( true && false || true )
    {
      System.out.println( "T and F or T -> T" );
    }

    if( true && true && false )
    {
      System.out.println( "T and T and F -> T" );
    }

    if( false && true || true )
    {
      System.out.println( "F and T or T -> T" );
    }

    if( false && true || false )
    {
      System.out.println( "F and T or F -> T" );
    }

    if( false || true && false )
    {
      System.out.println( "F or T and F -> T" );
    }
  }

}
