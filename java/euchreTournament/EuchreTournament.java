import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;

public class EuchreTournament {
  
  private int numParticipants;
  private int idealSize; // the highest multiple of four that is larger than or equal to the participant size.
  private List<Table> tables;
  private boolean hasGhosts;
  private List<Participant> participants;
  /**
   * list of pairings that looks like this:
   * Participants: [0,1,2,3]
   * Pairings: [01,02,03,12,13,23]
   * 
   * Participants: [0,1,2,3,4,5,6,7]
   * Pairings: [01,02,03,04,05,06,07,12,13,14,15,16,17,23,24,25,26,27,34,35,36,37,45,46,47,56,57,67]
   *                               *              *           *        *       
   */
  private Pairing [] pairings; 
  
  private static Set<String> seenRounds = new HashSet<String>();

  private EuchreTournament() {
    this.tables = new ArrayList<Table>();
    this.participants = new ArrayList<Participant>();
  }

  public EuchreTournament( int num ) {
    this();
    
    this.numParticipants = num;
    this.idealSize = ((num/4)*4) + ((num % 4) > 0 ? 4 : 0);

    this.hasGhosts = this.idealSize - this.numParticipants > 0;
    
    int numTables = this.idealSize / 4;
    for( int i = 0; i < numTables; i++ ) {
      this.tables.add(new Table(i+1));
    }
    
    if( this.hasGhosts ) {
      this.tables.get(numTables - 1).setWaitingRoom(true);
    }
    
    Participant tempParticipant;
    for( int i = 0; i < this.idealSize; i++ ) {
      tempParticipant = new Participant(i);
      if( i > (this.numParticipants - 1) ) {
        tempParticipant.setGhost(true);
      }
      
      this.participants.add(tempParticipant);
    }
    
    this.pairings = new Pairing[((this.idealSize * this.idealSize) - this.idealSize)/2];
    
    /**
     * 
     * half matrix of pairings:
     * 
     *    0  1  2  3  4  5  6  7
     * 0  X 01 02 03 04 05 06 07
     * 1  X  X 12 13 14 15 16 17
     * 2  X  X  X 23 24 25 26 27
     * 3  X  X  X  X 34 35 36 37  --> [01,02,03,04,...,67]
     * 4  X  X  X  X  X 45 46 47 
     * 5  X  X  X  X  X  X 56 57
     * 6  X  X  X  X  X  X  X 67
     * 7  X  X  X  X  X  X  X  X
     */
    int idx = 0;
    for( int i = 0; i < this.idealSize - 1; i++ ) {
      for( int j = i+1; j < this.idealSize; j++ ) {
        idx = (i*this.idealSize) + j - (((i+1)*(i+2))/2);
        this.pairings[idx] = new Pairing(this.participants.get(i), this.participants.get(j));
      }
    }
    
    /**
     * Construct an array of all possible rounds, where each round is a set of 
     * simultaneous games involving all idealSize participants, e.g for an 8 person 
     * tournament some rounds could be:
     * 
     * 01 vs. 23, 45 vs. 67
     * or
     * 07 vs. 16, 25 vs. 34
     */
    // how many simultaneous games per round?
    int roundSize = this.idealSize / 4;
    int [] pairingPointers = new int [ roundSize * 2 ]; // one pointer per pairing
    
    for( int i = 0; i < pairingPointers.length; i++ ) {
      pairingPointers[i] = -1;
    }
    
  }
  
  /**
   * Assumptions:
   * 
   * pairings has an even number of elements
   * pairings is ordered, in the sense that each pairing maintains the same
   * order it has in the master pairing list (e.g. 01,02,03,...,12,13,14,...,67
   * @param pairings
   * @return
   */
  private List<Game> getAllDistinctGamesFromPairings( Pairing [] pairings ) {
    List<Game> retVal = new ArrayList<Game>();
    List<Game> subPairings = null;
    int subCount = 0;
    
    if( pairings.length == 2 ) {
      retVal.add( new Game( pairings[0], pairings[1] ) );
    } else {
      // walk pairings with a set of pointers, and for each one get the pairings from the un-pointed
      // at pairings
      int i = 0;
      for( int j = i+1; j < pairings.length; j++ ) {
        subPairings = getAllDistinctGamesFromPairings( getSubArrayOfPairings( pairings, i, j ) );
        for( Game g : subPairings ) {
          if( subCount++ % ((pairings.length - 2)/2) == 0 ) {
            retVal.add( new Game(pairings[i], pairings[j]) );
          }
          retVal.add( g );
        }
        
        subCount = 0;
      }
    }
    
    return retVal;
  }
  
  /**
   * Get all of the pairings in "pairings" except those at indexes p1 and p2
   * @param pairings
   * @param p1
   * @param p2
   * @return
   */
  private Pairing [] getSubArrayOfPairings( Pairing [] pairings, int p1, int p2) {
    Pairing [] retVal = new Pairing [ pairings.length - 2 ];
    int idx = 0;
    for( int i = 0; i < pairings.length; i++ ) {
      if( i != p1 && i != p2 ) {
        retVal[ idx++ ] = pairings[i];
      }
    }
    
    return retVal;
  }
  
  public void buildTournament() {

  }
  
  private void buildTournamentInner(Set<Pairing> seen, Set<Integer> rowColExclude, int tableIdx ) {
    // how many total pairings do we have to use

  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[ tables: ").append(this.tables.size()).append("]\n");
    sb.append("[ participants: ").append(this.numParticipants).append("]\n");
    sb.append("[ idealSize: ").append(this.idealSize).append("]\n");
    sb.append("[ pairings: ").append(this.getPairingsTableString()).append("]\n");
    sb.append("[ hasGhosts: ").append(this.hasGhosts).append("]\n");
    return sb.toString();
  }
  
  private String getPairingsTableString() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("[ ");    
    for( int i = 0; i < this.pairings.length; i++ ) {
      sb.append(this.pairings[i]).append(" ");
    }
    sb.append("]\n");
    
    return sb.toString();
  }
  
  public static void main( String [] args ) {
    if( args.length != 1 ) {
      System.out.println( "error: too few arguments: usage: java EuchreTournament <num>" );
      System.exit(1);
    }

    int num = 4;
    String numStr = args[0];
    try {
      num = Integer.parseInt(numStr);
    } catch( Exception e ) {
      System.out.println("error: bad argument: provide a number of participants: " + numStr + " is not valid");
      System.exit(1);
    }

    EuchreTournament ET = new EuchreTournament( num );
    System.out.println( ET.toString() );
    ET.buildTournament();
  }
}
