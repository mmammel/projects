/**
  Just play every game possible, spit out the winner (player 1 or player 2, where player one makes the
  first move)
*/
public class Nim {
  public void play( boolean player1, int [] state ) {
    int dec = 0;
    if( isLoser( state ) ) {
      System.out.println( "Player " + (player1 ? "One" : "Two") + " Loses!" );
    } else {
      for( int i = 0; i < state.length; i++ ) {
        for( int j = state[i]; j > 0; j-- ) {
          dec = j;
          state[i] -= dec;
          play( !player1, state );
          state[i] += dec;
        }
      }
    }
  }

  private boolean isLoser( int [] state ) {
    return state[0] + state[1] + state[2] == 1; 
  }

  public static void main( String [] args ) { 
    Nim N = new Nim();
    int [] state = { 3, 5, 7 };
    N.play(true, state);
  }
}
