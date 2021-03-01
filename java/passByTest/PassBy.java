import java.util.*;

public class PassBy
{
  public void noAssign( String str, int n, List<String> l )
  {
    str = str.substring( 0, 3 );
    n += 2;
    l = new ArrayList<String>();
    l.add("inNoAssign");
  }

  public void assign( String str, int n, List<String> l )
  {
    String retVal = str;
    retVal = retVal.substring( 0, 2 );
    int n2 = n;
    n2++;
    List<String> foo = l;
    foo.add("inassign");
  }

  public static void main( String [] args )
  {
    PassBy PB = new PassBy();
    List<String> l1 = new ArrayList<String>();
    l1.add( "foo" );
    String testStr = "Mammel";
    int n1 = 1;
    List<String> l2 = new ArrayList<String>();
    l2.add( "bar" );
    String testStr2 = "Mammel";
    int n2 = 1;
    PB.noAssign( testStr, n1, l1 );
    System.out.println( testStr + "," + n1 + "," + l1 );
    PB.assign( testStr2, n2, l2 );
    System.out.println( testStr2 + "," + n2 + "," + l2 );
  }


}
