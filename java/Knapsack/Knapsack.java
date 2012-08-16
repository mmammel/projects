import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;

public class Knapsack
{
  /*
    // items is an array of items with value and weight attributes.
    // weight is the maximum available weight.
    // returns the optimal value
    let results = new array[0..items.length][0..weight] // assume initializes to zero
    let item_track = new array[0..items.length][0..weight] // assume initializes to zero
    for i = 1..items.length
      for j = 1..weight
        if items[i].weight <= j
          if( items[i].value + results[i-1][j-items[i].weight] > results[i-1][j] )
            results[i][j] = items[i].value + results[i-1][j-items[i].weight]
            item_track[i][j] = i
          else
            results[i][j] = results[i-1][j]
            item_track[i][j] = item_track[i-1][j]
        else
          results[i][j] = results [i-1][j]
          item_track[i][j] = item_track[i-1][j]

    return results[items.length,weight]
*/
  public void findOptimal( List<Item> items, int maxweight )
  {
    int [][] results = new int[items.size()+1][];
    for( int k = 0; k <= items.size(); k++ ) {
      results[k] = new int[maxweight+1];
      Arrays.fill( results[k], 0 );
    }

    int [][] track = new int[items.size()+1][];
    for( int r = 0; r <= items.size(); r++ ) {
      track[r] = new int[maxweight+1];
      Arrays.fill( track[r], -1 );
    }

    Item tempItem = null;
    for( int i = 1; i <= items.size(); i++ )
    {
      tempItem = items.get(i-1);
      for( int j = 1; j <= maxweight; j++ )
      {
        if( tempItem.weight <= j )
        {
          if( tempItem.value + results[i-1][j-tempItem.weight] > results[i-1][j] )
          {
            results[i][j] = tempItem.value + results[i-1][j-tempItem.weight];
            track[i][j] = i;
          }
          else
          {
            results[i][j] = results[i-1][j];
          }
        }
        else
        {
          results[i][j] = results[i-1][j];
        }
      }
    }

    System.out.println( results[items.size()][maxweight] );

    Formatter formatter = new Formatter( System.out );
    for( int m = 0; m < results.length; m++ ) {
      for( int n = 0; n < results[m].length; n++ ) {
        formatter.format( "%4d",results[m][n]);
      }
      System.out.println( "" );
    }

    for( int p = 1; p < track.length; p++ ) {
      for( int q = 1; q < track[p].length; q++ ) {
        //formatter.format( "%3d",track[p][q]);
        System.out.print( track[p][q] );
      }
      System.out.println( "" );
    }

    this.printSolution( items, maxweight, track );
  }

  private void printSolution( List<Item> items, int maxweight, int [][] s )
  {
    int idx = items.size();
    while( maxweight > 0 && idx > 0 )
    {
      if( s[idx][maxweight] != -1 )
      {
        System.out.println( items.get(s[idx][maxweight] - 1) );
        maxweight = maxweight - items.get(s[idx][maxweight] - 1).weight;
      }

      idx--;
    }
  }

  public static void main( String [] args )
  {
    int maxweight = Integer.parseInt( args[0] );
    List<Item> items = new ArrayList<Item>();
    String [] temp = null;
    for( int i = 1; i < args.length; i++ )
    {
      //weight-value
      temp = args[i].split("-");
      items.add( new Item( Integer.parseInt(temp[0]),Integer.parseInt(temp[1]) ) );
    }

    Knapsack K = new Knapsack();
    K.findOptimal( items, maxweight );
  }

}