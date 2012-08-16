import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.InputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class GenericParser
{
   private ContentHandler mContentHandler = null;
   private ErrorHandler mErrorHandler     = null;

   public GenericParser()
   {
     mContentHandler = new PrintingContentHandler();
     mErrorHandler   = new BasicErrorHandler();
   }

   /**
    * Parse the sync op set.
    * <p>
    * @param context   work flow context
    * <p>
    * @return WorkFlowContext
    * <p>
    * @throws SyncEngineException
    */
   public void parse( InputStream inStream )
   {
      XMLReader reader = null;
      ContentHandler contentHandler = null;
      ErrorHandler errorHandler = null;

      try
      {
         // create an instance of the parser
         reader = XMLReaderFactory.createXMLReader( "org.apache.xerces.parsers.SAXParser" );

         // register the content handler and error handler
         reader.setContentHandler( mContentHandler );
         reader.setErrorHandler( mErrorHandler );

         // parse the document
         reader.parse( new InputSource( inStream ) );
      }
      catch( Exception e )
      {  
        e.printStackTrace();
        System.out.println( e.toString() );
      }
   }

   public void setContentHandler( ContentHandler handler )
   {
     this.mContentHandler = handler;
   }

   public void setErrorHandler( ErrorHandler handler )
   {
     this.mErrorHandler = handler;
   }

   public static void main( String [] args )
   {
     try
     {
System.out.println( "Getting input stream" );
       InputStream finStream = new FileInputStream( new File( "scores.xml" ) );
System.out.println("Initializing printing content handler" );
       PrintingContentHandler myHandler = new PrintingContentHandler();
System.out.print( "Processing arguments: " );
       for( int i = 0; i < args.length; i++ )
       {
System.out.print( args[i] + " " );
         myHandler.addXPath( args[i] );
       }
System.out.println("\nInitializing Generic Parser");
       GenericParser GP = new GenericParser();
       GP.setContentHandler( myHandler );
System.out.println("Parsing...");
       GP.parse(finStream); 
     }
     catch( Exception e )
     {
       e.printStackTrace();
       System.out.println( e.toString() );
     }
   }

}

