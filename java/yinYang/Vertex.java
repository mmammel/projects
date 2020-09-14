import java.util.EnumSet;

/*

  +---+---+
  | 0 | 1 |
  +---V---+
  | 3 | 2 |
  +---+---+
*/
public class Vertex {
  private Cell [] cells; // [ TOPLEFT, TOPRIGHT, BOTTOMRIGHT, BOTTOMLEFT ]
  
  public Vertex() {
    this.cells = new Cell [4];
  }
  
  public boolean valid() {
    boolean retVal = true;
    EnumSet<CellVal> cellVals = EnumSet.of(this.cells[0].value(), this.cells[1].value(),
                                           this.cells[2].value(), this.cells[3].value());
    if(cellVals.size() == 1 /*&& !cellVals.contains(CellVal.BLANK)*/ ) {
      retVal = false;
    }
    return retVal;
  }
  
  public void setTopLeft( Cell c ) {
    this.setCell(c, 0);
  }
  public void setTopRight( Cell c ) {
    this.setCell(c, 1);
  }
  public void setBottomRight( Cell c ) {
    this.setCell(c, 2);
  }
  public void setBottomLeft( Cell c ) {
    this.setCell(c, 3);
  }
  private void setCell(Cell c, int idx) {
    this.cells[idx] = c;
  }
}
