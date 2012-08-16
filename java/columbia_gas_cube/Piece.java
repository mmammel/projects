public class Piece
{
  public static final int COMPLETE = 0x111111;
  public static final int MASK     = 0x000001;

  int [] sides = null;

  public Piece( int s1, int s2, int s3, int s4 )
  {
    if( sides == null )
    {
      sides = new int [4];
    }

    sides[0] = s1;
    sides[1] = s2;
    sides[2] = s3;
    sides[3] = s4;
  }

  public String toString()
  {
    return "Side1: " + sides[0] + ", Side2: " + sides[1] +
           ", Side3: " + sides[2] + ", Side4: " + sides[3];
  }

  public void rotate()
  {
    int temp = sides[0];
    sides[0] = sides[3];
    sides[3] = sides[2];
    sides[2] = sides[1];
    sides[1] = temp;
  }

  public void flip()
  {
    sides[0] = flipSide( sides[2] );
    sides[1] = flipSide( sides[1] );
    sides[2] = flipSide( sides[0] );
    sides[3] = flipSide( sides[3] );
  }

  private int flipSide( int side )
  {
    int temp = side;
    int test = 0;
    int result = 0;

    for( int i = 0; i < 6; i++ )
    {
      test = temp & MASK;
      temp = temp >> 1;
      if( test == 1 )
      {
        result |= MASK;
  System.out.println( "Result: " + result );
      }

      if( i < 5 ) result = result << 1;
    }

    return result;
        
  }
      

  public static void main( String [] args )
  {
    int [] params = new int [4];
    Piece P = null;

    for( int i = 0; i < 4; i++ )
    {
      params[i] = Integer.parseInt( args[i] );
    }

    P = new Piece( params[0], params[1], params[2], params[3] );
    System.out.println( P );
    P.rotate();
    System.out.println( P );
    P.flip();
    System.out.println( P );
    P.rotate();
    System.out.println( P );
    
  }    

}
