public class Box extends CellCollection
{
  public Box( int [] indices, Cell [] board )
  {
    super(indices,board);
  }

  public int getId()
  {
    int idx = this.mCells[0].getId();
    int temp = idx / 9;
    return temp + ((idx%9)/3);
  }
}