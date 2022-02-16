
public class TimeToPowerOfTwo {

  private int limit = 0;

  public TimeToPowerOfTwo( int n ) {
    this.limit = n;
  }

  public void printCounts() {
    int tmp = 0;
    int total = 0;
    int count = 0;
    for( int i = 2; i <= this.limit; i++ ) {
      tmp = i;
      count = 0;
      while( (tmp & (tmp - 1)) != 0 ) {
        count++;
        tmp = tmp % 2 == 0 ? tmp / 2 : 3 * tmp + 1;
      }

      System.out.println( count );
      total += count;
    }

//    System.out.println("Sum: " + total );
  }

  public static void main( String [] args ) {
    try {
      TimeToPowerOfTwo PT = new TimeToPowerOfTwo( Integer.parseInt(args[0]) ); 
      PT.printCounts();
    } catch( Exception e ) {
      System.out.println( "Exception: " + e.toString() );
    }
  }
}
