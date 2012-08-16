
public class ClassForNameTest
{
  public static void main( String [] args )
  {
      Class clazz = null;
      Object obj = null;
      String classname = "incertrt.LeakSeekerDataSource";
      SomeClass [] someClasses = new SomeClass[5];

      try
      {
        System.out.println( "Custom class array: " + someClasses.getClass().getName() );
        clazz = Class.forName( classname );
        obj = clazz.newInstance();
      }
      catch( ClassNotFoundException cnfe )
      {
        System.out.println( "Class: \"" + classname + "\" not found:" + cnfe.toString());
      }
      catch( Exception e )
      {
        System.out.println("Error obtaining instance of class: \"" + classname + "\"" +  e.toString());
      }
  }

  public class SomeClass
  {
    private String name;
  
    public SomeClass() { }
  }

}
