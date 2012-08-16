public class OuterTwo extends OuterOne
{

  protected void printOuterInfo()
  {
    System.out.println( "OuterTwo" );
  }

  /*
     Uncommented, the call to OuterTwo's printInnerInfo will print
     the string in this file.  Commented, we will print OuterOne's
     inner class string.

  protected void printInnerInfo()
  {
    InnerClass inner = new InnerClass();
    inner.printInfo();
  }
   */

  protected class InnerClass extends OuterOne.InnerClass
  {
    protected void printInfo()
    {
      System.out.println( "I'm the OuterTwo's inner class" );
    }
  }

}
