import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BucketCalcTest
{
  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    String input_str = "";
    long longVal = 0L;

    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( "Hi." );

      System.out.print("Enter a number: ");

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "quit" ) )
        {
          break;
        }
        else
        {
          // Process Input
          longVal = Long.parseLong( input_str );

          System.out.println( "Bucket number: " + SliceBucket.getBucketNumber( longVal ) );
           
          System.out.println( "Next Bucket Number: " + SliceBucket.getNextBucketNumber( longVal ) );
          
          System.out.println( "Bucket End Time: " + SliceBucket.getBucketEndTime( longVal ) );

        }

        System.out.print( "\nEnter a number: " );
      }

      System.out.println( "latah" );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

