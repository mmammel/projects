public class KthHighest {
  public static void main( String [] args ) {
    try {
      int m = Integer.parseInt(args[0]);
      int n = Integer.parseInt(args[1]);
      int k = Integer.parseInt(args[2]);

      int temp = 0;

      if( m > n ) {
        temp = m;
        m = n;
        n = temp;
      }

      int [] productCounters = new int [ m*n + 1];

      for( int i = 1; i <= m; i++ ) {
        for( int j = i; j <= n; j++ ) {
          if( i != j && j <= m ) {
            productCounters[i*j] += 2;
          } else {
            productCounters[i*j]++;
          }
        }
      }

      int idx = 0, count = 0;
      while( count < k ) {
        idx++;
        count+=productCounters[idx];
      }

      System.out.println( idx );

    } catch( Exception e ) {
      System.out.println( "Usage: java KthHighest m n k" );
    }
  }
}
