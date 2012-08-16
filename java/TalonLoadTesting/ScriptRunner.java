import com.meterware.httpunit.*;
import java.util.List;

public class ScriptRunner implements Runnable
{

  private static final String JAVASCRIPT_CONTROLLER = "/test/runJavaScript";

  private final LoadTestScript script;
  private String baseURL;

  public ScriptRunner( String baseURL, LoadTestScript script )
  {
    this.script = script;
    this.baseURL = baseURL;
  }

  public void run()
  {
    WebConversation conversation;
    List<ScriptStep> steps;

    try
    {
      conversation = new WebConversation();



      conversation.getClientProperties().setAcceptGzip(true);

      System.out.println( "Props: " + conversation.getClientProperties() );

      steps = this.script.getSteps();

      for( ScriptStep step : steps )
      {
        switch( step.getType() )
        {
          case JAVASCRIPT_METHOD:
              conversation.getResponse(this.baseURL + JAVASCRIPT_CONTROLLER + "?method=" + step.getCommand() + "&args=" + step.getArgs() );
              break;
          case HTTP_ABSOLUTE:
              conversation.getResponse(step.getCommand());
              break;
          case HTTP_RELATIVE:
              conversation.getResponse(this.baseURL + step.getCommand());
              break;
          default:
              break;
        }
      }
    }
    catch( Exception e )
    {
      //System.out.println( "Script aborted: " + e.toString() );
    }

  }
}