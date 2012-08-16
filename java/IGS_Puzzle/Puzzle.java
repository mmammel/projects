public class Puzzle
{

  private Piece [] layout = new Piece [6];  
  private Piece [] pieces = new Piece [6];

  public Puzzle()
  {
    pieces[0] = new Piece( 12, 11, 50, 12 );
    pieces[1] = new Piece( 12, 12, 13, 52 );
    pieces[2] = new Piece( 50, 12, 18, 19 );
    pieces[3] = new Piece( 11, 51, 51, 50 );
    pieces[4] = new Piece( 12, 20, 18, 12 );
    pieces[5] = new Piece( 52, 12, 11, 51 );
  }

  private void iterate( int position )
  {
    for( int i = 0; i < 6; i++ )
    {
      for( int j = 0; j < 6; j++ )
      {
      }
    }
  
  }

}