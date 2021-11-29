import java.util.SortedSet;
import java.util.TreeSet;

public class SortedSetTest implements Comparable<SortedSetTest>{
  public int id;
  public int val;

  public SortedSetTest( int id, int val ) {
    this.id = id;
    this.val = val;
  }

  public int compareTo( SortedSetTest sst ) {
    return this.equals(sst) ? 0 : (this.val <= sst.val ? -1 : 1);
  }

  public boolean equals( Object o ) {
    return this.id == ((SortedSetTest)o).id;
  }

  public String toString() {
    return "[" + this.id + "," + this.val + "]";
  }

  public int hashCode() {
    return Integer.valueOf( this.id ).hashCode();
  }

  public static void main( String [] args ) {
    SortedSetTest sst1 = new SortedSetTest( 1, 34 );
    SortedSetTest sst2 = new SortedSetTest( 2, 33 );

    SortedSet<SortedSetTest> mySet = new TreeSet<SortedSetTest>();

    mySet.add( sst1 );
    mySet.add( sst2 );

    System.out.println( mySet.first() + ", " + mySet.size() );

    mySet.add( new SortedSetTest( 3, 7 ) );
    System.out.println( mySet.first() + ", " + mySet.size() );
    sst1.val = 6;
    sst1.id = 5;
    mySet.remove( sst1 );
    mySet.add( sst1 );
    System.out.println( mySet.first() + ", " + mySet.size() );
    System.out.println( mySet );
  }
}
