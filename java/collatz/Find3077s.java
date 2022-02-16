
public class Find3077s {

  private int limit = 0;

  public Find3077s( int n ) {
    this.limit = n;
  }

  public void printCounts() {
    int tmp = 0;
    int total = 0;
    int count = 0;
    for( int i = 2; i <= this.limit; i++ ) {
      tmp = i;
      while( tmp != 1 && tmp != 3077 ) {
        count++;
        tmp = tmp % 2 == 0 ? tmp / 2 : 3 * tmp + 1;
      }

      if( tmp == 3077 ) {
        System.out.println( i );
        total += i;
      }

    }

    System.out.println("Sum: " + total );
  }

  public static void main( String [] args ) {
    try {
      Find3077s PT = new Find3077s( Integer.parseInt(args[0]) ); 
      PT.printCounts();
    } catch( Exception e ) {
      System.out.println( "Exception: " + e.toString() );
    }
  }
}
