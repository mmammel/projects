import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

public class DistinctPartitions {

  private int numObjects = 0;
  private int partitionSize = 0;

  private static String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  private String [] objects = null; 
  private int [] pointers = null;
  private Set<Integer> pointed = null;

  public DistinctPartitions( int objects, int partitionSize ) {
    this.numObjects = objects;
    this.partitionSize = partitionSize;

    this.objects = new String [objects];

    for( int i = 0; i < objects; i++ ) {
      this.objects[i] = ""+ALPHABET.charAt(i);
    }

    this.pointers = new int [ objects ];
    this.pointed = new HashSet<Integer>();
  }


  public void process() {
    this.processInner(1, 0, 0);
  }


  public void processInner( int partitionNum, int pointerIdx, int start ) {
    if( partitionNum > (this.numObjects / this.partitionSize ) ) {
      this.printSortedPartitions();
    } else {
      int idx = (partitionNum - 1) * 2 + pointerIdx;
      for( int i = start; i < this.numObjects; i++ ) {
        if( !this.pointed.contains( i ) ) {
          // try it.
          this.pointers[ idx ]  = i;
          this.pointed.add( i );
          if( pointerIdx == (this.partitionSize - 1) ) {
            this.processInner( partitionNum + 1, 0, 0 );
          } else {
            this.processInner( partitionNum, pointerIdx + 1, i+1 );
          }

          this.pointed.remove(this.pointers[ idx ]);
          this.pointers[idx] = -1;
        }
      }
    }    
  }

  private void printPartitions() {
    for( int i = 0; i < this.pointers.length; i++ ) {
      System.out.print( this.objects[this.pointers[i]] );
      if( (i+1) % this.partitionSize == 0 ) System.out.print( " " );
    }
    System.out.println("");
  }

  private void printSortedPartitions() {
    String [] parts = new String [ (this.numObjects / this.partitionSize) ];
    for( int i = 0; i < parts.length; i++ ) parts[i] = "";
    int idx = 0;
    for( int i = 0; i < this.pointers.length; i++ ) {
      parts[idx] += this.objects[this.pointers[i]];
      if( (i+1) % this.partitionSize == 0 ) idx++; 
    }

    Arrays.sort( parts );
    for( int i = 0; i < parts.length; i++ ) {
      System.out.print( parts[i] );
      System.out.print( " " );
    }
    System.out.println("");
  }

  public static void main( String [] args ) {
    int o = 0;
    int p = 0;
    try {
      o = Integer.parseInt( args[0] );
      p = Integer.parseInt( args[1] );

      if( o > 26 ) {
        System.err.println( "Cannot have more than 26 objects" );
        System.exit(1);
      }

      if( o % p != 0 ) {
        System.err.println( "Partition size must be a factor of numObjects" );
        System.exit(1);
      }
    } catch( Exception e ) {
      System.err.println("Bad arguments");
      System.exit(1);
    }

    DistinctPartitions D = new DistinctPartitions( o, p );
    D.process();
  }
}
