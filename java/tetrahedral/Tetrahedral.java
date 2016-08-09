public class Tetrahedral {
  public static final int [][][] TETRAHEDRAL_GROUP = {
    {
      {-1, 0, 0}, {0, -1, 0}, {0, 0, 1}
    }, 
    { 
      {-1, 0, 0}, {0, 1, 0}, {0, 0, -1}
    }, 
    { 
      {0, -1, 0}, {0, 0, -1}, {1, 0, 0}
    }, 
    {
      {0, -1, 0}, {0, 0, 1}, {-1, 0, 0}
    }, 
    {
      {0, 0, -1}, {-1, 0, 0}, {0, 1, 0}
    }, 
    {
      {0, 0, -1}, {1, 0, 0}, {0, -1, 0}
    }, 
    {
      {0, 0, 1}, {-1, 0, 0}, {0, -1, 0}
    }, 
    {
      {0, 0, 1}, {1, 0, 0}, {0, 1, 0}
    }, 
    {
      {0, 1, 0}, {0, 0, -1}, {-1, 0, 0}
    }, 
    {
      {0, 1, 0}, {0, 0, 1}, {1, 0, 0}
    }, 
    {
      {1, 0, 0}, {0, -1, 0}, {0, 0, -1}
    }, 
    {
      {1, 0, 0}, {0, 1, 0}, {0, 0, 1}
    }
  };

  public static final float [][] VERTICES = {
    {.4f, .8f, 2.0f}, {-.4f, -.8f, 2.0f}, {-1.5f, -1.5f, 1.5f}, {-2.0f, -.4f, .8f}, {-1.0f, 1.0f, 1.0f}
  };


  /*
    Apply the tetrahedral group to the vertices, and print it out.
  */
  public static void multiply() {
    float x = 0, y = 0, z = 0;
    int [][] temp2d = null;
    int [] temp1d = null;
    for( float [] vertex : VERTICES ) {
      for( int i = 0; i < TETRAHEDRAL_GROUP.length; i++ ) {
        temp2d = TETRAHEDRAL_GROUP[i];
        x = vertex[0] * temp2d[0][0] + vertex[1] * temp2d[1][0] + vertex[2] * temp2d[2][0];
        y = vertex[0] * temp2d[0][1] + vertex[1] * temp2d[1][1] + vertex[2] * temp2d[2][1];
        z = vertex[0] * temp2d[0][2] + vertex[1] * temp2d[1][2] + vertex[2] * temp2d[2][2];
        System.out.println( "" + x + "," + y + "," + z );
      }
    }   
  }

  public static void main( String [] args ) {
    Tetrahedral.multiply();
  }

}
