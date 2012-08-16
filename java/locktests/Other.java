public class Other
{
  private int count = 0;

  private static Other lockObj = new Other();

  private Other() {}

  private void bumpCount()
  {
    count++;
  }

  public int getCount()
  {
    return count;
  }
    
  public static Other getLockObject()
  {
    lockObj.bumpCount();
    return lockObj;
  } 
}
