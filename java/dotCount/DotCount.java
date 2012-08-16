import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Date;

public class DotCount
{
  public static void main( String [] args )
  {
    boolean useNew = true;
    String toCount = null;
    String actualString = null;
    long start = 0L;
    long finish = 0L;

    if( args.length > 2 || args.length == 0 )
    {
      System.out.println( "Usage: DotCount [old|new*] <string>" );
    }
    else if( args.length == 2 )
    {
      useNew = !args[0].toLowerCase().equals( "old" );
      toCount = args[1];
    }
    else
    {
      toCount = args[0];
    }

    try
    {
      actualString = new BufferedReader( new FileReader( toCount ) ).readLine();
    }
    catch( IOException ioe )
    {
      System.out.println( "Caught and Exception: " + ioe.toString() );
    }

    start = new Date().getTime();
    if( useNew )
      System.out.println( DotCount.countDots( actualString ) + " dots.");
    else
      System.out.println( DotCount.oldCountDots( actualString ) + " dots.");
    finish = new Date().getTime();

    System.out.println( "Took " + (finish - start) + "ms" );
  }

  public static int countDots( String str )
  {
    System.out.println( "Running new count" );
    int idx = 0;
    int count = 0;
    while( true )
    {
       if( (idx = str.indexOf( '.', idx )) != -1 )
       {
         count++;
         idx++;
       }
       else break;
    }

    return count;
  }

  public static int oldCountDots( String str )
  {
    System.out.println( "Running old count" );
    int retVal = 0;

    for(int i=0;i<str.length();i++) {
      if(str.charAt(i)=='.')
          retVal++;
    }

    return retVal;
  }
}

