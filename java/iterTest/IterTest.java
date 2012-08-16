import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class IterTest
{
  List los = new ArrayList();

  public IterTest()
  {
    los.add( new LittleObj( "jomama" ) );
    los.add( new LittleObj( "yomama" ) );
    los.add( new LittleObj( "mammel" ) );

    LittleObj temp = null;

    for( Iterator iter = los.iterator(); iter.hasNext(); )
    {
      temp = (LittleObj)iter.next();
      temp.printName();
    } 
  }

  public void modifyList()
  {
    LittleObj temp = null;

    for( Iterator iter = los.iterator(); iter.hasNext(); )
    {
      temp = (LittleObj)iter.next();
      temp.name = temp.name.toUpperCase();
    }
  }

  public void printList()
  {
    LittleObj temp = null;

    for( Iterator iter = los.iterator(); iter.hasNext(); )
    {
      temp = (LittleObj)iter.next();
      temp.printName();
    }
  }

  public class LittleObj
  {
    public String name = null;

    LittleObj( String n )
    {
      name = n;
    }

    public void printName()
    {
      System.out.println( "Name is: " + name );
    }
  }

  public static void main( String [] args )
  {
    IterTest it = new IterTest();
    it.modifyList();
    it.printList();
  }
}
