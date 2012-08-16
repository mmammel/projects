import java.util.EnumSet;

public class EnumSetTest
{
  public static void main( String [] args )
  {
    EnumSet<MyEnum> set1 = EnumSet.of( MyEnum.A, MyEnum.D );
    EnumSet<MyEnum> set2 = MyEnum.getSetFromMask(118);

    System.out.println( "Set 1: " + set1 + "(" + Integer.toBinaryString(getMask(set1)) + "), Set 2: " + set2 + "(" + Integer.toBinaryString(getMask(set2)) + ")" );
  }  

  public static int getMask( EnumSet<MyEnum> set )
  {
    int retVal = 0;
    for( MyEnum e : set ) retVal |= MyEnum.getMaskValue(e);
    return retVal;
  }
}
