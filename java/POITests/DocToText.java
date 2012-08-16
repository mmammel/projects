import org.apache.poi.poifs.filesystem.*;
import org.apache.poi.hwpf.*;
import org.apache.poi.hwpf.extractor.*;
import java.io.*;

public class DocToText
{
  public static void main( String[] args )
  {
    String filesname = "RoboticPosting_BoardSetup.doc";
    POIFSFileSystem fs = null;
    try
    {
                  fs = new POIFSFileSystem(new FileInputStream(filesname));
                  //Couldn't close the braces at the end as my site did not allow it to close

                  HWPFDocument doc = new HWPFDocument(fs);

      WordExtractor we = new WordExtractor(doc);

      String[] paragraphs = we.getParagraphText();

      System.out.println( "Word Document has " + paragraphs.length + " paragraphs" );
      for( int i=0; i<paragraphs .length; i++ ) {
      paragraphs[i] = paragraphs[i].replaceAll("\\cM?\r?\n","");
                  System.out.println( "Length:"+paragraphs[ i ].length());
      }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
         }
}