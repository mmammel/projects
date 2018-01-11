import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

public class CSVBreaker {
  private String outputPrefix;
  private int [] keyFields;
  private boolean hasHeader = false;

  public CSVBreaker( String outputPrefix, String keyFields, boolean hasHeader ) {
    this.outputPrefix = outputPrefix;
    this.hasHeader = hasHeader;
    String [] fields = keyFields.split(",");
    this.keyFields = new int [ fields.length ];
    try {
      for( int i = 0; i < this.keyFields.length; i++ ) {
        this.keyFields[i] = Integer.parseInt( fields[i] );
      }
    } catch( Exception e ) {
      System.out.println( "Bad fields passed - must be comma-separated list of integers" );
      throw new RuntimeException( "Bad params" );
    }
  }

  public void process( String fileName ) {
    this.process_inner( fileName );
  }

  private void process_inner( String fileName ) {

    Map<String,PrintWriter> writers = new HashMap<String,PrintWriter>();
    PrintWriter currWriter = null;
    String header = null;
    BufferedReader input_reader = null;
    String input_str = "";
    String [] fields = null;
    String key = null;

    try
    {
      input_reader = new BufferedReader( new FileReader ( fileName ) );

      while( (input_str = input_reader.readLine()) != null )
      {
        if( this.hasHeader && header == null ) {
          header = input_str;
          continue;
        }

        if( input_str.length() == 0 ) break;

        // Process Input
        fields = input_str.split( "," ); // TODO, use the QuotedSplitter
        key = this.buildKey( fields );
        if( (currWriter = writers.get(key)) == null ) {
          currWriter = new PrintWriter( new BufferedWriter( new FileWriter( this.buildFileName( key ) ) ) );
          currWriter.println( header );
          writers.put( key, currWriter );
        }

        currWriter.println( input_str );
      }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
    finally 
    {
      try {
        input_reader.close();
      } catch( IOException ioe ) {
        System.out.println( "Error closing reader" );
      }

      for( String k : writers.keySet() ) {
        PrintWriter p = writers.get(k);
        try {
          p.flush();
          p.close();
        } catch( Exception ioe ) {
          System.out.println( "Error closing writer: " + ioe.toString() );
        }
      }
    }
    
  }

  private String buildFileName( String key ) {
    return this.outputPrefix + key + ".csv";
  }

  private String buildKey( String [] fields ) {
    StringBuilder sb = new StringBuilder();
    for( int i = 0; i < this.keyFields.length; i++ ) {
      sb.append("_");
      sb.append( fields[ this.keyFields[ i ] ].trim() );
    }

    return sb.toString();
  }

  public static void main( String [] args ) {
    int argIdx = 0;

    // defaults
    String outputPrefix = "output";
    String keyFields = "1";
    boolean header = false;
    
    String currArg = null;
    while( argIdx < args.length - 1 ) {
      currArg = args[argIdx++]; 
      if( currArg.equals( "-p") ) {
        outputPrefix = args[argIdx++];
      } else if( currArg.equals( "-f" ) ) {
        keyFields = args[argIdx++];
      } else if( currArg.equals( "-h" ) ) {
        header = true;
      } else {
        printUsage();
        break;
      }
    }

    CSVBreaker CB = new CSVBreaker( outputPrefix, keyFields, header );
    CB.process( args[argIdx] );
  }

  public static void printUsage() {
    System.out.println( "Usage: csvBreaker [-h] [-p <outputPrefix>] [-f <keyfields>] <fileName>" );
  }
}
