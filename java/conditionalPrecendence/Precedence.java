public class Precedence
{
  public static void main( String [] args )
  {
    if( true || false && false )
    {
      System.out.println( "true || false && false => true" );
    }
    else
    {
      System.out.println( "true || false && false => false" );
    }
  }

}
