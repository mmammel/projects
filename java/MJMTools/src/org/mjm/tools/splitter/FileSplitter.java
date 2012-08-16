package org.mjm.tools.splitter;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.File;
import java.util.regex.*;
import java.util.Properties;
import java.util.List;
import java.util.ArrayList;

public class FileSplitter
{
  private static Properties theProperties = null;
  private static final String PROPS_FILE = "fileSplitter.properties";
  private static final String START_PATTERN_PROP = "start_pattern";
  private static final String END_PATTERN_PROP = "end_pattern";
  private static final String RESULT_DIR_PROP = "result_dir";
  private static final String DIR_PER_SPLIT_PROP = "directory_per_split";
  private static final String RESULT_EXT_PROP = "result_extension";
  private static final String RESULT_NAME_PROP = "result_name";
  private static final String RESULT_HEADER_PROP = "result_header";
  private static final String RESULT_TRAILER_PROP = "result_trailer";
  private static final int MAX_APPENDERS = 25;

  // State related members.
  private BufferedReader mCurrentReader = null;
  private BufferedWriter mCurrentWriter = null;
  private String mCurrentLine = null;
  private String mCurrentResultName = null;
  private State mState = State.INIT;

  // Properties related members.
  private String mResultExtension = null;
  private String mResultName = null;
  private int mResultNameGroup = -1;
  private int mResultCount = 0;
  private String mResultDir = null;
  private boolean mDirPerSplit = false;
  private Pattern mStartPattern = null;
  private Pattern mEndPattern = null;
  private String [] mResultHeaders = null;
  private String [] mResultTrailers = null;

  static
  {
    theProperties = new Properties();

    try
    {
      theProperties.load( new FileInputStream( PROPS_FILE ) );
    }
    catch( IOException ie )
    {
      System.out.println( "Exception while loading properties: " + ie.toString() );
      ie.printStackTrace();
    }
  }

  public enum State
  {
    INIT,
    START,
    READ,
    END,
    DONE,
    INVALID
  }

  public FileSplitter( String configName )
  {
    this.mResultExtension = theProperties.getProperty( configName + "." + RESULT_EXT_PROP, ".txt" );
    this.mResultName = theProperties.getProperty( configName + "." + RESULT_NAME_PROP, "result" );
    this.mResultDir = theProperties.getProperty( configName + "." + RESULT_DIR_PROP, "results" );

    if( mResultName.startsWith( "$" ) )
    {
      this.mResultNameGroup = Integer.parseInt( this.mResultName.substring( 1 ) );
    }

    this.mDirPerSplit = theProperties.getProperty( configName + "." + DIR_PER_SPLIT_PROP, "false" ).equalsIgnoreCase( "true" );
    String tempPattern = theProperties.getProperty( configName + "." + START_PATTERN_PROP );

    if( tempPattern == null )
    {
      this.mStartPattern = Pattern.compile("(?s).*");
      this.mState = State.INVALID;
    }
    else
    {
      this.mStartPattern = Pattern.compile( tempPattern );
    }

    tempPattern = theProperties.getProperty( configName + "." + END_PATTERN_PROP );

    if( tempPattern == null || tempPattern.trim().length() <= 0 )
    {
      this.mEndPattern = null;
    }
    else
    {
      this.mEndPattern = Pattern.compile( tempPattern );
    }

    // Loop through the headers/trailers.
    this.mResultHeaders = this.getMultiProps( configName, RESULT_HEADER_PROP );
    this.mResultTrailers = this.getMultiProps( configName, RESULT_TRAILER_PROP );
  }

  private String [] getMultiProps( String config, String prop )
  {
    String [] retVal = null;
    List<String> tempVals = new ArrayList<String>();
    String prefix = config + "." + prop;
    String temp = null;
    int startIdx = 0;

    temp = theProperties.getProperty( prefix );

    if( temp != null || temp.trim().length() <= 0 )
    {
      tempVals.add( temp );
      startIdx = 1;
    }

    for( int i = startIdx; i < MAX_APPENDERS; i++ )
    {
      if( (temp = theProperties.getProperty( prefix + i )) == null || temp.trim().length() <= 0 )
      {
        break;
      }
      else
      {
        tempVals.add( temp );
      }
    }

    retVal = new String [ tempVals.size() ];

    for( int j = 0; j < retVal.length; j++ )
    {
      retVal[j] = tempVals.get(j);
    }

    return retVal;
  }

