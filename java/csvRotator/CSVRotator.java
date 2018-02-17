import java.util.*;
import org.json.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.PrintWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CSVRotator {

  private Set<String> dataColumns;
  private Map<String,String> dataColumnPrefixes;
  private String keyColumn;
  private String pivotColumn;
  private String lastStaticColumn;
  private List<String> rotatedHeader;  

  public CSVRotator( String configJSON ) {
    this.dataColumns = new HashSet<String>();
    this.dataColumnPrefixes = new HashMap<String,String>();
    JSONObject obj = new JSONObject(configJSON);
    this.keyColumn = obj.getString("keyColumn");
    this.lastStaticColumn = obj.getString("lastStaticColumn");
    this.pivotColumn = obj.getString("pivotColumn");
    JSONArray dataCols = obj.getJSONArray("dataColumns");
    JSONObject tempDataCol = null;
    String tempCol = null, tempPrefix = null;
    for( int i = 0; i < dataCols.length(); i++ ) {
      tempDataCol = dataCols.getJSONObject(i);
      tempCol = tempDataCol.getString("column");
      if( tempCol != null ) {
        this.dataColumns.add(tempCol);
        if( tempDataCol.has("prefix") ) {
          tempPrefix = tempDataCol.getString("prefix");
          this.dataColumnPrefixes.put(tempCol,tempPrefix);
        }
      }
    }
    
    this.rotatedHeader = new ArrayList<String>();
  }

  public void process( String fileName, String outFile ) {
    this.process_inner(fileName, outFile);
  }

  private void process_inner( String fileName, String outFile ) {

    PrintWriter outputWriter = null;
    String [] header = null;
    BufferedReader input_reader = null;
    String input_str = "";
    String [] fields = null;
    String currKey = null, tempKey = null, tempDataCol = null, tempPrefix = null, tempPivot = null;
    int keyIdx = -1;
    int pivotIdx = -1;
    int lastStaticIdx = -1;
    Map<Integer, String> dataColumnIndices = new HashMap<Integer, String>();
    boolean rotatedHeaderFinished = false;
    boolean firstRowForKey = false;
    List<String> currentOutputRow = null;

    try
    {
      input_reader = new BufferedReader( new FileReader ( fileName ) );
      outputWriter = new PrintWriter( new BufferedWriter( new FileWriter( outFile ) ) );

      while( (input_str = input_reader.readLine()) != null )
      {
        if( input_str.length() == 0 ) break;

        if( header == null ) {
          header = input_str.split( "," ); // get the header.
          for( int i = 0; i < header.length; i++ ) {
            if( header[i].equals( this.keyColumn ) ) {
              keyIdx = i;
              System.out.println( "Key column (" + this.keyColumn + ") Index: " + i);
            } else if( header[i].equals( this.pivotColumn ) ) {
              pivotIdx = i;
              System.out.println( "Pivot column (" + this.pivotColumn + ") Index: " + i);
            } else if( header[i].equals( this.lastStaticColumn ) ) {
              lastStaticIdx = i;
              System.out.println( "Last static column (" + this.lastStaticColumn + ") Index: " + i);
            } else if( this.dataColumns.contains( header[i] ) ) {
              dataColumnIndices.put( i, header[i] );
              System.out.println( "Data column (" + header[i] + ") Index: " + i);
            }

            if( lastStaticIdx == -1 || i == lastStaticIdx ) {
              // until we hit the pivot column or the last static idx, the rotated header will be the same.
              System.out.println( "Adding : " + header[i] + " to the rotatedHeader");
              this.rotatedHeader.add( header[i] );
            }
          }
        } else {
          // The header has been processed, we know where all of the pivots are - we have not necessarily finished constructing 
          // the rotated header though.
          fields = input_str.split( "," ); // TODO, use the QuotedSplitter

          tempKey = fields[ keyIdx ];
          tempPivot = fields[ pivotIdx ];

          if( currKey == null || !currKey.equals( tempKey ) ) {
            if( currentOutputRow != null ) {
              // see if we're done with the first row - if so the header is done.
              if( !rotatedHeaderFinished ) {
                outputWriter.println( this.getPrintableRow(this.rotatedHeader) );
                rotatedHeaderFinished = true;
              }

              outputWriter.println( this.getPrintableRow( currentOutputRow ) );
            }
            firstRowForKey = true;
            currentOutputRow = new ArrayList<String>();
            currKey = tempKey;
          } else {
            firstRowForKey = false;
          }

          for( int i = 0; i < fields.length; i++ ) {
            if( firstRowForKey && i <= lastStaticIdx ) {
              currentOutputRow.add( fields[i] );
            } else if( dataColumnIndices.containsKey( i ) ) {
              tempDataCol = dataColumnIndices.get( i );
              tempPrefix = this.dataColumnPrefixes.get( tempDataCol );
              if( !rotatedHeaderFinished ) {
                this.rotatedHeader.add( (tempPrefix == null ? "" : tempPrefix) + tempPivot );
              }

              currentOutputRow.add( fields[i] );
            }
          }

        }
      }

      if( currentOutputRow != null ) {
        // see if we're done with the first row - if so the header is done.
        if( !rotatedHeaderFinished ) {
          outputWriter.println( this.getPrintableRow(this.rotatedHeader) );
          rotatedHeaderFinished = true;
        }

        outputWriter.println( this.getPrintableRow( currentOutputRow ) );
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

      try {
        outputWriter.flush();
        outputWriter.close();
      } catch( Exception ioe ) {
        System.out.println( "Error closing writer: " + ioe.toString() );
      }
    }

  }

  private String getPrintableRow( List<String> row ) {
    StringBuilder sb = new StringBuilder();
    boolean first = true;

    for( String s : row ) {
      if( !first ) {
        sb.append(",");
      } else {
        first = false;
      }

      sb.append( s );
    }

    return sb.toString();
  }

  public static void main( String [] args ) {
    // usage: CSVRotator <configFile> <inputFile> <outputFile>
    if( args.length != 3 ) {
      System.err.println( "Usage: CSVRotator <configFile> <inputFile> <outputFile>" );
    } else {
      String configFile = args[0];
      String inputFile = args[1];
      String outputFile = args[2];
      
      try {
        String config = StreamUtil.stringFromStream( new FileInputStream( configFile ) );
        CSVRotator R = new CSVRotator( config );
        R.process( inputFile, outputFile );
      } catch( Exception e ) {
        System.out.println( "Error!!! " + e.toString() );
        e.printStackTrace();
      }
    }
  }

}
