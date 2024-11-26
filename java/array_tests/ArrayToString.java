import java.util.Arrays;

public class ArrayToString {

  public static void main( String [] args ) {
    int [] arr = new int [5];
    for( int i = 0; i < 5; i++ ) {
      arr[i] = 76*(i+1);
    }

    System.out.println( Arrays.toString(arr) );
  }
}
