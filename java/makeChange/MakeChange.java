import java.util.Arrays;

public class MakeChange
{

  // safe to assume coins has a penny
  public int [] makeChange( int val, int [] coins )
  {
    //first index will hold total count.
    int [] retVal = new int [coins.length + 1];
    int [][] temp = new int [coins.length][];
    Arrays.fill(retVal,0);
    Arrays.sort( coins );

    for( int i = 0; i < coins.length; i++ )
    {
      if( val >= coins[i] )
      {
        temp[i] = makeChange( val - coins[i], coins );
      }
    }

    int idx = -1;
    int least = Integer.MAX_VALUE;
    for( int j = 0; j < coins.length; j++ )
    {
      if( temp[j] != null && least > temp[j][0] )
      {
        least = temp[j][0];
        idx = j;
      }
    }

    if( idx >= 0 )
    {
      this.merge( retVal, temp[idx] );
      retVal[0]++;
      retVal[idx+1]++;
    }

    return retVal;

  }

  private void merge( int [] result, int [] from )
  {
    for( int i = 0; i < result.length; i++ )
    {
      result[i] += from[i];
    }
  }


  public void makeChangeFast( int val, int [] coins )
  {
    Arrays.sort(coins);
    int [] S = new int [val+1];
    int [] C = new int [val+1];
    Arrays.fill(S,-1);
    Arrays.fill(C,0);
    int least = -1;
    int coin = 0;

    for( int i = 1; i <= val; i++ )
    {
      least = Integer.MAX_VALUE;
      for( int k = 0; k < coins.length; k++ )
      {
        if( i >= coins[k] )
        {
          if( 1 + C[i - coins[k]] < least )
          {
            least = 1 + C[i - coins[k]];
            coin = coins[k];
          }
        }
      }

      C[i] = least;
      S[i] = coin;
    }

    //StringBuilder a = new StringBuilder("[ "), b = new StringBuilder("[ ");
    //for( int m = 1; m < C.length; m++ )
    //{
    //  a.append( C[m] ).append(" ");
    //  b.append( S[m] ).append(" ");
    //}
    //a.append("]");
    //b.append("]");

    //System.out.println( a );
    //System.out.println( b );
    this.printResult( val, S );
  }

  public void printResult( int val, int [] coins )
  {
    while( val > 0 )
    {
      System.out.print( "" + coins[val] + " " );
      val = val - coins[val];
    }
    System.out.println("");
  }

  public static void main( String [] args )
  {
    int val = Integer.parseInt( args[0] );
    int [] coins = new int [ args.length - 1 ];

    for( int i = 0; i < coins.length; i++ )
    {
      coins[i] = Integer.parseInt( args[i+1] );
    }

    MakeChange MC = new MakeChange();
/*
    int [] result = MC.makeChange( val, coins );

    System.out.print("\n" + result[0] + " : [ " );
    for( int j = 1; j < result.length; j++ )
    {
      System.out.print(""+result[j] + " ");
    }
    System.out.print("]\n");
*/
    MC.makeChangeFast( val, coins );

  }

}
