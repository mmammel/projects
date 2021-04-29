import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class UseIntern
{
  public static void main( String [] args )
  {
    String fname = args[0];
    BufferedReader input_reader = null;
    String input_str = "";
    Map<String,String> localIntern = new HashMap<String,String>();
    Map<String,Integer> freqs = new HashMap<String,Integer>();
    Integer tempFreq = 0;

    try
    {
      input_reader = new BufferedReader( new FileReader ( fname ) );

      while( (input_str = input_reader.readLine()) != null )
      {
        if( !localIntern.containsKey(input_str) ) localIntern.put( input_str, input_str.intern() );
        for( int i = 0; i < 1000; i++ ) {
          input_str = localIntern.get(input_str);
          if( input_str == "antenna" ||
            input_str == "antennae" ||
            input_str == "antennas" ||
            input_str == "anterior" ||
            input_str == "anthem" ||
            input_str == "anthems" ||
            input_str == "anther" ||
            input_str == "anthologies" ||
            input_str == "anthology" ||
            input_str == "Anthony" ||
            input_str == "anthracite" ||
            input_str == "anthropological" ||
            input_str == "anthropologically" ||
            input_str == "anthropologist" ||
            input_str == "anthropologists" ||
            input_str == "anthropology" ||
            input_str == "anthropomorphic" ||
            input_str == "anthropomorphically" ||
            input_str == "anti" ||
            input_str == "antibacterial" ||
            input_str == "antibiotic" ||
            input_str == "antibiotics" ||
            input_str == "antibodies" ||
            input_str == "antibody" ||
            input_str == "antic" ||
            input_str == "anticipate" ||
            input_str == "anticipated" ||
            input_str == "anticipates" ||
            input_str == "anticipating" ||
            input_str == "anticipation" ||
            input_str == "anticipations" ||
            input_str == "anticipatory" ||
            input_str == "anticoagulation" ||
            input_str == "anticompetitive" ||
            input_str == "antics" ||
            input_str == "antidisestablishmentarianism" ||
            input_str == "antidote" ||
            input_str == "antidotes" ||
            input_str == "Antietam" ) {
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

