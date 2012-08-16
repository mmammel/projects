import java.io.IOException;

public class QuickZipDriver
{
  public static void main( String [] args )
  {
    if( args.length < 1 || args.length > 2 )
    {
       System.out.println( "Usage: QuickZipDriver <file_to_zip> [<output_file>]" );
    }
    else
    {
      try
      {
        if( args.length == 1 )
        {
          QuickZip.zipSingleFile( args[0] );
        }
        else
        {
          QuickZip.zipSingleFile( args[0], args[1] );
        }
      }
      catch( IOException ioe )
      {
        System.out.println( "Caught an exception: " + ioe.toString() );
      }
    }
  }
}