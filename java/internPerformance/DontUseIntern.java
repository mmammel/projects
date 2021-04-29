import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class DontUseIntern
{
  public static void main( String [] args )
  {
    String fname = args[0];
    BufferedReader input_reader = null;
    String input_str = "";
    Map<String,Integer> freqs = new HashMap<String,Integer>();
    Integer tempFreq = 0;

    try
    {
      input_reader = new BufferedReader( new FileReader ( fname ) );

      while( (input_str = input_reader.readLine()) != null )
      {
        for( int i = 0; i < 1000; i++ ) {
          if( input_str.equals("antenna") ||
            input_str.equals("antennae") ||
            input_str.equals("antennas") ||
            input_str.equals("anterior") ||
            input_str.equals("anthem") ||
            input_str.equals("anthems") ||
            input_str.equals("anther") ||
            input_str.equals("anthologies") ||
            input_str.equals("anthology") ||
            input_str.equals("Anthony") ||
            input_str.equals("anthracite") ||
            input_str.equals("anthropological") ||
            input_str.equals("anthropologically") ||
            input_str.equals("anthropologist") ||
            input_str.equals("anthropologists") ||
            input_str.equals("anthropology") ||
            input_str.equals("anthropomorphic") ||
            input_str.equals("anthropomorphically") ||
            input_str.equals("anti") ||
            input_str.equals("antibacterial") ||
            input_str.equals("antibiotic") ||
            input_str.equals("antibiotics") ||
            input_str.equals("antibodies") ||
            input_str.equals("antibody") ||
            input_str.equals("antic") ||
            input_str.equals("anticipate") ||
            input_str.equals("anticipated") ||
            input_str.equals("anticipates") ||
            input_str.equals("anticipating") ||
            input_str.equals("anticipation") ||
            input_str.equals("anticipations") ||
            input_str.equals("anticipatory") ||
            input_str.equals("anticoagulation") ||
            input_str.equals("anticompetitive") ||
            input_str.equals("antics") ||
            input_str.equals("antidisestablishmentarianism") ||
            input_str.equals("antidote") ||
            input_str.equals("antidotes") ||
            input_str.equals("Antietam") ) {
            
              tempFreq = freqs.containsKey(input_str) ? freqs.get(input_str) : 0;
              freqs.put(input_str,++tempFreq);
          }
        }
      }

      // for( String key : freqs.keySet() ) {
      //   System.out.println(key + " -> " + freqs.get(key) );
      // }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

