import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SetFreqs
{
  private int maxWordLen;
  private Map<String,int[]> seqFreqs = null;
  
  public SetFreqs(int maxWordLen) {
    this.seqFreqs = new HashMap<String, int[]>();
    this.maxWordLen = maxWordLen;
  }
  
  public void processWord( String w ) {
    int wordLen = w.length();
    w = w.toUpperCase(); // zebra -> ZEBRA
    char [] lets = w.toCharArray(); // ZEBRA -> ['Z','E','B','R','A']
    Arrays.sort(lets); // ['Z','E','B','R','A'] -> ['A','B','E','R','Z']
        
    int [] tempFreqs = null;
    for( String sub : this.getSubSequences(lets) ) {
      if( (tempFreqs = this.seqFreqs.get(sub)) == null ) {
        tempFreqs = new int [ this.maxWordLen + 1 ];
        this.seqFreqs.put(sub, tempFreqs);
      }
      
      tempFreqs[0]++;
      tempFreqs[wordLen]++;      
    }
  }
  
  private Set<String> getSubSequences( char [] lets ) {
    System.out.println( "Processing: " + new String( lets ) );
    Set<String> subs = new HashSet<String>();
    StringBuilder sub = new StringBuilder();
    int mask = 1 << lets.length;
    int marker = 1;
    int idx = 0;
    for( int i = 1; i < mask; i++ ) {
      while( marker <= mask ) {
        if( (i & marker) > 0 ) {
          sub.append( lets[idx] );
        }
        marker = marker << 1;
        idx++;
      }
      
      if( subs.add(sub.toString()) ) {
        System.out.println( "  --> " + sub.toString());
      }
      sub.delete( 0, sub.length() );
      marker = 1;
      idx = 0;
    }
    
    return subs;
  }
  
  public void printAllFrequencies() {
    int [] temp = null;
    System.out.print( "Sequence\tTotal");
    for( int i = 1; i <= this.maxWordLen; i++ ) {
      System.out.print("\tLen " + i);
    }
    System.out.println("");
    
    for( String key: this.seqFreqs.keySet() ) {
      temp =  this.seqFreqs.get( key );
      System.out.print( key );
      for( int i = 0; i <= this.maxWordLen; i++ ) {
        System.out.print("\t" + temp[i] );
      }
      System.out.println("");
    }
  }
  
  public void printAnagramFrequencies() {
    int total = 0;
    int [] freqArray = null;
    System.out.println("Sequence\tTotal");
    for( String seq : this.seqFreqs.keySet() ) {
      if( seq.length() == this.maxWordLen ) {
        total = 0;
        // this is a "root" sequence, let's add up totals.
        for( String sub : this.getSubSequences(seq.toCharArray())) {
          freqArray = this.seqFreqs.get(sub);
          total += freqArray[ sub.length() ];
        }
        
        System.out.println(""+seq+"\t"+total);
      }
    }
  }
  
  public static void main( String [] args )
  {
    String fname = args[0];
    String wordSizeStr = args[1];
    int wordSize = 0;
    
    try {
      wordSize = Integer.parseInt(wordSizeStr);
    } catch( NumberFormatException e ) {
      wordSize = 15;
    }
    
    SetFreqs SF = new SetFreqs( wordSize );
    
    BufferedReader input_reader = null;
    String input_str = "";

    try
    {
      input_reader = new BufferedReader( new FileReader ( fname ) );

      while( (input_str = input_reader.readLine()) != null )
      {
        if( input_str.length() <= wordSize ) {
          SF.processWord(input_str);
        }
      }

      //SF.printAnagramFrequencies();
      SF.printAllFrequencies();
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
  
}

