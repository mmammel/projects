public class TestResults
{
  private long startTime;
  private long endTime;
  private float avgRequestTime;
  private Map<ScriptStep,Float> stepTimes;

  public TestResults()
  {
    stepTimes = new HashMap<ScriptStep,Float>();
  }

  public void addStep( ScriptStep step, Float time )
  {
    Float tempTime = time;

    if( this.stepTimes.containsKey( step ) )
    {
      tempTime += this.stepTimes.get( step );
    }

    this.stepTimes.put( step, tempTime );
  }



}
