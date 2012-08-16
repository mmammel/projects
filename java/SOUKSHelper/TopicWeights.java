import java.io.*;
import java.util.*;
import java.util.regex.*;

public class TopicWeights
{

  private static int ASSERTIVE = 0;
  private static int ADAPTABILITY = 1;
  private static int TRUST = 2;
  private static int STRESS = 3;
  private static int COOPERATION = 4;
  private static int CANDIDNESS = 5;
  private static int DEPENDABILITY = 6;
  private static int SERVICE = 7;
  private static int LEADERSHIP = 8;
  private static int COMPETITIVE = 9;
  private static int CONFIDENCE = 10;

  public static void main( String [] args )
  {
    try
    {

      BufferedReader inputReader = new BufferedReader( new InputStreamReader( new FileInputStream( "topicWeights.txt" ) ));

      List<String> [] topics = (ArrayList<String>[]) new ArrayList[11];
      for( int i = 0; i < topics.length; i++ )
      {
        topics[i] = new ArrayList<String>();
      }

      String temp = null;
      String [] tempArray = null;
      while( ( temp = inputReader.readLine()) != null )
      {
        tempArray = temp.split("\\|");
        if( tempArray[1].indexOf( "Assertive" ) > 0 )
        {
          topics[ASSERTIVE].add(tempArray[0]);
        } else if( tempArray[1].indexOf( "Adaptability" ) == 7 ) {
           topics[ADAPTABILITY].add(tempArray[0]);
        } else if( tempArray[1].indexOf( "Trust" ) == 7 ) {
          topics[TRUST].add(tempArray[0]);
        } else if( tempArray[1].indexOf( "Stress" ) == 7 ) {
          topics[STRESS].add(tempArray[0]);
        } else if( tempArray[1].indexOf( "Cooperation" ) == 7 ) {
          topics[COOPERATION].add(tempArray[0]);
        } else if( tempArray[1].indexOf( "Candidness" ) == 7 ) {
          topics[CANDIDNESS].add(tempArray[0]);
        } else if( tempArray[1].indexOf( "Dependability" ) == 7 ) {
          topics[DEPENDABILITY].add(tempArray[0]);
        } else if( tempArray[1].indexOf( "Service" ) == 7 ) {
          topics[SERVICE].add(tempArray[0]);
        } else if( tempArray[1].indexOf( "Leadership" ) == 7 ) {
          topics[LEADERSHIP].add(tempArray[0]);
        } else if( tempArray[1].indexOf( "Competitive" ) == 7 ) {
          topics[COMPETITIVE].add(tempArray[0]);
        } else if( tempArray[1].indexOf( "Confidence" ) == 7 ) {
          topics[CONFIDENCE].add(tempArray[0]);
        }

      }

      BufferedWriter outputWriter = new BufferedWriter( new OutputStreamWriter( new FileOutputStream( "topics.txt"), "UTF-16LE" ) );

      String inputStr = null;
      int count = 1;
      boolean first = true;
      StringBuilder sb = null;
      for( List<String> topic : topics )
      {
        first = true;
        sb = new StringBuilder("set @topicScore" + count + "=");

        for( String num : topic )
        {
          if( !first )
          {
            sb.append( "+" );
          }
          else
          {
            first = false;
          }

          sb.append( "@weight" + num );
        }

        inputStr = sb.toString();
        outputWriter.write( inputStr, 0, inputStr.length() );
        outputWriter.newLine();
        count++;
      }

      outputWriter.flush();
    }
    catch( Exception e )
    {
      System.out.println( "Caught Exception: " + e.toString() );
      e.printStackTrace();
    }

  }

}
