public class Driver
{
  public void callDoSomething( MyInterface myint )
  {
    System.out.println( "Int" );
    myint.doSomething();
  }

  public void callDoSomething( MyObject myobj )
  {
    System.out.println( "Obj" );
    myobj.doSomething();
  }

  public void callDoSomethingElse( MyObject myobj )
  {
    myobj.doSomethingElse();
  }


  public static void main( String [] args )
  {
    Driver D = new Driver();
    MyInterface myint = new MyObject();
    MyObject myobj = new MyObject();
    System.out.println( "Status: " + myobj.getStatus() );
    myobj.setStatus( MyInterface.Status.STATE3 );
    System.out.println( "Status: " + myobj.getStatus() );

    D.callDoSomething( myint );
    D.callDoSomething( myobj );
    //error: D.callDoSomethingElse( myint );
    D.callDoSomethingElse( myobj );
  }
}

