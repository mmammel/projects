public class Slice
{
  public long startTime = 0L;
  public long endTime = 0L;

  public String host = null;
  public String jvm = null;

  public Slice( long st, long et, String h, String j )
  {
    startTime = st;
    endTime = et;
    host = h;
    jvm = j;
  }

  public String toString()
  {
    StringBuffer buff = new StringBuffer();

    buff.append( "[" + startTime + "," + endTime + "," + host + "," + jvm + "]" );

    return buff.toString();
  }
}
