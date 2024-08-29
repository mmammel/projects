
public class Participant {
  private int id;
  private boolean isGhost;
  private String name;
  
  public Participant( int id ) {
    this(id, false);
  }
  
  public Participant( int id, boolean ghost ) {
    this.id = id;
    this.isGhost = ghost;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public boolean isGhost() {
    return isGhost;
  }

  public void setGhost(boolean isGhost) {
    this.isGhost = isGhost;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
}
