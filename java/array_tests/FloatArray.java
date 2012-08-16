public class FloatArray
{
  public static final float [][] f_array = {
       {0.0f,0.0f,0.0f,0.0f},
       {1.0f,1.0f,1.0f,1.0f},
       {2.0f,2.0f,2.0f,2.0f} };

  public static void main( String [] args )
  {
    //Dump the static array
    for( int i = 0; i < FloatArray.f_array.length; i++ )
    {
      System.out.print( "Row: " );
      for( int j = 0; j < FloatArray.f_array[i].length; j++ )
      {
        System.out.print( "" + FloatArray.f_array[i][j] + " " );
      }
      System.out.print( "\n" );
    }

  }

}
