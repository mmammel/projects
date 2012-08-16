public class Palindrome
{

  public void longestPalindrome( String p )
  {
    StringBuilder [][] results = new StringBuilder[p.length()][];
    for( int k = 0; k < p.length(); k++ )
    {
      results[k] = new StringBuilder[p.length()];
    }

    for( int ii = 0; ii < p.length(); ii++ )
    {
      for( int jj = 0; jj < p.length(); jj++ )
      {
        results[ii][jj] = new StringBuilder();
      }
    }

    for( int i = p.length() - 1; i >= 0; i-- )
    {
      for( int j = 0; j < p.length(); j++ )
      {
        if( i == j )
        {
          results[i][j] = new StringBuilder(""+p.charAt(i));
        }
        else if( i < j )
        {
          if(  p.charAt(i) != p.charAt(j) )
          {
            if( results[i+1][j].length() >= results[i][j-1].length() )
            {
              results[i][j] = new StringBuilder( results[i+1][j].toString() );
            }
            else
            {
              results[i][j] = new StringBuilder( results[i][j-1].toString() );
            }
          }
          else
          {
            results[i][j] = new StringBuilder( "" + p.charAt(i) + results[i+1][j-1].toString() + p.charAt(j) );
          }
        }
      }
    }

    System.out.println( "Longest = " + results[0][p.length() - 1] );
  }

  public static void main( String [] args )
  {
    Palindrome P = new Palindrome();

    P.longestPalindrome( args[0] );
  }
}
