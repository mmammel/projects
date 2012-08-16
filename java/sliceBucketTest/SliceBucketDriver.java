import java.io.BufferedReader;
import java.io.InputStreamReader;

import net.sf.ehcache.CacheManager;

public class SliceBucketDriver
{
  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    String input_str = "";
    long startTime = 0L;
    long endTime = 0L;
    
    SliceCache sliceCache = new SliceCache("sampleCache3");
    SliceCacheReader reader = null;
    Slice proto = null;
    Slice tempSlice = null;
    
    int count = 0;
    
    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      //System.out.println( System.getProperty("java.io.tmpdir"));
      System.out.println( "Welcome to the Slice Bucket tester." );

      System.out.print("$ ");

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "quit" ) )
        {
          break;
        }
        else
        {
          if( input_str.startsWith("query"))
          {
            count = 0;
            startTime = Long.parseLong(input_str.substring(input_str.indexOf(" ") + 1, input_str.lastIndexOf(" ")));
            endTime = Long.parseLong(input_str.substring(input_str.lastIndexOf(" ") + 1));
            proto = new Slice( SliceDataBase.HOST, SliceDataBase.JVM, startTime, endTime );
            reader = sliceCache.getReader(proto);
            
            while( (tempSlice = reader.read()) != null )
            {
              count++;
              System.out.println( "Got: " + tempSlice.getStartTime() + ", " + tempSlice.getEndTime() );
            }
            
            System.out.println( "Got: " + count + " slices.");
          }

        }

        System.out.print( "\n$ " );
      }

      System.out.println( "Nice work." );
      CacheManager.getInstance().shutdown();
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

