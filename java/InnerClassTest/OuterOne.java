public class OuterOne
{

  protected void printOuterInfo()
  {
    System.out.println( "OuterOne" );
  }

  protected void printInnerInfo()
  {
    InnerClass inner = new InnerClass();
    inner.printInfo();
  }

  protected class InnerClass
  {
    protected void printInfo()
    {
      System.out.println( "I'm the OuterOne's inner class" );
    }
  }

}