  public void process( String fileToSplit ) throws IOException
  {
    if( this.mState == State.INVALID )
    {
      // should be more specific
      System.out.println( "Invalid state - check your properties." );
    }
    else if( this.mState != State.INIT )
    {
      throw new IOException("Bad state to start: " + this.mState );
    }
    else
    {
      this.mCurrentReader = new BufferedReader( new FileReader( fileToSplit ) );
      while( this.processInner() ){};
    }
  }

  public boolean processInner() throws IOException
  {
    boolean retVal = true;

    switch( this.mState )
    {
      case INIT:
        this.processInit();
        break;
      case START:
        this.processStart();
        break;
      case READ:
        this.processRead();
        break;
      case END:
        this.processEnd();
        break;
      case DONE:
        retVal = false;
        break;
      default:
        System.out.println( "Bad State: " + this.mState );
        retVal = false;
        break;
    }

    return retVal;
  }

  private void processInit() throws IOException
  {
    this.mCurrentLine = this.mCurrentReader.readLine();
    this.findStart();
  }

  private void processStart() throws IOException
  {
    this.createResultDir();
    File tempFile = new File( this.createResultFileName() );
    tempFile.createNewFile();

    this.mCurrentWriter = new BufferedWriter( new FileWriter( tempFile ) );

    for( int i = 0; i < this.mResultHeaders.length; i++ )
    {
      this.writeLine( this.mResultHeaders[i] );
    }

    this.writeLine( this.mCurrentLine );

    this.mState = State.READ;
  }

  private void processRead() throws IOException
  {
    Pattern patternToMatch = this.mEndPattern == null ? this.mStartPattern : this.mEndPattern;
    Matcher endMatcher = null;

    while( ( this.mCurrentLine = this.mCurrentReader.readLine()) != null )
    {
      endMatcher = patternToMatch.matcher( this.mCurrentLine );

      if( endMatcher.matches() )
      {
        if( this.mEndPattern != null )
        {
          this.writeLine( this.mCurrentLine );
        }

        this.mState = State.END;
        break;
      }

      this.writeLine( this.mCurrentLine );
    }

    if( this.mCurrentLine == null )
    {
      this.mState = State.END;
    }
  }

  private void processEnd() throws IOException
  {
    for( int i = 0; i < this.mResultTrailers.length; i++ )
    {
      this.writeLine( this.mResultTrailers[i] );
    }

    this.mCurrentWriter.flush();
    this.mCurrentWriter.close();

    if( this.mCurrentLine == null )
    {
      this.mState = State.DONE;
    }
    else
    {
      // this call sets the next state.
      this.findStart();
    }
  }

  private void writeLine( String str ) throws IOException
  {
    this.mCurrentWriter.write( str, 0, str.length() );
    this.mCurrentWriter.newLine();
  }

  private void createResultDir()
  {
    String path = this.mResultDir;

    if( this.mDirPerSplit )
    {
      path += File.separator + this.mCurrentResultName;
    }

    File tempDir = new File(path);

    if( !tempDir.exists() )
    {
      tempDir.mkdirs();
    }
  }

  private String createResultFileName()
  {
    return this.mResultDir + File.separator +
           (this.mDirPerSplit ? this.mCurrentResultName + File.separator : "") +
           this.mCurrentResultName + this.mResultExtension;
  }

  private void findStart() throws IOException
  {
    boolean startFound = false;
    Matcher startMatcher = null;

    do
    {
      startMatcher = this.mStartPattern.matcher( this.mCurrentLine );

      if( startMatcher.matches() )
      {
        startFound = true;

        if( this.mResultNameGroup == -1 )
        {
          this.mCurrentResultName = this.mResultName + "-" + this.mResultCount++;
        }
        else
        {
          this.mCurrentResultName = startMatcher.group(this.mResultNameGroup);
        }

        break;
      }
    }
    while( (this.mCurrentLine = this.mCurrentReader.readLine()) != null );

    if( !startFound )
    {
      this.mState = State.DONE;
    }
    else
    {
      this.mState = State.START;
    }
  }

  public static void main( String [] args )
  {
    FileSplitter FS = null;

    if( args.length != 2 )
    {
      System.out.println( "Usage: FileSplitter <config> <filename>" );
      System.exit(1);
    }

    try
    {
      FS = new FileSplitter(args[0]);
      FS.process( args[1] );
    }
    catch( IOException ioe )
    {
      System.out.println( "Caught an exception: " + ioe.toString() );
      ioe.printStackTrace();
    }
  }
}
