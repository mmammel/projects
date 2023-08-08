import java.io.BufferedReader;
import java.io.FileReader;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Arrays;

public class FindRuns
{
  public static void main( String [] args )
  {
    String fname = args[0];
    BufferedReader input_reader = null;
    String input_str = "";

    char [] lets = null;
    SortedSet<String> result = new TreeSet<String>();
    String ordered = null;

    try
    {
      input_reader = new BufferedReader( new FileReader ( fname ) );

      while( (input_str = input_reader.readLine()) != null )
      {
        lets = input_str.toUpperCase().toCharArray();
        Arrays.sort(lets);
        char prev = 0;
        StringBuilder sb = null;
        for( char c : lets ) {
          if( c != prev ) {
            if( sb != null ) {
              result.add( sb.toString() );
            }
            sb = new StringBuilder();
          }
          prev = c;
          sb.append(c);
        }
        result.add( sb.toString() );
      }

      for( String s : result ) {
        System.out.println( s );
      }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

