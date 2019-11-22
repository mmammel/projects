import java.io.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class StreamEncoding {

  public static void main( String[] args ) {
    //StringBufferInputStream theStream = null;
    FileInputStream theStream = null;
    try {
 
      //StringBuffer rb = new StringBuffer();
      //String line = null;

      //BufferedReader reader = new BufferedReader( new FileReader( args[0] ) );
      //while ((line = reader.readLine()) != null)
       // rb.append(line);
			    	    					    		 		    			    
      //theStream = new StringBufferInputStream(rb.toString());
      theStream = new FileInputStream( args[0] );

      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = dbf.newDocumentBuilder();
      org.w3c.dom.Document doc = (org.w3c.dom.Document)builder.parse(theStream);
    } catch( Exception e ) {
      System.out.println( "Exception: " + e.toString() );
    } finally {
      if( theStream != null ) {
        try {
          theStream.close();
        } catch( IOException ioe ) {
        }
      }
    }
  }
}
