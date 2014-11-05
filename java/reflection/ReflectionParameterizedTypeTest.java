import java.util.*;

public class ReflectionParameterizedTypeTest {

  public static void main( String [] args ) {
    List<String> list = new ArrayList<String>();
    printType( list.getClass() );

  }

  public static void printType( Class<?> clazz ) {
    if( isCollection(clazz) ) {
      System.out.println( "Is collection" );
    }
    System.out.println( clazz );
  }

  public static boolean isCollection( Class<?> clazz ) {
    boolean retVal = false;
    Class[] interfaces = clazz.getInterfaces();
    for( Class c : interfaces ) {
      System.out.println( "Interface: " + c.getName() );
      if( c.getName().equals( "java.util.Collection" ) ) {
        retVal = true;
      }
    }

    return retVal;
  }

}
