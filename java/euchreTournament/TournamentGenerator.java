import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TournamentGenerator {
  private static String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  
  // number of players.
  private int numPlayers = 0;
  // number of teams.  For odd numbers of players, the last team is incomplete. 7 players, still 8 teams.
  private int numTeams = 0;
  // the players, A, B, C, ...
  private String [] players = null;
  // array of pointers to indices of player, represents current permutation
  private int [] pointers = null;
  private Set<Integer> pointedTo = new HashSet<Integer>();
  
  public TournamentGenerator( int numPlayers ) {
    this.numPlayers = numPlayers;
    this.numTeams = (numPlayers + 1) / 2;
    this.players = new String [ this.numPlayers ];
    for( int i = 0; i < this.numPlayers; i++ ) {
      this.players[i] = ""+ALPHABET.charAt(i);
    }
    this.pointers = new int [ this.numPlayers ];
    
    System.out.println( "Num players: " + this.numPlayers + ", Num teams: " + this.numTeams );
  }
  
  public void process() {
    this.processInner( 0 );
  }
  
  // team 0: 0, team 1: 2, team 2, 4
  
  /**
   * recursive function to find distinct player combinati0ons.
   * @param ps The players
   * @param team Which team, 0-based.
   * @param member Which team member, 0 or 1
   */
  public void processInner( int team ) {
 //   System.out.println( "Processing team #: " + team );
    if( team == this.numTeams || this.pointedTo.size() == this.pointers.length ) {
      this.printPlayers();
    } else {
      int playerOne = team * 2;
      int playerTwo = playerOne + 1;
      
      int prevPlayerOne = (team - 1) * 2;
      
      int playerOneStart = prevPlayerOne <= 0 ? 0 : this.pointers[prevPlayerOne];
      int playerOneEnd = playerOne + 1;
      
// System.out.println( "Team: " + team + ", member: " + member + ", Pointer idx: " + pointerIdx );
      for( int i = playerOneStart; i < playerOneEnd; i++ ) {
        if( !this.pointedTo.contains(i) ) {
          for( int j = i+1; j < this.players.length; j++ ) {
            if( !this.pointedTo.contains(j) ) {
              this.pointers[playerOne] = i;
              this.pointers[playerTwo] = j;
              this.pointedTo.add(i);
              this.pointedTo.add(j);
      //  System.out.println( "Set pointer " + pointerIdx + " to " + this.pointers[pointerIdx] );
              this.processInner(team + 1);
              this.pointedTo.remove(i);
              this.pointedTo.remove(j);
            }
          }

        }
      }
    }
  }
  
  
  private void printPlayers() {
    StringBuilder sb = new StringBuilder();
    for( int i = 0; i < this.pointers.length; i++ ) {
      sb.append( this.players[this.pointers[i]]);
      if( (i+1) % 2 == 0 ) sb.append( " " );
    }
    
    System.out.println( sb.toString() );
  }
  
  public static void main( String [] args ) {
    try {
      int players = Integer.parseInt(args[0]);
      TournamentGenerator TG = new TournamentGenerator(players);
      TG.process();
    } catch( NumberFormatException nfe ) {
      System.err.println( "Bad argument, must be number" );
      System.exit(1);
    }
  }
}
