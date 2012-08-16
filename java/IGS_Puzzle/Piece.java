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
    return   "Side1: " + getSideString( sides[0] ) + ":" + sides[0] + 
           "\nSide2: " + getSideString( sides[1] ) + ":" + sides[1] +
           "\nSide3: " + getSideString( sides[2] ) + ":" + sides[2] + 
           "\nSide4: " + getSideString( sides[3] ) + ":" + sides[3] +
           "\nCorners: " + getCornersString( this.getCorners() ) + ":" + this.getCorners();
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
    int temp0, temp1, temp2, temp3 = 0;
    
    temp0 = flipSide( sides[2] );
    temp1 = flipSide( sides[1] );
    temp2 = flipSide( sides[0] );
    temp3 = flipSide( sides[3] );
    sides[0] = temp0;
    sides[1] = temp1;
    sides[2] = temp2;
    sides[3] = temp3;
  }
  
  public int getSide( int side )
  {
    return sides[side];
  }
  
  public int getCorner( int corner )
  {
    return sides[corner] & MASK;
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
      }

      if( i < 5 ) result = result << 1;
    }

    return result;
        
  }
  
  public int getCorners()
  {
    int test = 0;
    int retVal = 0;
    
    for( int i = 0; i < 4; i++ )
    {
      retVal |= sides[i] & MASK;
      if( i < 3 ) retVal = retVal << 1;
    }
    
    return retVal;
  }
  
  private String getSideString( int side )
  {
    String retVal = new String();
    int test = 0;
    
    for( int i = 0; i < 6; i++ )
    {
      test = side & MASK;
      side = side >> 1;
      retVal = "" + test + retVal;
    }
    
    return retVal;
  }
  
  private String getCornersString( int corners )
  {
    String retVal = new String();
    int test = 0;
    
    for( int i = 0; i < 4; i++ )
    {
      test = corners & MASK;
      corners = corners >> 1;
      retVal = "" + test + retVal;
    }
    
    return retVal;
  }
  
  public void printPiece()
  {
    String side0, side1, side2, side3 = new String();
    StringBuffer piece = new StringBuffer();
    
    side0 = getSideString( sides[0] );
    side1 = getSideString( sides[1] );
    side2 = getSideString( sides[2] );
    side3 = getSideString( sides[3] );
    
    for( int i = 0; i < 6; i++ )
    {
      piece.append( side1.charAt(i) == '0' ? " " : "X" );
    }
    
    piece.append("\n");
    
    piece.append( side0.charAt(4) == '0' ? " " : "X" ).append("XXXX").append( side2.charAt(1) == '0' ? " " : "X" ).append("\n");
    piece.append( side0.charAt(3) == '0' ? " " : "X" ).append("XXXX").append( side2.charAt(2) == '0' ? " " : "X" ).append("\n");
    piece.append( side0.charAt(2) == '0' ? " " : "X" ).append("XXXX").append( side2.charAt(3) == '0' ? " " : "X" ).append("\n");
    piece.append( side0.charAt(1) == '0' ? " " : "X" ).append("XXXX").append( side2.charAt(4) == '0' ? " " : "X" ).append("\n");
    
    for( int j = 5; j >= 0; j-- )
    {
      piece.append( side3.charAt(j) == '0' ? " " : "X" );
    }
    
    piece.append("\n");
    
    System.out.println( piece.toString() );
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
    P.printPiece();
    System.out.println( "No action: \n" + P + "\n" );
    P.rotate();
    P.printPiece();
    System.out.println( "Rotate: \n" + P + "\n" );
    P.flip();
    P.printPiece();
    System.out.println( "Flip: \n" + P + "\n" );
    P.rotate();
    P.printPiece();
    System.out.println( "Rotate: \n" + P + "\n" );    
  }    

}
 