import java.io.*;

public class FileWrapper
{
  public static final String WRAP_CMD = "wrap";
  public static final String UNWRAP_CMD = "unwrap";

  int mWidth = 0;
  String mCommand = null;
  File mInputFile = null;
  FileInputStream mFInStream = null;
  File mOutputFile = null;
  FileOutputStream mFOutStream = null;

  public FileWrapper( String inputFile, String outputFile, String cmd, int width )
  {
    try
    {
      mInputFile = new File( inputFile );
      mFInStream = new FileInputStream( mInputFile );
      mOutputFile = new File( outputFile );
      mFOutStream = new FileOutputStream( mOutputFile );
      mWidth = width;
      mCommand = cmd;
    }
    catch( Exception e )
    {
      System.out.println( "Caught an Exception: " + e.toString() );
      e.printStackTrace();
    }
  }

  private void unwrap() throws IOException
  {
    int count = 0;
    int readByte = 0;
    char tempChar = (char)0;

    while( (readByte = mFInStream.read()) != -1 )
    {
      if( ++count%(mWidth+1) != 0 )
      {
        if( readByte == '\r' || readByte == '\n' )
        {
          count = 0;
        }
        else
        {
          mFOutStream.write( readByte );
        }
      } 
      else if( readByte == '\r' )
      {
        readByte = mFInStream.read();
        if( readByte != '\n' )
        {
          count++;
          mFOutStream.write( readByte );
        }
      }
    }
  }

  public void wrap() throws IOException
  {
    int count = 0;
    int readByte = 0;
                                                                                
    while( (readByte = mFInStream.read()) != -1 )
    {
      if( count == mWidth )
      {
        count = 0;
        mFOutStream.write( '\r' );
        mFOutStream.write( '\n' );
      }

      if( readByte != '\r' && readByte != '\n' )
      {
        mFOutStream.write( readByte );
        count++;
      }
    }
  }

  public void run()
  {
    try
    {
      if( mCommand.equalsIgnoreCase( WRAP_CMD ) )
      {
        this.wrap();
      }
      else if( mCommand.equalsIgnoreCase( UNWRAP_CMD ) )
      {
        this.unwrap();
      }
      else
      {
        mInputFile.renameTo( mOutputFile );
      }
    }
    catch( IOException ioe )
    {
      System.out.println( "Caught an IO Exception: " + ioe.toString() );
      ioe.printStackTrace();
    }
    finally
    {
      try
      {
        mFInStream.close();
        mFOutStream.close();
      }
      catch( IOException ioe2 )
      {
        System.out.println( "Caught an IO Exception: " + ioe2.toString() );
        ioe2.printStackTrace();
      }
    }
  }

  public static void main( String [] args )
  {
    FileWrapper FW = null;
    if( args.length != 4 )
    {
      System.out.println( "Usage: java FileWrapper <inFile> <outFile> [wrap|unwrap] <width>" );
    }
    else
    {
      FW = new FileWrapper( args[0], args[1], args[2], Integer.parseInt( args[3] ) );
      FW.run();
    }
  }

}
