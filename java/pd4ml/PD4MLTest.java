import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.io.IOException;
import org.zefer.pd4ml.PD4ML;
import org.zefer.pd4ml.PD4Constants;
import java.awt.Insets;
import java.awt.Dimension;
import java.io.StringReader;

public class PD4MLTest {

  public static void main( String [] args )
  {
    InputStream is = null;
    String report;
    FileOutputStream fos = null;
    ByteArrayOutputStream bos = null;

    try
    {
      is = new FileInputStream( args[0] );

      report = stringFromStream( is, "UTF-8" );
      
      bos = createPdf( report );

      fos = new FileOutputStream( "report.pdf" );

      bos.writeTo(fos);
      fos.flush();
    }
    catch( Exception e )
    {
      System.out.println( "Caught an Exception: " + e.toString() );
    }
    finally
    {
      try
      {
        is.close();
        bos.close();
        fos.close();
      }
      catch( IOException ioe )
      {
      }
    }
  }

public static ByteArrayOutputStream createPdf(String pReportString)  {
		String testingURL = "http://" + "beta2.skillcheck.com" + "/onlinetesting/images/";
		//pReportString = pReportString.replaceAll("/images/", testingURL);
		
		StringReader stringReader = new StringReader(pReportString);
		
		ByteArrayOutputStream os = null;
		boolean landscapeValue = false;
		
		try{
			os = new ByteArrayOutputStream();
		
			Dimension format=PD4Constants.A4;
			PD4ML pd4ml = new PD4ML();
			pd4ml.setPageInsets(new Insets(20, 20, 20, 20));
			pd4ml.setHtmlWidth(950);
			pd4ml.adjustHtmlWidth();
			pd4ml.setPageSize(landscapeValue?pd4ml.changePageOrientation(format): format); // landscape page orientation
            
			pd4ml.disableHyperlinks();
			pd4ml.enableTableBreaks(false);
			pd4ml.enableImgSplit(false);
			pd4ml.setDefaultTTFs("Tahoma", "Tahoma", "Tahoma");
						
		    pd4ml.useTTF("/Network/Library/Fonts", true);
	
			pd4ml.render(stringReader, os);
		}
		catch(Exception e){
			System.out.println( "Exception! " + e);
		}

		
       return os;
   }

	  /**
	   * Utility to get a string from a streamed input. Useful for getting a 
	   * String version of a request XML or something.
	   * @param input
	   * @return
	   */
	  public static String stringFromStream(InputStream input, String charset) {
	    int c;

	    StringBuilder sb = new StringBuilder();
	    InputStreamReader reader = null;

	    try {
	      reader = new InputStreamReader(input, charset);

	      while ((c = reader.read()) != -1) {
	        char charin = (char) c;
	        sb.append(charin);
	      }
	    }
	    catch (IOException ioe) {
	      System.out.println("Caught an exception trying to read the input stream: " + ioe.toString() );
	    }
	    finally {
	      try {
	        reader.close();
	      }
	      catch (IOException ioe2) {
	        System.out.println("Couldn't close input stream: " + ioe2.toString());
	      }
	    }

	    return sb.toString();
	  }

}
