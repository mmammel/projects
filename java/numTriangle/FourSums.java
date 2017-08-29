public class FourSums {
  public static void main( String [] args ) {
    for( int i = 1; i < 7; i++ ) {
      for( int j = i+1; j < 8; j++ ) {
        for( int k = j+1; k < 9; k++ ) {
          for( int m = k+1; m < 10; m++ ) {
            System.out.println( i+"+"+j+"+"+k+"+"+m+"="+(i+j+k+m) );
          }
        }
      }
    }
  }
}
