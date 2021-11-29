import java.util.SortedSet;
import java.util.TreeSet;

class Solution {
    public int findKthNumber(int m, int n, int k) {

SortedSet<RowWalker> walkers = new TreeSet<RowWalker>();

      int temp = 0;

      if( m > n ) {
        temp = m;
        m = n;
        n = temp;
      }

      /*
         1  2  3  4  5  6  7
         x  4  6  8 10 12 14
         x  x  9 12 15 18 21
         x  x  x 16 20 24 28
         x  x  x  x 25 30 35
         x  x  x  x  x 36 42
         x  x  x  x  x  x 49
       */

      // lets see if we can pre-count a huge swath of cells
      int startRow = 0, endRow = 0, nextRow = 0, startCol = 0, currVal = 0, count = 0, idx = 0;
      
      // find the square on the diagonal that will contain 
      startRow = Double.valueOf(
                  Math.min(Integer.valueOf(m).doubleValue(), 
                           n - Math.floor(Math.sqrt(n*n - k)))
                  ).intValue();

      endRow = Double.valueOf(
        Math.ceil(Math.sqrt((double)k))
      ).intValue();

      // count off the initial swath of cells we can get rid of.
      //System.out.println( "Chose start row: " + startRow + ", count: " + count );

      startRow = refineStartRow( startRow, m, n, k );
      nextRow = startRow + 1;
      currVal = startRow * startRow;
      count = startRow * startRow - 1;

      //System.out.println( "Refined start row: " + startRow + ", count: " + count );

      // add the initial walker
      walkers.add(new RowWalker(startRow, startRow, m, n) );

      // create row walkers for every row going up from here until:
      //   1. we are out of rows OR
      //   2. the start col is off the end.
      // as we add walkers, count off the cells we are excluding
      for( int i = startRow - 1; i >= 1; i-- ) {
        if( (startCol = getStartColumnForRow(i, startRow )) <= n ) {
          walkers.add( new RowWalker(i, startCol, m, n));
          count += (startCol - startRow - 1);
          if( m >= startCol ) {
            count += (startCol - startRow - 1 );
          } else if( m > startRow ) {
            count += (m - startRow);  
          }
          //System.out.println( "Added a walker for row "+ i +", count now (" + count + ")");
        } else {
          count += ((n - startRow) * i);
          if( m > startRow ) {
            count += ((m - startRow) * i);
          }
          //System.out.println( "Hit the last column, count now (" + count + ")");
          break;
        }
      }

      // Create all of the walkers below, until we have included enough
      // to cover k
      // idx = startRow + 1;
      // while( idx <= endRow && idx <= m ) {
      //   ////System.out.println( "Adding row " + idx );
      //   walkers.add( new RowWalker(idx, idx, m, n) );
      //   idx++;
      // }

      //System.out.println( "We have pre-excluded " +count+ " values, start walking");
      // Ok we have all of our walkers.  Just let them walk until we are done
      RowWalker w = null;
      while( count < k ) {
        w = walkers.first();
        //System.out.println( "Walking: " + w );
        walkers.remove(w);
        count += w.getCount();
        //System.out.println( "Count now: " + count);
        currVal = w.getVal();
        if( w.move() ) {
          if( nextRow <= m && w.getVal() > nextRow * nextRow ) {
            walkers.add(new RowWalker( nextRow, nextRow, m, n ));
            //System.out.println( "Adding row " + nextRow );
            nextRow++;
          }
          walkers.add(w);
        }
      }

      return currVal;
  }

  private static int getStartColumnForRow( int r, int startRow ) {
    int retVal = r; // default
    if( startRow > 0 && startRow > r ) {
      retVal = Double.valueOf(Math.ceil(
        Integer.valueOf(startRow*startRow).doubleValue() / Integer.valueOf(r).doubleValue()
      )).intValue();
    }
//System.out.println( "Start col for row " + r + ": " + retVal);
    return retVal;
  }

  private static int refineStartRow( int startRow, int m, int n, int k ) {
    double rd = Integer.valueOf(startRow).doubleValue() - 1.0;
    double kd = Integer.valueOf(k).doubleValue();
    double lnmn = Math.log(Integer.valueOf(m).doubleValue() * Integer.valueOf(n).doubleValue());
    double result = 0.0;
    while( result <= kd && rd < m ) {
      rd = rd + 1.0;
      //System.out.println( "refining: try: " + (rd + 1.0) );
      result = Math.pow((rd + 1.0),2.0) * (1.0 + lnmn - (2.0 * Math.log(rd + 1.0)));
      //System.out.println( "refining: got: " + result );
    }

    return Double.valueOf(rd).intValue();
  }

  // private static int getPreCutCount( int cutRows, int m, int n ) {
  //   int retVal = 0;
  //   if( cutRows > 0 ) {
  //     retVal = cutRows * m + cutRows * ( n - cutRows);
  //   }

  //   return retVal;
  // }

  private static class RowWalker implements Comparable<RowWalker> {
    public RowWalker(int row, int col, int m, int n) { this.row = row; this.col = col; this.m = m; this.n = n; }
    int row;
    int col;
    int m;
    int n;
    boolean hasSpawned = false;
    public boolean equals(Object o) {
      return this.row == ((RowWalker)o).row;
    }
    public boolean move() {
      this.col++;
      return this.col <= n;
    }
    public int compareTo( RowWalker r ) {
      return this.equals(r) ? 0 : (this.getVal() <= r.getVal() ? -1 : 1);
    }
    public int getCount() { 
      if( this.row != this.col && this.col <= this.m ) {
        return 2;
      } else {
        return 1;
      }
    }
    public int getVal() { return this.row * this.col; }
    public String toString() {
      return "[" + this.row + "," + this.col + "," + this.getVal() + "]";
    }
  }
}

