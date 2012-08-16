import java.util.*;

public class NoSuchElementTest
{
  private Set<String> testSet = null;

  public NoSuchElementTest()
  {
    testSet = new HashSet<String>();
  }

  public String getFirstElement()
  {
    Iterator<String> iterator = this.testSet.iterator();
    return iterator.next();
  }

  public void addElement( String val )
  {
    this.testSet.add( val );
  }

  public static void main( String [] args )
  {
    NoSuchElementTest nset = new NoSuchElementTest();

    System.out.println( "First element (empty map): " + nset.getFirstElement() );
    nset.addElement( "bar" );
    nset.addElement( "mama" );
    nset.addElement( "Mammel" );
    System.out.println( "First element (non-empty map a): " + nset.getFirstElement() );
    System.out.println( "First element (non-empty map b): " + nset.getFirstElement() );
    System.out.println( "First element (non-empty map c): " + nset.getFirstElement() );
    System.out.println( "First element (non-empty map d): " + nset.getFirstElement() );
  }

}