public class ChildClass extends ParentClass
{
  public static void main( String [] args )
  {
    ChildClass CC = new ChildClass();
    CC.setFoo( "jomama" );
    System.out.println( CC.getFoo() );
  }
}
