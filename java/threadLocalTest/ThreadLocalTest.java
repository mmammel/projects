public class ThreadLocalTest
{
  private static ThreadLocal<String> threadString = new ThreadLocal<String>();

  public static String getThreadString()
  {
    String retVal = threadString.get();
    threadString.set(null);
    return retVal;
  }

  public static void setThreadString( String s )
  {
    threadString.set(s);
  }


  public static void main( String [] args )
  {
    for( int i = 0; i < 99; i++ )
    {
      ThreadLocalTest.setThreadString("Jomama" + i);
      System.out.println( ThreadLocalTest.getThreadString() );
    }

  }
}

