import java.util.*;

/**
  For every unique starting move, see what the win/loss ratio looks like regardless of the following moves.

  Follow-up:  it's ~50/50 no matter the starting move!
*/
public class NimUnique {
  private static Map<String,int []> startLogger = new HashMap<String,int []>();
  private static String currentFirstMove = null;

  public void play( boolean player1, int [] state, boolean firstMove ) {
    int dec = 0;
    if( isLoser( state ) ) {
      System.out.println( "Player " + (player1 ? "One" : "Two") + " Loses!" );
      int [] tempLog = null;
      if( (tempLog = startLogger.get( currentFirstMove )) == null ) {
        tempLog = new int [2];
        startLogger.put(currentFirstMove,tempLog);
      }

      tempLog[ (player1 ? 0 : 1 ) ]++;
    } else {
      for( int i = 0; i < state.length; i++ ) {
        for( int j = state[i]; j > 0; j-- ) {
          dec = j;
          state[i] -= dec;
          if( firstMove ) {
            currentFirstMove = ""+state[0]+state[1]+state[2];
          } 
          play( !player1, state, false );
          state[i] += dec;
        }
      }
    }
  }

  private boolean isLoser( int [] state ) {
    return state[0] + state[1] + state[2] == 1; 
  }

  public static void main( String [] args ) { 
    NimUnique N = new NimUnique();
    int [] state = { 3, 5, 7 };
    N.play(true, state, true);

    int [] tempWinLoss = null;
    for( String key : startLogger.keySet() ) {
      tempWinLoss = startLogger.get(key);
      System.out.println( key + ": Wins: " + tempWinLoss[0] + ", Losses: " + tempWinLoss[1] );
    }
  }
}
