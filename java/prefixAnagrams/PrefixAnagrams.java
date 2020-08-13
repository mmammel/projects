import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.Arrays;

public class PrefixAnagrams
{
  private String prefix;
  private int prefixLen;
  private Map<String,List<String>> index;
  private Set<String> anagramKeys;

  public PrefixAnagrams( String prefix ) {
    this.prefix = prefix;
    this.prefixLen = this.prefix.length();
    this.index = new HashMap<String,List<String>>();
    this.anagramKeys = new TreeSet<String>();
  }

  public void indexWord( String word ) {
    if( word.indexOf( this.prefix ) == 0 && word.length() > this.prefixLen ) {
      //System.out.println( "Found a candidate: " + word );
      List<String> tempList = null;
      byte [] tempArray = word.getBytes();
      Arrays.sort( tempArray, this.prefixLen, word.length() - 1 );
      String key = new String(tempArray);
      //System.out.println( "Key: " + key );
      if( (tempList = this.index.get( key )) == null ) {
        tempList = new ArrayList<String>();
        this.index.put( key, tempList );
      } else {
        // this is at least the second time we've seen this key.
        this.anagramKeys.add( key );
      }

      tempList.add( word );
    }
  }

  public void printAnagrams() {
    List<String> tempList = null;
    for( String k : this.anagramKeys ) {
      tempList = this.index.get( k );
      for( String w : tempList ) {
        System.out.print( w + " " );
      }
      System.out.println("");
    }
  }

  public static void main( String [] args )
  {
    String fname = args[0];
    String prefix = args[1];
    BufferedReader input_reader = null;
    String input_str = "";

    PrefixAnagrams PA = new PrefixAnagrams(prefix);

    try
    {
      input_reader = new BufferedReader( new FileReader ( fname ) );

      while( (input_str = input_reader.readLine()) != null )
      {
        PA.indexWord( input_str );
      }

      PA.printAnagrams();

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

