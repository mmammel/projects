package org.mjm.domino;

public class DominoDescriptor {
  private Domino domino;
  private DominoPosition position;
  
  public DominoDescriptor( Domino d, DominoPosition p ) {
    this.domino = d;
    this.position = p;
  }
  
  public Domino getDomino() {
    return domino;
  }
  
  public void setDomino(Domino domino) {
    this.domino = domino;
  }
  
  public DominoPosition getPosition() {
    return position;
  }
  
  public void setPosition(DominoPosition position) {
    this.position = position;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder();
    
    if( this.position == DominoPosition.Horizontal ) {
      sb.append("+===+===+\n");
      sb.append("| ").append(this.domino.val(0)).append(" | ").append(this.domino.val(1) ).append(" |\n");
      sb.append("+===+===+\n");
    } else {
      sb.append("+===+\n");
      sb.append("| ").append(this.domino.val(0)).append(" |\n");
      sb.append("+===+\n");
      sb.append("| ").append(this.domino.val(1) ).append(" |\n");
      sb.append("+===+\n");
    }
    
    return sb.toString();
  }
}
