import org.apache.commons.csv.*;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class ParseCSV {
  public static void main( String [] args ) {
    try {
      File csvData = new File(args[0]);
      CSVParser parser = CSVParser.parse(csvData, Charset.forName("UTF-8"), CSVFormat.RFC4180);
      Map<String,Integer> headerMap = parser.getHeaderMap();
      for (CSVRecord csvRecord : parser) {
        for( int i = 0; i < csvRecord.size(); i++ ) {
          System.out.println( csvRecord.get(i) );
        }
      }
    } catch( IOException ioe ) {
      System.out.println( "Exception: " + ioe.toString() );
    }
  }
}
