public class LockTest
{
  public void doSomething()
  {
    Object lockObj = Other.getLockObject();

    if( ((Other)lockObj).getCount() % 2 == 0 )
    {
      System.out.println( "Using AndAnother" );
      lockObj = AndAnother.getLockObject();
    }
    else
    {
      System.out.println( "Using Other" );
    }

    synchronized( lockObj )
    {
      System.out.println( "Doing something" );
    }

  }

  public static void main( String [] args )
  {
    LockTest LT = new LockTest();

    for( int i = 0; i < 10; i++ )
    {
      LT.doSomething();
    }
  }

}
