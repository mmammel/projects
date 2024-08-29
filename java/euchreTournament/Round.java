import java.util.ArrayList;
import java.util.List;

public class Round {
  private List<Game> games;

  public Round() {
    this.games = new ArrayList<Game>();
  }
  
  public void addGame(Game g) {
    this.games.add(g);
  }
  
  public List<Game> getGames() {
    return games;
  }

  public void setGames(List<Game> games) {
    this.games = games;
  }
  
  public static List<Round> getRoundsFromGames( List<Game> games, int roundSize ) {
    List<Round> retVal = new ArrayList<Round>();
    Round tempRound = null;
    int count = 0;
    
    for( Game g : games ) {
      if( count % roundSize == 0 ) {
        tempRound = new Round();
        retVal.add(tempRound);
      }
      
      tempRound.addGame(g);
    }
    
    return retVal;
  }
  
}
