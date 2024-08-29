import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Pairing {
  private Participant participant1;
  private Participant participant2;
  
  public Pairing( Participant p1, Participant p2) {
    this.participant1 = p1;
    this.participant2 = p2;
  }
  
  public boolean isExclusive( Pairing p2 ) {
    return this.participant1.getId() != p2.getParticipant1().getId() &&
           this.participant1.getId() != p2.getParticipant2().getId() &&
           this.participant2.getId() != p2.getParticipant1().getId() &&
           this.participant2.getId() != p2.getParticipant2().getId();
  }
  
  public String getIdString() {
    String retVal = null;
    if( this.participant1.getId() <= this.participant2.getId()) {
      retVal = ""+this.participant1.getId()+""+this.participant2.getId();
    } else {
      retVal = ""+this.participant2.getId()+""+this.participant1.getId();      
    }
    
    return retVal;
  }
  
  public Set<Integer> getIdxSet() {
    Set<Integer> retVal = new HashSet<Integer>();
    retVal.add(this.participant1.getId());
    retVal.add(this.participant2.getId());
    return retVal;
  }
  
  public boolean hasGhost() {
    return this.participant1.isGhost() || this.participant2.isGhost();
  }
  
  public String toString() {
    String retVal = null;
    if( this.participant1.getId() <= this.participant2.getId()) {
      retVal = ""+this.participant1.getId()+" and "+this.participant2.getId();
    } else {
      retVal = ""+this.participant2.getId()+" and "+this.participant1.getId();      
    }
    
    return retVal;
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(this.participant1.getId(), this.participant1.getId());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Pairing other = (Pairing) obj;
    return participant1.getId() == other.participant1.getId() && participant2.getId() == other.participant2.getId();
  }

  public void collectIds( Set<Integer> idset ) {
    if( idset != null ) {
      idset.add(this.participant1.getId());
      idset.add(this.participant2.getId());
    }
  }
  
  public Participant getParticipant1() {
    return participant1;
  }

  public void setParticipant1(Participant participant1) {
    this.participant1 = participant1;
  }

  public Participant getParticipant2() {
    return participant2;
  }

  public void setParticipant2(Participant participant2) {
    this.participant2 = participant2;
  }
}
