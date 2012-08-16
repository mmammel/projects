

public class Child extends Parent
{
  protected static void printSomething()
  {
    System.out.println( "In the Child" );
  }

  public static void main( String [] args )
  {
    Child.printSomething();
    Parent.printSomething();
    
    /**
     * After running this, comment out or delete the 
     * the child's printSomething method 
     */
    Parent p = new Parent();
    Parent c = new Child();

    p.printSomething();
    c.printSomething();
 
  }

}
