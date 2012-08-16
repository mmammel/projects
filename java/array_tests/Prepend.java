public class Prepend
{
  public String buildStringTest( long count )
  {
    StringBuffer vals = new StringBuffer();

    int tempCount = (count == 0L) ? 1 : (int)count;
    for( int j = 2; j >= 0 && tempCount >= 1; j-- )
    {
      vals.insert( 0, "<val>" + j + "</val>" );
      tempCount--;
    }

    for( long i = 0L; i < (count - 3); i++ )
    {
      vals.insert( 0, "<val />" );
    }
    return vals.toString();
  }

  public static void main( String [] args )
  {
    if( args.length != 1 )
    {
      System.out.println( "Usage: java Prepend <count>" );
    }
    else
    {
      try
      {
        Prepend pp = new Prepend();
        long count = Long.parseLong( args[0] );
        System.out.println( "Result: \n" + pp.buildStringTest(count) );
        
      }
      catch( Exception e )
      {
        System.out.println( "Caught and exception: " + e.toString() );
      }
    }
  }


}
