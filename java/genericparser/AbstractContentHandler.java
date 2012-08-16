/*
 * AbstractContentHandler.java
 */
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;

import java.util.List;
import java.util.ArrayList;

public abstract class AbstractContentHandler
   implements ContentHandler
{
   protected Locator locator = null;
   protected List paths = null;

   public AbstractContentHandler()
   {
     paths = new ArrayList();
   }

   public void addXPath( XPathArray path )
   {
     paths.add( path );
   }

   public void addXPath( String pathStr )
   {
     paths.add( new XPathArray( pathStr ) );
   }

   public void addXPaths( XPathArray [] patharray )
   {
     for( int i = 0; i < patharray.length; i++ )
     {
       paths.add( patharray[ i ] );
     }
   }

   public void addXPaths( String [] pathStrArray )
   {
     for( int i = 0; i < pathStrArray.length; i++ )
     {
       paths.add( new XPathArray( pathStrArray[ i ] ) );
     }
   } 
   
   public void skippedEntity( String entity )
   {
   }
   
   public void startPrefixMapping( String prefix, String uri )
   {
   }
   
   public void processingInstruction( String target, String data )
   {
   }
   
   public void ignorableWhitespace( char[] characters, int start, int offset )
   {
   }
   
   public void endPrefixMapping( String prefix )
   {
   }
   
   public void setDocumentLocator( Locator locator )
   {
      this.locator = locator;
   }
   
   public int getLocatorLineNumber()
   {
      return( this.locator != null ? this.locator.getLineNumber() : -1 );
   }
   
   public int getLocatorColumnNumber()
   {
      return( this.locator != null ? this.locator.getColumnNumber() : -1 );
   }
}

