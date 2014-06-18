import java.io.*;
import java.util.regex.*;

public class NamespaceApplyer {

  public static final Pattern XML_TAG_PATTERN = Pattern.compile( "(<\\/?)(.*?)(\\/?>)" );

  public static void main( String [] args ) {
    if( args.length != 2 ) {
      System.out.println( "Usage: java NamespaceApplyer <filename> <namespace>" );
      System.out.println( "Usage: assumes non-namespaced XML file" );
      System.exit(1);  
    }
    String fname = args[0];
    String namespace = args[1];

    try {
      String xml = StreamUtil.stringFromStream( new FileInputStream( fname ) ); 

      StringBuffer retVal = new StringBuffer();
      Matcher m = XML_TAG_PATTERN.matcher(xml);
      String tempVar = null;
      String tempReplacement = null;
      
      while( m.find() )
      {
        tempReplacement = m.group(1) + namespace + ":" + m.group(2) + m.group(3);
      
        m.appendReplacement(retVal, tempReplacement);
      }
    
      m.appendTail(retVal);
      
      System.out.println(retVal.toString());

    } catch( Exception e ) {
      System.out.println( "Caught an exception: " + e.toString() );
    }
  }
}
