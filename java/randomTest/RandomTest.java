import java.util.Random;

public class RandomTest
{
  public static void main( String [] args )
  {
    int j = 0;
    for( int i = 0; i < (j = new Random().nextInt(1000)); i++ )
    {
      System.out.println( "[" + i + "][" + j + "]" );
    }

    for( int k = 0; k < 100; k++ )
    {
      System.out.println( new Random().nextInt(2) + 1 );
    }
  }
}
