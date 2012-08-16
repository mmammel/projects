public class IDXTest
{

  public int getIdx( int x, int y )
  {
    return ((x/3)*3 + (y/3));
  }
  
  public static void main( String [] args )
  {
    IDXTest it = new IDXTest();
    
    for( int i = 0; i < 9; i++ )
    {
      for( int j = 0; j < 9; j++ )
      {
        System.out.println( "(" + i + "," + j + ")->" + it.getIdx(i,j) );
      }
    }
  }
}