import java.util.Arrays;

public class SubSequence
{
  public static void main( String [] args )
  {
    SubSequence LCS = new SubSequence();
    LCS.findLongestCommonSubSequence( args[0], args[1] );
  }

  /**
               { "" if i = 0 or j = 0
    LCS(i,j) = { LCS(i-1,j-1) + t[i] if t[i] = p[j]
               { MAX( LCS(i-1,j), LCS(i,j-1) ) if t[i] != p[j]


   */
  public void findLongestCommonSubSequence( String t, String p )
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
        if( m == 0 || n == 0 )
        {
          results[m][n] = "";
        }
        else if( t.charAt(m-1) == p.charAt(n-1) )
        {
          results[m][n] = results[m-1][n-1] + new String( ""+t.charAt(m-1) );
        }
        else if( results[m-1][n].length() >= results[m][n-1].length() )
        {
          results[m][n] = results[m-1][n];
        }
        else
        {
          results[m][n] = results[m][n-1];
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
