import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.ArrayList;

public class NormsBuilder
{
  public static void main( String [] args )
  {
    String fname = args[0];
    String testKey = fname.substring(0, fname.lastIndexOf(".") );
    BufferedReader input_reader = null;
    String input_str = "";
    List<Integer> scores = new ArrayList<Integer>();

    int [] percentiles = new int [99];

    String [] scoreFreq = null;
    double score = 0.0;
    double totScore = 0.0;
    double average = 0.0;
    int total = 0;
    int freq = 0;

    try
    {
      input_reader = new BufferedReader( new FileReader ( fname ) );

      while( (input_str = input_reader.readLine()) != null )
      {
        scoreFreq = input_str.split(",");
        score = Double.parseDouble( scoreFreq[0] );
        freq = Integer.parseInt( scoreFreq[1] );
        total += freq;

        for( int i = 0; i < freq; i++ ) {
          scores.add( Double.valueOf(Math.floor(score)).intValue() );
          totScore += score;
        }
      }

      average = Math.ceil(totScore / total);
      System.out.println( "Average: " + average );

      double bucketRange = Double.valueOf((Integer.valueOf(total).doubleValue() / 100.0));
      System.out.println( "bucket range: " + bucketRange );

      int count = 1;
      int scoresIdx = 0;

      while( count < 100 ) {
        scoresIdx = Double.valueOf(count * bucketRange).intValue();
        percentiles[ count - 1 ] =  scores.get(scoresIdx);
        count++;
      }

      StringBuilder sb = new StringBuilder("INSERT INTO NormData (tableKey, parentKey, testKey, numScores, avgScore, percentile1, percentile2, percentile3, percentile4, percentile5, percentile6, percentile7, percentile8, percentile9, percentile10, percentile11, percentile12, percentile13, percentile14, percentile15, percentile16, percentile17, percentile18, percentile19, percentile20, percentile21, percentile22, percentile23, percentile24, percentile25, percentile26, percentile27, percentile28, percentile29, percentile30, percentile31, percentile32, percentile33, percentile34, percentile35, percentile36, percentile37, percentile38, percentile39, percentile40, percentile41, percentile42, percentile43, percentile44, percentile45, percentile46, percentile47, percentile48, percentile49, percentile50, percentile51, percentile52, percentile53, percentile54, percentile55, percentile56, percentile57, percentile58, percentile59, percentile60, percentile61, percentile62, percentile63, percentile64, percentile65, percentile66, percentile67, percentile68, percentile69, percentile70, percentile71, percentile72, percentile73, percentile74, percentile75, percentile76, percentile77, percentile78, percentile79, percentile80, percentile81, percentile82, percentile83, percentile84, percentile85, percentile86, percentile87, percentile88, percentile89, percentile90, percentile91, percentile92, percentile93, percentile94, percentile95, percentile96, percentile97, percentile98, percentile99, fixed, version) VALUES ('SKILLCHECK."+testKey+"', 'SKILLCHECK', '"+testKey+"', ");

     sb.append( total ).append(", ").append( Double.valueOf( average ).intValue() ); 

      for( int p : percentiles ) {
        sb.append(", ").append( p );
      }

      sb.append( ", 70, 1.0);");

      System.out.println( sb.toString() );
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

