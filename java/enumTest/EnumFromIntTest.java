public class EnumFromIntTest
{

  static private enum NumEnum
  {
    ZERO,
    ONE,
    TWO,
    THREE
  }


  public static void main(String [] args )
  {
    try
    {
      NumEnum myenum = Enum.valueOf(NumEnum.class,args[0]);
      System.out.println( "My Enum: " + myenum.name() );
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
    }
  }
}
