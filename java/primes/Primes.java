import java.io.*;

public class Primes
{
  public boolean isPrime( double n )
  {
    boolean retVal = false;

    if( n < 2L )
    {
      retVal = false;
    }
    else if( n == 2L || n == 3L )
    {
      retVal = true;
    }
    else if( n%2L == 0 )
    {
      retVal = false;
    }
    else
    {
      double sr = new Double( Math.sqrt( n ) ).doubleValue();
     
      retVal = true;
 
      for( double l = 3; l <= sr; l +=2 )
      {
        if( n%l == 0 )
        {
          retVal = false;
          break;
        }
      }
    }
   
    return retVal; 
  }

  public static void main( String [] args )
  {
    String tempStr = null;
    double num = 0L;
    Primes P = new Primes();

    try
    {
      BufferedReader input = new BufferedReader( new InputStreamReader( System.in ) );

      System.out.print( "Enter a number: " );
      while( (tempStr = input.readLine()) != null )
      {
        num = Double.parseDouble( tempStr.trim() );

        if( P.isPrime( num ) )
        {
          System.out.println( "" + num + " is Prime." );
        }
        else
        {
          System.out.println( "" + num + " is not Prime." );
        }

        System.out.print( "Enter a number: " );
      }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
    }
  }

}
