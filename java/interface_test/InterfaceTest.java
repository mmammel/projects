public class InterfaceTest extends Interface2
{
  public void doSomething1()
  {
    System.out.println( "did something 1" );
  }

  public void doSomething2()
  {
    System.out.println( "did something 2" );
  }

  public void doAnother1()
  {
    System.out.println( "did another 1" );
  }

  public void doAnother2()
  {
    System.out.println( "did another 2" );
  }

  public static void main( String [] args )
  {
    InterfaceTest IT = new InterfaceTest();
    IT.doSomething1();
    IT.doAnother2();
  }
}
