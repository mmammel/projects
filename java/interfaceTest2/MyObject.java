public class MyObject implements MyInterface
{
  private MyInterface.Status status = MyInterface.Status.STATE1;

  public void doSomething()
  {
    System.out.println( "Tweedle Dee" );
  }

  public void doSomethingElse()
  {
    System.out.println( "Tweedle Dum" );
  }

  public MyInterface.Status getStatus()
  {
    return this.status;
  }

  public void setStatus( MyInterface.Status status )
  {
    this.status = status;
  }

}
