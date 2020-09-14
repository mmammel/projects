
public class RowNeighborDescriptor {
  private int id;
  private boolean ceilingNeighbor;
  private boolean floorNeighbor;
  
  public RowNeighborDescriptor( int id ) {
    this.id = id;
  }
  
  public int getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  
  public boolean isCeilingNeighbor() {
    return ceilingNeighbor;
  }

  public void setCeilingNeighbor(boolean ceilingNeighbor) {
    this.ceilingNeighbor = ceilingNeighbor;
  }

  public boolean isFloorNeighbor() {
    return floorNeighbor;
  }
  public void setFloorNeighbor(boolean floorNeighbor) {
    this.floorNeighbor = floorNeighbor;
  }
  
  public String toString() {
    return "["+this.id+","+this.ceilingNeighbor+","+this.floorNeighbor+"]";
  }
}
