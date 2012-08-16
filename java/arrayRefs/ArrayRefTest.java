public class ArrayRefTest
{

  public ArrayRefTest [] array = null;
  public ArrayRefTest myRef = null;
  public String myVal = null;

  public ArrayRefTest(String val)
  {
    array = new ArrayRefTest [1];
    array[0] = myRef;
    myVal = val;
  }

  public void setRef( ArrayRefTest art )
  {
    myRef = art;
  }

  public String getMyRefVal()
  {
    return array[0].getMyVal();
  }

  public String getMyVal()
  {
    return myVal;
  }

  public static void main( String [] args )
  {
    ArrayRefTest parent = new ArrayRefTest( "PARENT" );
    ArrayRefTest child = new ArrayRefTest( "CHILD" );

    parent.setRef(child);

    System.out.println(parent.getMyRefVal());
  }


}
