import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.math.BigInteger;

public class Driver
{

  private Map<BigInteger,Bucket> bucketMap;
  private List<Bucket> bucketList;
  private List<String> wordList;

  public Driver() {
    this.bucketMap = new HashMap<BigInteger, Bucket>();
    this.bucketList = new ArrayList<Bucket>();
    this.wordList = new ArrayList<String>();
  }

  public void addWord( String w ) {
    wordList.add( w.toUpperCase() );
  }

  public void processWords() {
    this.wordList.sort( new Comparator<String>() {
       public int compare( String s1, String s2 ) {
         int retVal = s2.length() - s1.length();
         if( retVal == 0 ) {
           retVal = s1.compareTo( s2 );
         }

         return retVal;
       } 
    });

    //for( String w : this.wordList ) {
    //  System.out.println( w );
    //}

    int curr = 0;
    int total = this.wordList.size();

    for( String w : this.wordList ) {
      if( ++curr % 1000 == 0 ) System.out.println( "Processing " + curr + " of " + total + ", have : " + this.bucketMap.size() + " buckets." );
      Bucket newBucket = null;
      BigInteger tempPrime = Letter.getWordKey( w );
      Bucket existingBucket = null;
      if( (existingBucket = this.bucketMap.get( tempPrime )) == null ) {
        // new bucket
        newBucket = new Bucket(w);
        this.bucketMap.put( tempPrime, newBucket );
        // see if we match any existing buckets
        for( Bucket b : this.bucketList ) {
          if( b.test( newBucket) ) {
            newBucket.addParent( b );
            for( Bucket p : b.getParents() ) {
              newBucket.addParent(p);
            }
            break;
          }
        }
        this.bucketList.add(0, newBucket );
      } else {
        // no new bucket needed, but add this word to all of the existing buckets parents.
        for( Bucket b : existingBucket.getParentBuckets() ) {
          b.addSubWord(w);
        }
      }
    }
  }

  public static void main( String [] args )
  {
    Driver D = new Driver();
    String fname = args[0];
    BufferedReader input_reader = null;
    String input_str = "";

    try
    {
      input_reader = new BufferedReader( new FileReader ( fname ) );
      while( (input_str = input_reader.readLine()) != null )
      {
        D.addWord( input_str.trim() );
      }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }

    D.processWords();
  }
}

