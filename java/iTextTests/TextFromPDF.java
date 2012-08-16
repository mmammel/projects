import java.io.BufferedReader;
import java.util.HashMap;
import java.io.InputStreamReader;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.parser.*;
import com.lowagie.text.FontFactory;

public class TextFromPDF
{
  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    String input_str = "";
    PdfReader pdfReader = null;
    PdfTextExtractor textExtractor = null;

    try
    {

      FontFactory.registerDirectories();
FontFactory.registerDirectory("C:\\cygwin\\usr\\share\\fonts\\100dpi" );
FontFactory.registerDirectory("C:\\cygwin\\usr\\share\\fonts\\75dpi" );
FontFactory.registerDirectory("C:\\cygwin\\usr\\share\\fonts\\OTF" );
FontFactory.registerDirectory("C:\\cygwin\\usr\\share\\fonts\\Speedo" );
FontFactory.registerDirectory("C:\\cygwin\\usr\\share\\fonts\\TTF" );
FontFactory.registerDirectory("C:\\cygwin\\usr\\share\\fonts\\Type1" );
FontFactory.registerDirectory("C:\\cygwin\\usr\\share\\fonts\\cyrillic" );
FontFactory.registerDirectory("C:\\cygwin\\usr\\share\\fonts\\encodings" );
FontFactory.registerDirectory("C:\\cygwin\\usr\\share\\fonts\\misc" );
FontFactory.registerDirectory("C:\\cygwin\\usr\\share\\fonts\\util" );

      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( "Hola!!" );

      System.out.print("pdf filename>");

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "quit" ) )
        {
          break;
        }
        else
        {
          // Process Input
          pdfReader = new PdfReader( input_str );

          System.out.println( "MetaData: \n" + new String(pdfReader.getMetadata(), "UTF-8") );

          HashMap infoMap = pdfReader.getInfo();

          for( Object infoKey : infoMap.keySet() )
          {
            System.out.println( infoKey.toString() + " -> " + infoMap.get(infoKey).toString() );
          }

          PdfDictionary dictionary = pdfReader.getCatalog();


          for( Object obj : dictionary.getKeys() )
          {
            System.out.println( "dictionary key: " + obj.toString() );
            System.out.println( "dictionary value:\n" + dictionary.get((PdfName)obj).getClass().getName() );
          }


          if( dictionary.contains( PdfDictionary.FONT ) )
          {
            System.out.println( "Font: " + dictionary.get( PdfDictionary.FONT ).toString() );
          }

          textExtractor = new PdfTextExtractor( pdfReader );

          int pages = pdfReader.getNumberOfPages();

          System.out.println( "Pages: " + pages );

          for( int i = 1; i <= pages; i++ )
          {
             System.out.println( "PAGE " + i + ":\n---------" );
             System.out.println( textExtractor.getTextFromPage(i) );
          }

        }

        System.out.print( "\npdf filename>" );
      }

      System.out.println( "Adios!!" );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

