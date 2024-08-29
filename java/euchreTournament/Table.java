import java.util.ArrayList;
import java.util.List;

public class Table {
  private int id;
  private List<Game> games;
  private boolean isWaitingRoom;
  
  public Table(int id) {
    this.games = new ArrayList<Game>();
    this.id = id;
  }
  
  public void addGame(Game g) {
    this.games.add(g);
  }
  
  public void popGame() {
    if( this.games != null && this.games.size() > 0 ) {
      int idx = this.games.size() - 1;
      this.games.remove(idx);
    }
  }
  
  public Game peekGame() {
    Game retVal = null;
    if( this.games != null && this.games.size() > 0 ) {
      int idx = this.games.size() - 1;
      retVal = this.games.get(idx);
    }
    
    return retVal;
  }
  
  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public List<Game> getGames() {
    return games;
  }

  public void setGames(List<Game> games) {
    this.games = games;
  }

  public boolean isWaitingRoom() {
    return isWaitingRoom;
  }

  public void setWaitingRoom(boolean isWaitingRoom) {
    this.isWaitingRoom = isWaitingRoom;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    if( this.isWaitingRoom ) {
      sb.append("Waiting Room ").append("\n");
      sb.append("------------").append("\n");      
    } else {
      sb.append("Table ").append(this.id).append("\n");
      sb.append("--------").append("\n");
    }
    
    for( Game g : this.games ) {
      if( this.isWaitingRoom ) {
        boolean first = true;
        for( Participant p : g.getRealParticipants() ) {
          if( first ) {
            first = false;
          } else {
            sb.append(",");
          }
          
          sb.append(p.getId());
        }
        
        sb.append("\n");
      } else {
        sb.append(g.toString()).append("\n");
      }
    }
    
    return sb.toString();
  }
}
