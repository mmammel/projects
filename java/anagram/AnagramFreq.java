import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.Arrays;

public class AnagramFreq
{
  private Map<String,String> fullWordIndex;
  private Map<String,Integer> freqs;

  public AnagramFreq() {
    this.fullWordIndex = new HashMap<String,String>();
    this.freqs = new TreeMap<String,Integer>();
  }

  public void processWord( String word ) {
    byte [] strBytes = word.getBytes();
    Arrays.sort(strBytes);
    String key = new String(strBytes);
    Integer tempInt = 0;
    String tempStr = null;
    if( (tempInt = this.freqs.get(key)) != null ) {
      this.freqs.put( key, tempInt + 1 );
    } else {
      this.freqs.put( key, 1 );
    }

    if( (tempStr = this.fullWordIndex.get(key)) != null ) {
      this.fullWordIndex.put( key, tempStr+","+word );
    } else {
      this.fullWordIndex.put( key, word );
    }
  }

  public void printSuperAnagrams() {
    int n = 0;
    for( String key : this.freqs.keySet() ) {
      n = this.freqs.get(key);
      if( n >= key.length() ) {
        System.out.println( "[" + n + "] " + key + " : " + this.fullWordIndex.get(key) );
      }
    }
  }
  
  public static void main( String [] args )
  {
    String fname = args[0];
    BufferedReader input_reader = null;
    String input_str = "";

    AnagramFreq AF = new AnagramFreq();

    try
    {
      input_reader = new BufferedReader( new FileReader ( fname ) );

      while( (input_str = input_reader.readLine()) != null )
      {
        AF.processWord( input_str );
      }

      AF.printSuperAnagrams();

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }

  
}

