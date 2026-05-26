import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Tupler<T> {
  private T [] items;
  private TupleTester<T> tester;

  public Tupler( T [] items ) {
    this.items = items;
    this.tester = null;
  }

  public Tupler( T [] items, TupleTester<T> tester ) {
    this.items = items;
    this.tester = tester;
  }

  public List<Set<T>> getNTupleSets( int n ) {
    List<Set<T>> retVal = null;
    Set<T> temp = null;
    List<T []> tuples = this.getNTupleInner(n);

    if( tuples != null ) {
      retVal = new ArrayList<Set<T>>();
      for( T[] tuple : tuples ) {
        temp = new HashSet<T>();
        for( T item : tuple ) {
          temp.add(item);
        }
        retVal.add(temp);
      }
    }

    return retVal;
  }

  public List<T []> getNTupleArrays( int n ) {
    return this.getNTupleInner( n );
  }

  private List<T []> getNTupleInner( int n ) {
    List<T []> retVal = null;
    T [] temp;
    boolean moreTuples = true;
    if( n == this.items.length ) {
      retVal = new ArrayList<T []>();
      retVal.add( Arrays.copyOf(this.items, this.items.length ) );
    } else if( n < this.items.length ) {
      retVal = new ArrayList<T []>();

      // initialize the index pointers.
      int [] pointers = new int [n];
      for( int i = 0; i < pointers.length; i++ ) {
        pointers[i] = i;
      }

      while( moreTuples ) {
        if( pointers[0] == (this.items.length - pointers.length) ) {
          moreTuples = false;
        } else {
          for( int i = 1; i < pointers.length; i++ ) {
            if( pointers[i] == (this.items.length - (pointers.length - i)) ) {
              // this pointer is at the end of the line, bump the previous one.
              pointers[i-1] += 1;

              // now reset all following pointers.
              for( int j = i; j < pointers.length; j++ ) {
                pointers[j] = pointers[j-1] + 1;
              }
              break;
            }
          }

          while( pointers[pointers.length - 1] < this.items.length ) {
            temp = Arrays.copyOf(this.items, n);
            for( int j = 0; j < pointers.length; j++ ) {
              temp[j] = this.items[ pointers[j] ];
            }

            if( this.tester == null || this.tester.validTuple(temp) ) {
              retVal.add( temp );
              System.out.println( "Found " + retVal.size() + " tuples");
            }
            pointers[pointers.length - 1] += 1;
          }

          pointers[pointers.length - 1] = this.items.length - 1;
        }
      }
    }

    return retVal;
  }

  public static void main( String [] args ) {

    int arraySize = 5;
    int tupleSize = 2;
    try {
      arraySize = Integer.parseInt( args[0] );
      tupleSize = Integer.parseInt( args[1] );
    } catch( Exception e ) {
      arraySize = 5;
      tupleSize = 2;
    }

    String [] baseArray = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

    String [] strArray = Arrays.copyOf( baseArray, arraySize );

    Tupler<String> strTupler = new Tupler<String>( strArray );
    List<String []> tuples = strTupler.getNTupleArrays(tupleSize);

    System.out.println( "All " + tupleSize + "-tuples of " + Arrays.toString(strArray) + ":");

    for( String [] tuple : tuples ) {
      System.out.println( Arrays.toString(tuple) );
    }

    System.out.println( "(" + tuples.size() + " " + tupleSize + "-tuples)");

    Tupler<String[]> tupleTupler = new Tupler<String []>( tuples.toArray(new String [0][]), new TupleTester<String[]>() {
      public boolean validTuple( String [][] tuple ) {
        // see if this array of string arrays has equal numbers of all characters.  We can assume the tuples are the same length.
        boolean retVal = true;
        Integer tempCount = null;
        Map<String,Integer> counts = new HashMap<String,Integer>();
        for( int i = 0; i < tuple.length; i++ ) {
          for( int j = 0; j < tuple[i].length; j++ ) {
            if( (tempCount = counts.get(tuple[i][j])) == null ) {
              counts.put( tuple[i][j], 1);
            } else {
              counts.put( tuple[i][j], tempCount + 1);
            }
          }
        }

        int last = -1, check = 0;

        for( String k : counts.keySet() ) {
          check = counts.get(k);
          if( last != -1 && check != last ) {
            retVal = false;
            break;
          } else {
            last = check;
          }
        }
        return retVal;
      }
    });

    List<String[][]> tupleTuples = tupleTupler.getNTupleArrays(arraySize);
    for( String [][] balanced : tupleTuples ) {
      System.out.println("Balanced: ");
      for( String [] arr : balanced ) {
        System.out.println( Arrays.toString(arr));
      }
    }
  }
}
