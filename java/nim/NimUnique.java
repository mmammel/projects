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
      //System.out.println( "Player " + (player1 ? "One" : "Two") + " Loses!" );
      int [] tempLog = null;
      if( (tempLog = startLogger.get( currentFirstMove )) == null ) {
        tempLog = new int [2];
        startLogger.put(currentFirstMove,tempLog);
      }

      tempLog[ (player1 ? 1 : 0 ) ]++;
    } else if( isWinner( state ) ) {
      //System.out.println( "Player " + (player1 ? "Two" : "One") + " Loses!" );
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
    boolean retVal = false;
    
    if( state[0] + state[1] + state[2] == 1 ) {
      retVal = true;
    } else if( state[0] == 0 && state[1] > 1 && state[1] == state[2] ) {
      retVal = true;
    } else if( state[1] == 0 && state[0] > 1 && state[0] == state[2] ) {
      retVal = true;
    } else if( state[2] == 0 && state[0] > 1 && state[0] == state[1] ) {
      retVal = true;
    } else if( state[0] == 1 && state[1] == 1 && state[2] == 1 ) {
      retVal = true;
    } 

    return retVal;
  }

  private boolean isWinner( int [] state ) {
    boolean retVal = false;

    if( state[0] + state[1] + state[2] == 0 ) {
      retVal = true; // there are none left.
    } else if( state[0] == 0 && state[1] > 0 && state[2] > 1 && state[1] != state[2] ) {
      retVal = true;
    } else if( state[1] == 0 && state[0] > 0 && state[2] > 1 && state[0] != state[2] ) {
      retVal = true;
    } else if( state[2] == 0 && state[0] > 0 && state[1] > 1 && state[0] != state[1] ) {
      retVal = true;
    } else if( state[0] == 0 && state[2] > 0 && state[1] > 1 && state[1] != state[2] ) {
      retVal = true;
    } else if( state[1] == 0 && state[2] > 0 && state[0] > 1 && state[0] != state[2] ) {
      retVal = true;
    } else if( state[2] == 0 && state[1] > 0 && state[0] > 1 && state[0] != state[1] ) {
      retVal = true;
    } else if( state[0] > 0 && state[1] > 0 && state[2] > 0 ) {
      if( (state[0] == state[1] && state[0] > 1) || (state[0] == state[2] && state[0] > 1) || (state[1] == state[2] && state[1] > 1) ) {
        retVal = true;
      } else if( state[0] > 1 && state[1] == 1 && state[2] == 1 ) {
        retVal = true;
      } else if( state[1] > 1 && state[0] == 1 && state[2] == 1 ) {
        retVal = true;
      } else if( state[2] > 1 && state[0] == 1 && state[1] == 1 ) {
        retVal = true;
      }
    }
    
    return retVal;
  }

  public static void main( String [] args ) { 
    NimUnique N = new NimUnique();
    int [] state = new int [3];
    if( args.length == 3 ) {
      try {
        state[0] = Integer.parseInt( args[0] );
        state[1] = Integer.parseInt( args[1] );
        state[2] = Integer.parseInt( args[2] );
      } catch( NumberFormatException nfe ) {
        System.out.println( "Bad input: usage: java NumUnique [x y z] // x,y,z are integers" );
      }
    } else {
      state[0] = 3;
      state[1] = 5;
      state[2] = 7;
    }

    N.play(true, state, true);

    int [] tempWinLoss = null;
    for( String key : startLogger.keySet() ) {
      tempWinLoss = startLogger.get(key);
      System.out.println( key + ": Wins: " + tempWinLoss[0] + ", Losses: " + tempWinLoss[1] );
    }
  }
}
