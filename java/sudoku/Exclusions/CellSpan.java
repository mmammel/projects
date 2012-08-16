public abstract class CellSpan extends CellCollection
{
  public CellSpan( int [] indices, Cell [] board )
  {
    super(indices,board);
  }
  
  public abstract int getId();
}