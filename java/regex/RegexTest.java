import java.util.regex.*;

public class RegexTest
{
  public static final String DEST_REGEX = "[Dd]\\d+\\d*";
  public static final Pattern DEST_PATTERN = Pattern.compile(DEST_REGEX);

  public static final String FILE_REGEX = "[^\\/\\\\.]*.";
  public static final Pattern FILE_PATTERN = Pattern.compile(FILE_REGEX);

  public static final String SPACE_REGEX = "\\s+";
  public static final Pattern SPACE_PATTERN = Pattern.compile(SPACE_REGEX);

  public static final String SOURCE_REGEX = "[Ss]\\d+\\d*";
  public static final Pattern SOURCE_PATTERN = Pattern.compile(SOURCE_REGEX);

  public String getFileNameFromPath( String path )
  {
    String retVal = null;
    Matcher matcher = FILE_PATTERN.matcher( path );
    
    if( matcher.find() )
    {
      retVal = matcher.group();
    }
    
    return retVal;
  }

  public String getSourceRecordFromRule( String rule )
  {
    String retVal = null;
    Matcher matcher = SOURCE_PATTERN.matcher( rule );
    
    if( matcher.find() )
    {
      retVal = matcher.group();
    }
    
    return retVal;
  }

  public String getSpaceFreeString( String arg )
  {
    Matcher matcher = SPACE_PATTERN.matcher( arg );
    String retVal = matcher.replaceAll( "_" );

    return retVal;
  }

  public static void main( String [] args )
  {
    /*Matcher matcher = DEST_PATTERN.matcher( args[0] );

    while( matcher.find() )
    {
      System.out.println( "Found (dest): " + matcher.group() );
    }
    
    matcher = FILE_PATTERN.matcher( args[0] );
    
    while( matcher.find() )
    {
      System.out.println( "Found (file): " + matcher.group() );
    }*/
    
    RegexTest RE = new RegexTest();
    
    //System.out.println(RE.getFileNameFromPath( args[0]));
    //System.out.println(RE.getSpaceFreeString( args[0]));
    System.out.println(RE.getSourceRecordFromRule( args[0] ) );
    
  }




}
