/*
 * BasicErrorHandler.java
 */
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class BasicErrorHandler
   extends AbstractErrorHandler
{
   public void fatalError( SAXParseException e )
      throws SAXException
   {
   }
   
   public void error( SAXParseException e )
      throws SAXException
   {
   }
   
   public void warning( SAXParseException e )
   {
   }
   
   protected void log( String type, SAXParseException e )
   {
   }
}

