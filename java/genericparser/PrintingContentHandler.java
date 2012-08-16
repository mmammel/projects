/*
 * BasicContentHandler.java
 */
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import java.util.Iterator;

/**
 */
public class PrintingContentHandler
   extends AbstractContentHandler
{

   protected XPathStack stack = null;
   protected boolean marked   = false;
   protected StringBuffer characters = null;

   /**
    * Constructor
    */
   public PrintingContentHandler()
   {
      this.stack = new XPathStack();
   }

   /**
    * Call back for the start of the XML document.
    */
   public void startDocument()
   { }

   /**
    * Call back for the end of the XML document.
    */
   public void endDocument()
   {
      this.stack = null;
   }

   /**
    * Call back for the start of an element.
    * <p>
    * @param namespaceURI    URI of namespace
    * @param localName       element name without prefix
    * @param rawName         element name with prefix
    * @param attributes      element attributes
    */
   public void startElement( String namespaceURI, String localName, String rawName, Attributes attributes )
      throws SAXException
   {
      XPathArray tempPath = null;
      this.marked = false;
      String tempAttrValue = null;
      String intern = localName.intern(); // intern makes using == OK and it's fast
      this.stack.push( intern ); // beginning to traverse this element and any of it's children

      for( Iterator iter = paths.iterator(); iter.hasNext(); )
      {
        tempPath = (XPathArray)iter.next();
        if( stack.compareWithXPath( tempPath ) )
        {
          if( tempPath.isAttribute() )
          {
            tempAttrValue = attributes.getValue( tempPath.getAttributeName() );
            if( tempAttrValue != null )
            {
              System.out.println( tempAttrValue );
            }
          }
          else
          {
            this.marked = true;
          }
        }
      }
   }

   /**
    * Call back for text nodes.  NOTE: This method may be called many times for a given
    * element's text node, depending on parser implementation.  This method must support
    * preserving any textual data that exists from the previous call for the same element.
    * <p>
    * Example: <someNode>LONG_TEXT</someNode> may generate two separate calls to characters,
    * one for LONG_ and the other for TEXT.
    * <p>
    * @param chars     array of characters
    * @param start     start position within array for this read
    * @param length    length to read
    */
   public void characters( char[] chars, int start, int length )
   {
      this.characters = ( this.characters == null ? new StringBuffer( length ) : this.characters );
      this.characters.append( new String( chars, start, length ) );
   }

   /**
    * Call back for the end of an element.
    * <p>
    * @param namespaceURI    URI of namespace
    * @param localName       element name without prefix
    * @param rawName         element name with prefix
    */
   public void endElement( String namespaceURI, String localName, String rawName )
      throws SAXException
   {
      String intern = localName.intern(); // intern makes using == OK and it's fast

      if( marked )
      {
        System.out.println( this.characters() );
        marked = false;
      }
      else
      {
        this.characters = null;
      }

      this.stack.pop(); // fully traversed this element and any of it's children
   }

   /**
    * Return the current character values and reset the characters.
    * <p>
    * @return String
    */
   protected String characters()
   {
      String value = this.characters.toString().trim();
      this.characters = null;
      return( value );
   }
}

