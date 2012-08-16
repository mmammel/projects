import java.util.Arrays;

public class SuperSequence
{
  public static void main( String [] args )
  {
    SuperSequence SCS = new SuperSequence();
    SCS.findShortestSuperSequence( args[0], args[1] );
  }

  public void findShortestSuperSequence( String t, String p )
  {
    String [][] results = new String[ t.length() + 1 ][];
    for( int i = 0; i <= t.length(); i++ )
    {
      results[i] = new String[p.length() + 1];
      Arrays.fill( results[i], "" );
    }

    String tempStr = null;

    for( int m = 0; m <= t.length(); m++ )
    {
      for( int n = 0; n <= p.length(); n++ )
      {
        if( m == 0 && n == 0 )
        {
          results[m][n] = "";
        }
        else if( m == 0 )
        {
          results[m][n] = p.substring(0,n);
        }
        else if( n == 0 )
        {
          results[m][n] = t.substring(0,m);
        }
        else if( t.charAt(m-1) == p.charAt(n-1) )
        {
          results[m][n] = results[m-1][n-1] + new String( ""+t.charAt(m-1) );
        }
        else if( results[m-1][n].length() <= results[m][n-1].length() )
        {
          results[m][n] = results[m-1][n] + new String(""+t.charAt(m-1));
        }
        else
        {
          results[m][n] = results[m][n-1] + new String(""+p.charAt(n-1));
        }
      }
    }

    for( int c = 0; c <= t.length(); c++ )
    {
      for( int d = 0; d <= p.length(); d++ )
      {
        System.out.print( "[" + results[c][d] + "]" );
      }

      System.out.println("");
    }

    System.out.println( "Got: " + results[t.length()][p.length()] );
  }
}
