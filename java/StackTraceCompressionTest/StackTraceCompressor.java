import java.util.*;
import java.io.*;

public class StackTraceCompressor
{
  public static void main( String [] args )
  {
     if( args.length != 1 )
     {
       System.out.println( "Usage: java StackTraceCompressor <input_file>" );
       System.exit(1);
     }

     String fileName = args[0];
     BufferedReader reader  = null;
     int frameCount = 0;
     int hits = 0;
     Set compressed = new HashSet();

     try
     {
       String tempFrame = null;
       reader = new BufferedReader( new FileReader( fileName ) );

       while( (tempFrame = reader.readLine()) != null )
       {
         frameCount++;

         if( !compressed.add( tempFrame ) )
         {
           hits++;
         }
       }

       long origBytes = frameCount * ( 100 + 38 + 255 );
       long compressedBytes = (frameCount * ( 100 + 38 + 38 )) + (compressed.size() * ( 38 + 255 ));
       double percentCompression = 100.0 - (100.0 * ( new Double(compressedBytes).doubleValue() / new Double( origBytes ).doubleValue() ) );

       System.out.println( "\nReport\n------\n" +
                           "Number of original frames: " + frameCount + "\n" +
                           "Size of compressed set: " + compressed.size() + "\n" +
                           "Number of hits: " + hits + "\n" +
                           Math.round(percentCompression) + "% compression (" + origBytes + " -> " + compressedBytes + ")\n"); 

     }
     catch( Exception e )
     {
       System.out.println( "Caught an Exception: " + e.toString() );
       e.printStackTrace();
     }
  }
}
