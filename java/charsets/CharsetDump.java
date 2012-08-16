import java.util.*;
import java.nio.charset.*;

class CharsetDump
{

  public static void main( String [] args )
  {
    SortedMap<String,Charset> sets = Charset.availableCharsets();

    for( String key : sets.keySet())
    {
      System.out.println( key );
    }
  }


}
