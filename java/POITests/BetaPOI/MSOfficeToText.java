import org.apache.poi.*;
import org.apache.poi.extractor.*;
import java.io.*;

public class MSOfficeToText
{
  public static void main( String[] args )
  {
    String filename = args[0];

    try
    {

      POITextExtractor extractor = ExtractorFactory.createExtractor( new FileInputStream( args[0] ) );

      System.out.println( "Text:\n-----\n" + extractor.getText() );

    }
    catch(Exception e) {
       e.printStackTrace();
    }
  }
}