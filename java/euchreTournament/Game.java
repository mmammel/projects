import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Game {
  private Pairing p1;
  private Pairing p2;
  
  public Game( Pairing p1, Pairing p2 ) {
    this.p1 = p1;
    this.p2 = p2;
  }
  
  public String toString() {
    return this.p1.toString() + " vs. " + this.p2.toString();
  }
  
  public Set<Integer> getIdxSet() {
    Set<Integer> retVal = new HashSet<Integer>();
    this.p1.collectIds(retVal);
    this.p2.collectIds(retVal);
    return retVal;
  }
  
  public List<Participant> getRealParticipants() {
    List<Participant> retVal = new ArrayList<Participant>();
    if( !this.p1.getParticipant1().isGhost() ) {
      retVal.add(this.p1.getParticipant1());
    }
    if( !this.p1.getParticipant2().isGhost() ) {
      retVal.add(this.p1.getParticipant2());
    }
    if( !this.p2.getParticipant1().isGhost() ) {
      retVal.add(this.p2.getParticipant1());
    }
    if( !this.p2.getParticipant2().isGhost() ) {
      retVal.add(this.p2.getParticipant2());
    }
    
    return retVal;
  }
  
  

  @Override
  public int hashCode() {
    return Objects.hash(p1, p2);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Game other = (Game) obj;
    return Objects.equals(p1, other.p1) && Objects.equals(p2, other.p2);
  }

  public Pairing getP1() {
    return p1;
  }

  public void setP1(Pairing p1) {
    this.p1 = p1;
  }

  public Pairing getP2() {
    return p2;
  }

  public void setP2(Pairing p2) {
    this.p2 = p2;
  }
}
