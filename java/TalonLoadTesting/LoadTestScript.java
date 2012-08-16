import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.net.URISyntaxException;

public class LoadTestScript
{
  private static final String JS_METHOD_SEP = "||";

  private List<ScriptStep> steps;

  public LoadTestScript( String scriptFilePath )
  {
    steps = new ArrayList<ScriptStep>();

    BufferedReader reader = null;

    try
    {
      reader = new BufferedReader( new FileReader( this.getFileFromClasspath( scriptFilePath ) ) );

      String [] tempArray;
      String tempInput = null;

      while( (tempInput = reader.readLine()) != null )
      {

        if( tempInput.startsWith( "#" ) )
        {
          continue;
        }
        else if( tempInput.startsWith( "http" ) )
        {
          steps.add( new ScriptStep( tempInput, null, RowType.HTTP_ABSOLUTE ) );
        }
        else if( tempInput.startsWith( "/" ) )
        {
          steps.add( new ScriptStep( tempInput, null, RowType.HTTP_RELATIVE ) );
        }
        else
        {
          tempArray = tempInput.split( JS_METHOD_SEP );
          steps.add( new ScriptStep( tempArray[0],tempArray[1], RowType.JAVASCRIPT_METHOD ) );
        }
      }
    }
    catch( IOException ioe )
    {
      System.out.println( "Caught an Exception: " + ioe.toString() );
    }
  }

  private File getFileFromClasspath( String filePath )
  {
    File retVal = null;

    URL fileURL = this.getClass().getResource(filePath);

    if( fileURL != null )
    {

      try {
        retVal = new File(fileURL.toURI());
      } catch(URISyntaxException e) {
        retVal = new File(fileURL.getPath());
      }
    }

    return retVal;
  }

  public List<ScriptStep> getSteps()
  {
    return this.steps;
  }

}