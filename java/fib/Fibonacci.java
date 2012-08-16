import java.util.*;

public class Fibonacci
{

  public void fib( int limit )
  {
    List results = new ArrayList();
    int current = 1;
    int prev = 1;
    int temp = 0;
    results.add(""+prev);
    results.add(""+current);
    
    while( (current + prev) < limit )
    {
      temp = current;
      current = current + prev;
      prev = temp;
      results.add(""+current);
    }

    for( Iterator iter = results.iterator(); iter.hasNext(); )
    {
      System.out.print( iter.next() + " " );
    }
    System.out.print("\n");
    
  }

  public static void main( String [] args )
  {
    Fibonacci F = new Fibonacci();

    if( args.length != 1 )
    {
      System.out.println( "Usage: java Fibonacci <limit>" );
    }
    else
    {
      try
      {
        F.fib( Integer.parseInt( args[0] ) );        
      }
      catch( Exception e )
      {
        System.out.println( e.toString() );
        e.printStackTrace();
      }
    }
  }

}
