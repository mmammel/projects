public class MultiArray
{
  public static void main( String [] args )
  {
    String [][] multiArray = new String [5][];
    for( int i = 0; i < multiArray.length; i++ )
    {
      multiArray[i] = new String [10];
    }

    for( int j = 0; j < multiArray.length; j++ )
    {
      for( int k = 0; k < multiArray[j].length; k++ )
      {
        System.out.println( "[" + j + "," + k + "]" );
      }
    }
  }
}