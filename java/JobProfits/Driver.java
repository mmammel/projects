import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;

public class Driver
{
  public static void main( String [] args )
  {
    String [] tempJobArray = null;
    String tempJobString = null;
    Job tempJob = null;
    List<Job> jobList = new ArrayList<Job>();
    for( int i = 0; i < args.length; i++ )
    {
      tempJobArray = args[i].split("-");
      tempJob = new Job( Integer.parseInt( tempJobArray[0] ), Integer.parseInt( tempJobArray[1] ), Integer.parseInt( tempJobArray[2] ) );
      jobList.add( tempJob );
    }

    Collections.sort( jobList );

    for( int j = 0; j < jobList.size(); j++ )
    {
      System.out.println( jobList.get(j) );
    }

    Driver D = new Driver();
    D.findOptimalSchedule( jobList );
  }

  /**
             {  0 if i = 0, or d = 0
    P(i,d) = {  P(i-1,d) if ti > MIN(d,di)
             {  MAX( pi + P(i-1,MIN(di,d)-ti),P(i-1,d) ) if ti <= MIN(di,d)
  */
  public void findOptimalSchedule( List<Job> jobList )
  {
    int maxDeadline = jobList.get(jobList.size() - 1).deadline;

    int [][] results = new int[jobList.size()+1][];
    for( int k = 0; k <= jobList.size(); k++ ) {
      results[k] = new int[maxDeadline+1];
      Arrays.fill( results[k], 0 );
    }

    String [][] track = new String[jobList.size()+1][];
    for( int r = 0; r <= jobList.size(); r++ ) {
      track[r] = new String[maxDeadline+1];
      Arrays.fill( track[r], null );
    }

    int minDeadline = -1;
    Job tempJob = null;

    for( int i = 1; i <= jobList.size(); i++ )
    {
      tempJob = jobList.get(i-1);
      for( int j = 1; j <= maxDeadline; j++ )
      {
        if( tempJob.deadline <= j )
        {
          minDeadline = tempJob.deadline;
        }
        else
        {
          minDeadline = j;
        }

        if( tempJob.duration > minDeadline )
        {
          results[i][j] = results[i-1][j];
        }
        else
        {
          if( (tempJob.value + results[i-1][minDeadline - tempJob.duration]) > results[i-1][j] )
          {
            results[i][j] = tempJob.value + results[i-1][minDeadline - tempJob.duration];
            track[i][j] = "" + i + "-" + (lastTime - tempJob.duration);
          }
          else
          {
            results[i][j] = results[i-1][j];
          }
        }
      }
    }

    System.out.println( "Maximum profit is: " + results[jobList.size()][maxDeadline] );

    this.printSolution( jobList, maxDeadline, track );
  }

  private void printSolution( List<Job> jobs, int maxDeadline, String [][] s )
  {
    String [] tempSplit = null;
    Job tempJob = null;
    int idx = jobs.size();
    while( maxDeadline > 0 && idx > 0 )
    {
      if( s[idx][maxDeadline] != null )
      {
        tempSplit = s[idx][maxDeadline].split("-");
        tempJob = jobs.get(Integer.parseInt(tempSplit[0]) - 1);
        maxDeadline = maxDeadline - tempJob.duration;
        tempJob.startTime = Integer.parseInt(tempSplit[1]);
        System.out.println( tempJob );
      }

      idx--;
    }
  }
}