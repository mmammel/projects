import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class TriangleGetter
{
  public static void main( String [] args )
  {
    String fname = args[0];
    BufferedReader input_reader = null;
    String input_str = "";

    Map<Integer,Integer> vertexMap = new TreeMap<Integer,Integer>();
    List<String> triples = new ArrayList<String>();

    String [] lineArray = null;

    try
    {
      input_reader = new BufferedReader( new FileReader ( fname ) );

      while( (input_str = input_reader.readLine()) != null )
      {
        // Process Input
        lineArray = input_str.split(" ");
        vertexMap.put( Integer.parseInt( lineArray[0] ), 99 );
        for( int i = 1; i < 6; i++ ) {
          vertexMap.put( Integer.parseInt( lineArray[i] ), 99 );
          triples.add( lineArray[0] + "," + lineArray[i] +","+(i == 5 ? lineArray[1] : lineArray[i+1]));
        }
      }

      // map the vertex nums to simple order from 0
      int count = 0;
      for( Integer v : vertexMap.keySet() ) {
        System.out.println( "Putting: " + v + " -> " + count );
        vertexMap.put( v, count++);
      }


      boolean first = true;
      for( String trip : triples ) {
        if( !first ) {
          System.out.print(",");
        } else {
          first = false;
        }
        System.out.print( getMappedTriple( trip, vertexMap ) );
      }

      System.out.println( "" );
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }

  public static String getMappedTriple( String triple, Map<Integer,Integer> vertexMap ) {
    String [] arr = triple.split( "," );
    StringBuilder sb = new StringBuilder();
    boolean first = true;
    for( String s : arr ) {
     if( !first ) {
       sb.append(",");
     } else {
       first = false;
     }
     sb.append( vertexMap.get( Integer.parseInt( s ) ) );
    }
    return sb.toString();
  }
}

