public class OptimalSolution {

  public OptimalSolution(String str, int val)
  {
    this.buffer = new StringBuilder(str);
    this.value += val;
  }

  public OptimalSolution(OptimalSolution os)
  {
    this.buffer = new StringBuilder();
    this.absorb(os);
  }

  public void absorb( OptimalSolution os )
  {
    this.buffer.append(os.buffer);
    this.value += os.value;
  }

  public String toString()
  {
    return "" + this.value + ": " + this.buffer.toString();
  }

  public StringBuilder buffer = null;
  public int value = 0;
}