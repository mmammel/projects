public class Driver
{
  public static void main( String [] args )
  {
    OuterOne one = new OuterOne();
    OuterOne two = new OuterTwo();

    one.printOuterInfo();
    one.printInnerInfo();
    two.printOuterInfo();
    two.printInnerInfo();
  }
}
