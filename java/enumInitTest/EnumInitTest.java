public class EnumInitTest
{
  public TestEnum testEnum;

  public String toString()
  {
    return "" + this.testEnum;
  }

  public void setTestEnum( TestEnum val )
  {
    this.testEnum = val;
  }

  public void setTestEnum( int val )
  {
    this.testEnum = (TestEnum)val;
  }

  public enum TestEnum
  {
    ONE,
    TWO,
    THREE
  }

  public static void main( String [] args )
  {
    EnumInitTest EIT = new EnumInitTest();

    System.out.println( EIT );

    EIT.setTestEnum( TestEnum.TWO );

    System.out.println( EIT );

    EIT.setTestEnum( 2 );

    System.out.println( EIT );
  }

}
