import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class GeneralEuchreTournament {
  private static final int MAX_PLAYERS = 24;
  private static final String TOKENS = "ABCDEFGHIJKLMNOPQRSTUVWX";
  private static final Pattern ROUNDPATTERN = Pattern.compile("^(?:([A-X]{4}) ?)+?([A-X]{1,3})?$");

  private int numPlayers = 0;
  private int numTables = 0;
  private int numSitters = 0;
  private int distinctTeams = 0;
  private int desiredRounds = 0;
  private boolean opponentBalanced = false;
  private String [] players; // all of the players.
  private String [] teams;   // all of the possible teams, or pairings of players.
  private String [] games;   // all of the possible games, or pairings of teams.
  private String [] rounds;
  private Tournament [] tournaments;

  public GeneralEuchreTournament( int numPlayers, int desiredRounds, boolean opponentBalanced ) {
    this.opponentBalanced = opponentBalanced;
    this.numPlayers = numPlayers;
    this.desiredRounds = desiredRounds;
    this.numTables = this.numPlayers / 4;
    this.numSitters = this.numPlayers % 4;
    this.distinctTeams = ((this.numPlayers * this.numPlayers) - this.numPlayers) / 2;
    System.out.println( "Num players: " + this.numPlayers + ", distinct teams: " + this.distinctTeams + ", desired rounds: " + this.desiredRounds );
    this.players = new String [ this.numPlayers ];
    for( int i = 0; i < this.numPlayers; i++ ) {
      players[i] = ""+TOKENS.charAt(i);
    }
  }

  public static void main( String [] args ) {
    int numPlayers = 8;
    int desiredRounds = 7;
    boolean opponentBalanced = false;

    if( args.length < 1 || args.length > 5 ) {
      System.out.println( "Usage: java GeneralEuchreTournament -p <numPlayers> [-r <desiredRounds>] [-ob]");
      System.out.println( "<desiredRounds> defaults to <numPlayers> minus one.  -ob indicates the tournament must be opponent balanced.");
      System.exit(1);
    } else {
      String token = null;
      for( int i = 0; i < args.length; i++ ) {
        token = args[i];

        if( token.equals("-p") ) {
          // read number of players.
          token = args[++i];
          try {
            numPlayers = Integer.parseInt( token );
            if( numPlayers > MAX_PLAYERS ) {
              numPlayers = 8;
              System.out.println( "Max players is " + MAX_PLAYERS + ", using 8." );
            }
          } catch( Exception e ) {
            System.out.println( "Bogus input given, using 8 players" );
            numPlayers = 8;
            desiredRounds = 7;
          }
        } else if( token.equals("-r") ) {
          // read desired rounds.
          token = args[++i];
          try {
            desiredRounds = Integer.parseInt( token );
          } catch( Exception e ) {
            System.out.println( "Bogus input given, using 8 players" );
            desiredRounds = -1;
          }        
        } else if( token.equals("-ob") ) {
          opponentBalanced = true;
        } else {
          System.out.println( "Usage: java GeneralEuchreTournament -p <numPlayers> [-r <desiredRounds>] [-ob]");
          System.out.println( "<desiredRounds> defaults to <numPlayers> minus one.  -ob indicates the tournament must be opponent balanced.");
          System.exit(1);
        }
      }

      if( desiredRounds == -1 ) {
        desiredRounds = numPlayers - 1;
      }
    }

    GeneralEuchreTournament GT = new GeneralEuchreTournament(numPlayers, desiredRounds, opponentBalanced );
    GT.build();
  }

  public void build() {
    System.out.println( "Players: " + Arrays.toString(this.players) );
    this.buildTeams();
    System.out.println( "Teams: " + Arrays.toString(this.teams) );
    this.buildGames();
    System.out.println( "Games (" + this.games.length + "): " + Arrays.toString(games) );
    this.buildRounds();
    System.out.println( "Rounds (" + this.rounds.length + "): " + Arrays.toString(rounds) );

    // for( String r : this.rounds ) {
    //   RoundContext rc = new RoundContext(r);
    //   System.out.println( rc.teams + " " + r );
    // }

    this.buildTournaments();
  }

  private void buildTeams() {
    int numTeams = ((players.length * players.length) - players.length) / 2;
    this.teams = new String [ numTeams ];
    int teamIdx = 0;
    for( int i = 1; i < players.length; i++ ) {
      for( int j = 0; j < i; j++ ) {
        this.teams[ teamIdx++ ] = ""+players[j] + players[i];
      }
    }

    Arrays.sort( this.teams, String.CASE_INSENSITIVE_ORDER );
  }

  /**
   * Rounds consist of a group of games, one for each table, plus any sitters left over.  In 
   * every round, regardless of the number of players, each player must appear exactly once in the round - either at a 
   * table in a game or sitting.  This method will build all possible rounds based on the current games, tables, and sitters.
   */
  private void buildRounds() {
     List<String> tempRounds = new ArrayList<String>();
     int [] gamePointers = new int [ this.numTables ];
     for( int i = 0; i < this.numTables; i++ ) {
       gamePointers[i] = i;
     }

     // begin by advancing the last pointer as far as possible.
     this.buildRoundsInner( gamePointers, tempRounds ); 

     this.rounds = tempRounds.toArray(new String [0]);
  }

  private void buildRoundsInner( int [] pointers, List<String> roundList ) {
    String tempRound = null;
    boolean moreTuples = true;
    Set<String> seen = new HashSet<String>();
    while( moreTuples ) {
      if( pointers[0] == (this.games.length - pointers.length) ) {
        moreTuples = false;
      } else {
        for( int i = 1; i < pointers.length; i++ ) {
          if( pointers[i] == (this.games.length - (pointers.length - i)) ) {
            // this pointer is at the end of the line, bump the previous one.
            pointers[i-1] += 1;

            // now reset all following pointers.
            for( int j = i; j < pointers.length; j++ ) {
              pointers[j] = pointers[j-1] + 1;
            }
            break;
          }
        }

        RoundContext rc = null;
        String orderedKey = null;
        // now march the last pointer to the end
        while( pointers[pointers.length - 1] < this.games.length ) {
          if( validRound(pointers) ) {
            tempRound = this.buildRoundString(pointers);

            rc = new RoundContext(tempRound);
            orderedKey = ""+rc.teams;
            if( !seen.contains(orderedKey) ) {
              seen.add( orderedKey );
            
              if( this.numSitters > 0 ) {
               tempRound += (" " + this.getSitters(tempRound));
              }
              
              roundList.add(tempRound);
            }
          }
          pointers[pointers.length - 1] += 1;
        }
        pointers[pointers.length - 1] = this.games.length - 1;
      }
    }
  }

  private String buildRoundString( int [] pointers ) {
    StringBuilder sb = new StringBuilder();
    for( int i = 0; i < pointers.length; i++ ) {
      if( i > 0 ) sb.append(" ");
      sb.append( this.games[pointers[i]] );
    }

    return sb.toString();
  }

  private void buildTournaments() {
    List<Tournament> tempTournaments = new ArrayList<Tournament>();
    int [] roundPointers = new int [ this.desiredRounds ];
    for( int i = 0; i < this.desiredRounds; i++ ) {
      roundPointers[i] = 0;
    }

    this.buildTournamentsInner( roundPointers, 0, new Tournament(), tempTournaments );

    this.tournaments = tempTournaments.toArray(new Tournament[0]);
  }

  private void buildTournamentsInner( int [] pointers, int currIdx, Tournament currTournament, List<Tournament> tournaments ) {
    // attempt to push a round into the tournament
    for( int i = pointers[currIdx]; i <= (this.rounds.length - (pointers.length - currIdx)); i++ ) {
      if( currTournament.pushRound(this.rounds[pointers[currIdx]]) ) {
        // this one works, bump the index, set it to the next round unless we are on the last pointer, then we have found a full tournament.
        if( currIdx == (pointers.length - 1) ) {
          // we found a tournament!  See if it is balanced.
          if( currTournament.isBalanced() && (!this.opponentBalanced || currTournament.maxOpponentGap == 0) ) {
            System.out.println("Tournament found! \n" + currTournament );
          }
        } else {
          // recurse to the next pointer.
          pointers[currIdx+1] = pointers[currIdx] + 1;
          buildTournamentsInner(pointers, currIdx+1, currTournament, tournaments);
        }
        // now remove the round, and try the next one.
        currTournament.popRound();
      }
      pointers[currIdx]++;
    }
  }

  private void buildTournaments_old() {
    List<Tournament> tempTournaments = new ArrayList<Tournament>();
    int [] roundPointers = new int [ this.desiredRounds ];
    for( int i = 0; i < this.desiredRounds; i++ ) {
      roundPointers[i] = i;
    }

    this.buildTournamentsInner_old(roundPointers, tempTournaments);

    this.tournaments = tempTournaments.toArray(new Tournament[0]);
  }

  private void buildTournamentsInner_old(int [] pointers, List<Tournament> tournamentList ) {
    Tournament tempTournament = null;
    boolean moreTuples = true;
    while( moreTuples ) {
      if( pointers[0] == (this.rounds.length - pointers.length) ) {
        moreTuples = false;
      } else {
        for( int i = 1; i < pointers.length; i++ ) {
          if( pointers[i] == (this.rounds.length - (pointers.length - i)) ) {
            // this pointer is at the end of the line, bump the previous one.
            pointers[i-1] += 1;

            // now reset all following pointers.
            for( int j = i; j < pointers.length; j++ ) {
              pointers[j] = pointers[j-1] + 1;
            }
            break;
          }
        }

        // now march the last pointer to the end
        while( pointers[pointers.length - 1] < this.rounds.length ) {
          System.out.println( "Checking: " + Arrays.toString(pointers));
          if( validTournament(pointers) ) {
            tempTournament = new Tournament( pointers, this );
            tournamentList.add( tempTournament );
            System.out.println("Found good tournament! : \n" + tempTournament );
          }
          pointers[pointers.length - 1]++;
        }
        pointers[pointers.length - 1] = this.rounds.length - 1;
      }
    }
  }

  public boolean validTournament(int [] pointers ) {
    // Check to see if the pointed to rounds overlap teams at all.
    boolean retVal = true;
    EnumSet<Team> checker = EnumSet.noneOf(Team.class);

    String tempRound = null;
    for( int i = 0; i < pointers.length; i++ ) {
      tempRound = this.rounds[pointers[i]];
      RoundContext rc = new RoundContext(tempRound);
      if( Collections.disjoint(checker, rc.teams) ) {
        checker.addAll( rc.teams );
      } else {
        retVal = false;
        break;
      }
    }

    return retVal;
  }

  /**
   * Build all possible games from the list of teams, these games are valid pairings
   * where no player is on both teams.  They will later be combined into valid rounds.
   */
  private void buildGames() {
    Set<String> seen = new HashSet<String>();
    List<String> retVal = new ArrayList<String>();

    String tempTeam = null;
    for( int i = 1; i < teams.length; i++ ) {
      for( int j = 0; j < i; j++ ) {
        tempTeam = teams[j] + teams[i];
        if( this.validGame( teams[j], teams[i]) && !seen.contains( tempTeam ) ) {
          seen.add( tempTeam );
          retVal.add( tempTeam );
        }
      }
    }   

    retVal.sort( String.CASE_INSENSITIVE_ORDER );
    this.games = retVal.toArray( new String[0] );
  }

  /**
   * Determine if two teams can play against each other without any player overlap.
   * Each team is represented by a two-character string, where each character is a player.
   * @param t1 The first team (e.g., "AB")
   * @param t2 The second team (e.g., "CD")
   * @return true if the teams have no players in common, false otherwise
   */
  private boolean validGame( String t1, String t2 ) {
    return t1.charAt(0) != t2.charAt(0) && t1.charAt(0) != t2.charAt(1) &&
           t1.charAt(1) != t2.charAt(0) && t1.charAt(1) != t2.charAt(1);
  }

  /**
   * Determine if the two games (e.g. ABCD, EFGH) can be combined into a round
   * without any player overlap. Each game string consists of four characters,
   * representing two teams of two players each (e.g., "ABCD" means team "AB"
   * vs team "CD").  Assume the games are valid.
   * @param g1 The first game string
   * @param g2 The second game string
   * @return true if the games can be combined into a round without player overlap, false otherwise
   */
  private boolean validRound( int ... gamePointers ) {
    Set<Character> playersInRound = new HashSet<Character>();
    String [] games = new String [ gamePointers.length ];
    for( int i = 0; i < gamePointers.length; i++ ) {
      games[i] = this.games[ gamePointers[i]];
    } 
    for( String game : games ) {
      for( char player : game.toCharArray() ) {
        if( playersInRound.contains(player) ) {
          return false;
        }
        playersInRound.add(player);
      }
    }
    return true;
  }

  private String getSitters( String round ) {
    Set<Character> playersInRound = new HashSet<Character>();
    for (char player : round.toCharArray()) {
      playersInRound.add(player);
    }
    StringBuilder sitters = new StringBuilder();
    for (String player : players) {
      if (!playersInRound.contains(player.charAt(0))) {
        sitters.append(player);
      }
    }
    return sitters.toString();
  }

  public int getNumPlayers() {
    return numPlayers;
  }

  public void setNumPlayers(int numPlayers) {
    this.numPlayers = numPlayers;
  }

  public int getNumTables() {
    return numTables;
  }

  public void setNumTables(int numTables) {
    this.numTables = numTables;
  }

  public int getNumSitters() {
    return numSitters;
  }

  public void setNumSitters(int numSitters) {
    this.numSitters = numSitters;
  }

  public String[] getPlayers() {
    return players;
  }

  public void setPlayers(String[] players) {
    this.players = players;
  }

  public String[] getTeams() {
    return teams;
  }

  public void setTeams(String[] teams) {
    this.teams = teams;
  }

  public String[] getGames() {
    return games;
  }

  public void setGames(String[] games) {
    this.games = games;
  }

  public String[] getRounds() {
    return this.rounds;
  }

  public void setRounds( String [] rounds ) {
    this.rounds = rounds;
  }

  public int getDistinctTeams() {
    return distinctTeams;
  }

  public void setDistinctTeams(int distinctTeams) {
    this.distinctTeams = distinctTeams;
  }

  private static class RoundContext {
    String round = null;
    EnumSet<Player> players = EnumSet.noneOf(Player.class);
    EnumSet<Team> teams = EnumSet.noneOf(Team.class);
    EnumSet<Player> sitters = EnumSet.noneOf(Player.class);
    Map<Player,Team> opponentMap = new HashMap<Player,Team>();

    public RoundContext( String roundDescriptor ) {
      this.round = roundDescriptor;
      String [] tokens = roundDescriptor.split(" ");
      for( String token : tokens ) {
        if( token.length() == 4 ) {
          // it's a game
          Team t1 = Team.valueOf(token.substring(0,2));
          Team t2 = Team.valueOf(token.substring(2));
          teams.add(t1);
          teams.add(t2);
          players.addAll(t1.getPlayerSet());
          players.addAll(t2.getPlayerSet());

          // populate the opponentMap for this round, used later to determine opponent balance.
          for( Player p : t1.getPlayerSet() ) {
            opponentMap.put( p, t2);
          }

          for( Player p : t2.getPlayerSet() ) {
            opponentMap.put( p, t1);
          }

        } else {
          // add users to sitters
          Player tempPlayer = null;
          for( Character c : token.toCharArray() ) {
            tempPlayer = Player.valueOf(""+c);
            sitters.add(tempPlayer);
          }
        }
      }
    }

    public String toString() {
      // StringBuilder sb = new StringBuilder("[");
      // sb.append("teams: ").append(this.teams).append(", sitters: ").append(this.sitters).append("]");
      // return sb.toString();
      return this.round;
    }

  }

  private static class Tournament {
    // all of the teams in this tournament, avoid having the same team twice
    private EnumSet<Team> teams = EnumSet.noneOf(Team.class);
    // the rounds
    private List<RoundContext> rounds = new ArrayList<RoundContext>();
    // track how many times each player has to sit.
    private Map<Player,Integer> sitterCounts = new HashMap<Player,Integer>();
    // how many games does each player play?
    private Map<Player,Integer> playerCounts = new HashMap<Player,Integer>();
    // track opponent counts
    private Map<Player,Map<Player,Integer>> opponentCounts = new HashMap<Player,Map<Player,Integer>>();

    // when we check to see if a tournament is balanced, calculate how big the widest gap
    // between how many times one player plays another one is.  If it's 0, the tournament is 
    // opponent balanced - but some # of players make this impossible, so just find one
    // with a low gap.
    private int maxOpponentGap = 0;

    public Tournament() {

    }

    public Tournament( int [] roundPointers, GeneralEuchreTournament et ) {
      for( int i = 0; i < roundPointers.length; i++ ) {
        this.pushRound( et.getRounds()[roundPointers[i]] );
      }
    }

    // push a new round onto the tournament, adjust the stats if it can be added, otherwise return false.
    public boolean pushRound( String roundDescriptor ) {
      boolean retVal = false;
      RoundContext rc = new RoundContext(roundDescriptor);
      if( Collections.disjoint(this.teams, rc.teams) ) {
        // we don't have any of these teams yet, go ahead and add.
        retVal = true;
        this.rounds.add(rc);

        // track the teams that have now been added.
        this.teams.addAll( rc.teams );

        // log the opponentCounts
        Team tempOpponentTeam = null;
        Integer tempCount = 0;
        Map<Player,Integer> tempOppoCount = null;
        for( Player p : rc.opponentMap.keySet() ) {
          // loop through each player, and add to the opponent count
          tempOpponentTeam = rc.opponentMap.get(p);
          if( (tempOppoCount = this.opponentCounts.get(p)) == null ) {
            tempOppoCount = new HashMap<Player,Integer>();
            this.opponentCounts.put(p, tempOppoCount);
          }
          // add p1 and p2 from tempOpponentTeam
          if( (tempCount = tempOppoCount.get(tempOpponentTeam.getP1())) == null ) {
            tempOppoCount.put(tempOpponentTeam.getP1(), 1 );
          } else {
            tempOppoCount.put(tempOpponentTeam.getP1(), tempCount + 1 );
          }

          if( (tempCount = tempOppoCount.get(tempOpponentTeam.getP2())) == null ) {
            tempOppoCount.put(tempOpponentTeam.getP2(), 1 );
          } else {
            tempOppoCount.put(tempOpponentTeam.getP2(), tempCount + 1 );
          }
        }

        // log the sitter counts
        for( Player p : rc.sitters ) {
          if( (tempCount = this.sitterCounts.get(p)) == null ) {
            this.sitterCounts.put(p, 1);
          } else {
            this.sitterCounts.put(p, tempCount + 1);
          }
        }

        // log the player counts
        for( Player p : rc.players ) {
          if( (tempCount = this.playerCounts.get(p)) == null ) {
            this.playerCounts.put(p, 1);
          } else {
            this.playerCounts.put(p, tempCount + 1);
          }
        }        

      }

      return retVal;
    }

    // remove the last round added to the tournament, adjust the stats.
    public RoundContext popRound() {
      // remove the round
      RoundContext rc = this.rounds.remove( this.rounds.size() - 1 );

      // remove the teams
      this.teams.removeAll(rc.teams);

      // adjust opponent counts
      Team tempOpponentTeam = null;
      Integer tempCount = 0;
      Map<Player,Integer> tempOppoCount = null;
      for( Player p : rc.opponentMap.keySet() ) {
        // loop through each player, and add to the opponent count
        tempOpponentTeam = rc.opponentMap.get(p);
        if( (tempOppoCount = this.opponentCounts.get(p)) == null ) {
          System.out.println("Warning: removed a round from a tournament, but there was no oppoent count.");
        } else {
          // add p1 and p2 from tempOpponentTeam
          tempCount = tempOppoCount.get(tempOpponentTeam.getP1());
          if( tempCount == 1 ) {
            tempOppoCount.remove(tempOpponentTeam.getP1());
          } else {
            tempOppoCount.put( tempOpponentTeam.getP1(), tempCount - 1);
          }

          tempCount = tempOppoCount.get(tempOpponentTeam.getP2());
          if( tempCount == 1 ) {
            tempOppoCount.remove(tempOpponentTeam.getP2());
          } else {
            tempOppoCount.put( tempOpponentTeam.getP2(), tempCount - 1);
          }          
        }
      }

      // log the sitter counts
      for( Player p : rc.sitters ) {
        if( (tempCount = this.sitterCounts.get(p)) == 1 ) {
          this.sitterCounts.remove(p);
        } else {
          this.sitterCounts.put(p, tempCount - 1);
        }
      }

      // log the player counts
      for( Player p : rc.players ) {
        if( (tempCount = this.playerCounts.get(p)) == 1 ) {
          this.playerCounts.remove(p);
        } else {
          this.playerCounts.put(p, tempCount - 1);
        }
      }

      return rc;
    }

    public boolean isBalanced() {
      boolean retVal = true;
      // every one who plays, sits (or nobody sits)
      if( !this.sitterCounts.isEmpty() && (this.sitterCounts.size() != this.playerCounts.size()) ) {
        retVal = false;
      }

      if( retVal ) {
        // we're still good.  See if everyone plays the same amount of games
        int count = 0, prevCount = 0;
        for( Player p : this.playerCounts.keySet() ) {
          count = this.playerCounts.get(p);
          if( prevCount > 0 && count != prevCount ) {
            retVal = false;
            break;
          } else {
            prevCount = count;
          }
        }
      }

      if( retVal && !this.sitterCounts.isEmpty() ) {
        // we're still good, see if everyone sits the same amount of times
        int count = 0, prevCount = 0;
        for( Player p : this.sitterCounts.keySet() ) {
          count = this.sitterCounts.get(p);
          if( prevCount > 0 && count != prevCount ) {
            retVal = false;
            break;
          } else {
            prevCount = count;
          }
        }
      }


      // calculate the maxOpponentGap
      int count = 0, min = 99, max = 0;
      Map<Player, Integer> tempOppoMap = null;
      for( Player p : this.opponentCounts.keySet() ) {
        tempOppoMap = this.opponentCounts.get(p);
        for( Player o : tempOppoMap.keySet() ) {
          count = tempOppoMap.get(o);
          if( count < min ) min = count;
          if( count > max ) max = count;
        }
      }
      this.maxOpponentGap = max - min;


      return retVal;
    }

    public String toString() {
      StringBuilder sb = new StringBuilder();
      for( RoundContext rc : this.rounds ) {
        sb.append( rc.toString() ).append("\n");
      }
      sb.append( "Opponent counts: "+this.opponentCounts ).append( "\n");
      sb.append( "Play counts: "+this.playerCounts ).append("\n");
      sb.append( "Sitter counts: "+this.sitterCounts ).append("\n");
      sb.append( "Max Opponent Gap: " + this.maxOpponentGap );
      return sb.toString();
    }
  }


}
