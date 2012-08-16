import java.util.EnumSet;

public enum MyEnum {
  A,
  B,
  C,
  D,
  E,
  F,
  G;

  public static int getMaskValue( MyEnum e )
  {
    return (1 << e.ordinal());
  }

  public static EnumSet<MyEnum> getSetFromMask( int mask )
  {
    EnumSet<MyEnum> retVal = EnumSet.allOf(MyEnum.class);
    for( MyEnum e : retVal )
    {
      if( ((1 << e.ordinal()) & mask) == 0 ) retVal.remove(e);
    }

    return retVal;
  }
}
