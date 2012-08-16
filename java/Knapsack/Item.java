public class Item
{
  public Item( int w, int v ) { this.weight = w; this.value = v; }

  public int weight;
  public int value;

  public String toString()
  {
    return "["+this.weight+",$"+this.value+"]";
  }
}