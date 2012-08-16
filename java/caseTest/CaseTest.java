public class CaseTest
{
  public static void main( String [] args )
  {
    try
    {
      int input = Integer.parseInt( args[0] );

      switch( input )
      {
        case 0:
        {
          System.out.print( "Zero, " );
        }

        case 1:
        {
          System.out.println( "One" );
          break;
        }
 
        case 2:
        {
          System.out.println( "Two" );
          break;
        }

        default:
        {
          System.out.println( "Out of range!!" );
          break;
        }
      }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
    }
  }

}
