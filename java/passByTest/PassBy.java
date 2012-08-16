public class PassBy
{
  public String noAssign( String str )
  {
    str = str.substring( 0, 3 );
    return str;
  }

  public String assign( String str )
  {
    String retVal = str;
    retVal = retVal.substring( 0, 3 );
    return retVal;
  }

  public static void main( String [] args )
  {
    PassBy PB = new PassBy();
    String testStr = "Mammel";
    String testStr2 = "Mammel";
    testStr = PB.noAssign( testStr );
    System.out.println( testStr );
    testStr2 = PB.assign( testStr2 );
    System.out.println( testStr2 );
  }


}
