import java.util.*;

public class Person {
  private String firstName = null;
  private String lastName = null;
  private int age = -1;

  public Person( String f, String l, int age ) {
    this.firstName = f;
    this.lastName = l;
    this.age = age;
  }

  public Person( String [] array ) {
    if( array != null && array.length >= 3 ) {
      this.firstName = array[0];
      this.lastName = array[1];
      this.age = Integer.parseInt( array[2] );
    } else {
      throw new RuntimeException( "Bogus instantiator array" );
    }
  }

  public String toString() {
    return "[" + this.firstName + "," + this.lastName + "," + this.age + "]";
  }

  public static final class DefaultComparator implements Comparator<Person> {
    public int compare(Person o1, Person o2) {
      return new Integer(o1.age).compareTo(o2.age);
    }
  } 

  public static final class FirstComparator implements Comparator<Person> {
    public int compare(Person o1, Person o2) {
      int retVal = 0;
      if( (retVal = o1.firstName.compareTo(o2.firstName)) == 0) {
        retVal = new Integer(o1.age).compareTo(o2.age);
      }

      return retVal;
    }
  } 
  
  public static final class FirstLastComparator implements Comparator<Person> {
    public int compare(Person o1, Person o2) {
      int retVal = 0;
      if( (retVal = o1.firstName.compareTo(o2.firstName)) == 0) {
        if( (retVal = o1.lastName.compareTo(o2.lastName)) == 0) {
          retVal = new Integer(o1.age).compareTo(o2.age);
        }
      }

      return retVal;
    }
  } 

  public static final class LastAgeComparator implements Comparator<Person> {
    public int compare(Person o1, Person o2) {
      int retVal = 0;
      if( (retVal = o1.lastName.compareTo(o2.lastName)) == 0) {
        retVal = new Integer(o1.age).compareTo(o2.age);
      }
      return retVal;
    }
  } 

  public static final String [][] PEOPLE = {
    {"Max","Mammel","38"},
    {"Sophia","Mammel","6"},
    {"Jevne","Mammel","36"},
    {"Brian","Hatton","42"},
    {"Stephanie","Hatton","32"}
  };
 

  public static void main( String [] args ) {
    List<Person> peeps = new ArrayList<Person>();

    for( String [] p : PEOPLE ) {
      peeps.add( new Person( p ) );
    }

    System.out.println( "Default: " );
    Collections.sort(peeps,new Person.DefaultComparator() );
    printList( peeps );
    System.out.println( "First: " );
    Collections.sort(peeps,new Person.FirstComparator() );
    printList( peeps );
    System.out.println( "FirstLast: " );
    Collections.sort(peeps,new Person.FirstLastComparator() );
    printList( peeps );
    System.out.println( "LastAge: " );
    Collections.sort(peeps,new Person.LastAgeComparator() );
    printList( peeps );
    
  }

  public static void printList( List<Person> l ) {
    for( Person p : l ) {
      System.out.println( "  " + p );
    }
  }
}
