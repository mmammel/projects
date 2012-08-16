import java.util.*;

public class TypedArray
{
  List myarray = new ArrayList();

  public void addString( String str )
  {
    myarray.add(str);
  }

  public String [] getStrings()
  {
    String [] retval = new String [ this.myarray.size() ];
    return (String[])this.myarray.toArray(retval);
  }

  public static void main( String [] args )
  {
    String [] vals = null;
    String temp = null;

    TypedArray TA = new TypedArray();
    TA.addString("JOAMAM");
    TA.addString("FOO");
    TA.addString("BAR");

    vals = TA.getStrings();

    for( int i = 0; i < vals.length; i++ )
    {
      temp = vals[i];
      System.out.println( temp );
    }
  }

}
