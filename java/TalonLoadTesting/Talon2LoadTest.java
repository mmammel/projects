import com.meterware.httpunit.*;

public class Talon2LoadTest {

  private int runningThreads = 0;
  private int reportingThreads = 0;
  private String baseURL;
  private int threads;
  private LoadTestScript script;

  public Talon2LoadTest( String baseURL, int threads, String script )
  {
    this.baseURL = baseURL;
    this.threads = threads;
    this.script = new LoadTestScript(script);
  }

  public static void main(String [] args)
  {
    String baseURL = args[0];
    Integer threads = Integer.parseInt( args[1] );
    String script = args[2];

    Talon2LoadTest talonTest = new Talon2LoadTest( baseURL, threads, script );

    talonTest.runTest();
  }

  public void runTest()
  {
    HttpUnitOptions.setScriptingEnabled(false);
    HttpUnitOptions.setExceptionsThrownOnScriptError( false );
    Thread [] tempThreads = new Thread [this.threads];
    ScriptRunner tempRunner;

    for( int i = 0; i < this.threads; i++ )
    {
      tempRunner = new ScriptRunner( this.baseURL, this.script);
      tempThreads[i] = new Thread( tempRunner );
      tempThreads[i].start();
    }

    for( int j = 0; j < this.threads; j++ )
    {
      try
      {
        tempThreads[j].join();
      }
      catch( InterruptedException ie )
      {
        System.out.println( "InterruptedThread!! " + ie.toString() );
      }
    }

  }

}
