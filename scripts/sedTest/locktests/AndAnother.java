public class AndAnother
{
  private int count = 0;

  private static AndAnother lockObj = new AndAnother();

  private AndAnother() {}

  private void bumpCount()
  {
    count++;
  }

  public int getCount()
  {
    return count;
  }
    
  public static AndAnother getLockObject()
  {
    lockObj.bumpCount();
    return lockObj;
  } 
}
