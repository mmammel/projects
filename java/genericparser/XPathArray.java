import java.util.StringTokenizer;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class XPathArray
{
  private static final String PATH_SEP_SINGLE = "/";
  private static final String PATH_SEP_MANY   = "//";
  private static final String ATTR_INDICATOR  = "@";
  private static final String WILD_CARD       = "*";
  private String [][] mSections = null;
  private String mAttribute     = null;
  private boolean mIsAttr = false;
  private boolean mFloats  = false;
  private int mStepCount = 0;

  public XPathArray( String path )
  {
    this.processPath( path );
  }

  public String [][] getSections()
  {
    return mSections;
  }

  public String getAttributeName()
  {
    return mAttribute;
  }

  public int getStepCount()
  {
    return mStepCount;
  }

  public boolean isAttribute()
  {
    return mIsAttr;
  }

  public boolean isFloater()
  {
    return mFloats;
  }

  public String toString()
  {
    StringBuffer sb = new StringBuffer();
    
    if( mFloats )
    {
      sb.append( PATH_SEP_MANY );
    }
    else
    {
      sb.append( PATH_SEP_SINGLE );
    }

    for( int i = 0; i < mSections.length; i++ )
    {
      for( int j = 0; j < mSections[ i ].length; j++ )
      {
        sb.append( mSections[ i ][ j ] );
        if( j < mSections[ i ].length - 1 )
        {
          sb.append( PATH_SEP_SINGLE );
        }
      }
     
      if( i < mSections.length - 1 )
      {
        sb.append( PATH_SEP_MANY );
      }
    }

    return sb.toString();
  }

  private void processPath( String path )
  {
    StringTokenizer strtok2 = null;
    List majorSections = null;
    Iterator majorSectionIter = null;
    String tempToken1 = null, tempToken2 = null;
    int outerTokenCount = 0, innerTokenCount = 0;

    if( path.startsWith( PATH_SEP_MANY ) )
    {
      mFloats = true;
    }

    path = this.processAttribute( path );

    majorSections = this.tokenizeWithStringDelimiter( path, PATH_SEP_MANY );

    mSections = new String [ majorSections.size() ][];

    majorSectionIter = majorSections.iterator();

    while( majorSectionIter.hasNext() )
    {
      tempToken1 = (String)majorSectionIter.next();
      strtok2 = new StringTokenizer( tempToken1, PATH_SEP_SINGLE );
      mSections[ outerTokenCount ] = new String [ strtok2.countTokens() ];

      innerTokenCount = 0;
      
      while( strtok2.hasMoreTokens() )
      {
        mSections[ outerTokenCount ][ innerTokenCount++ ] = strtok2.nextToken();
        mStepCount++;
      }

      outerTokenCount++; 
    }

    if( mSections[ mSections.length - 1 ][ mSections[ mSections.length - 1 ].length - 1 ].startsWith( ATTR_INDICATOR ) )
    {
      mIsAttr = true;
    }
  }  

  private String processAttribute( String path )
  {
    int attrIdx = 0;
    String attrName = null, pathPart = null;

    attrIdx = path.indexOf( ATTR_INDICATOR );
    
    if( attrIdx >= 0 )
    {
      mIsAttr = true;
      mAttribute = path.substring( attrIdx + 1, path.length() );
      pathPart = path.substring( 0, attrIdx );
    }
    else
    {
      pathPart = path;
    }

    return pathPart;
  }

  private List tokenizeWithStringDelimiter( String source, String delim )
  {
    int currIdx = 0, findIdx = 0;
    int delimiterLength = delim.length();
    List returnList = new ArrayList();
    
    while( findIdx != -1 )
    {
      findIdx = source.indexOf( delim, currIdx );

      if( findIdx == -1 )
      {
        returnList.add( source.substring( currIdx, source.length() ) );
      }
      else
      {
        if( findIdx == 0 )
        {
          currIdx += delimiterLength;
        }
        else if( findIdx > currIdx )
        {
          returnList.add( source.substring( currIdx, findIdx ) );
          currIdx = findIdx + delimiterLength;
        }
      }
    }

    return returnList;
  }

  public static void main( String [] args )
  {
    XPathArray MJMX = null;

    try {

      BufferedReader stdReader = new BufferedReader(
                                 new InputStreamReader( System.in ));

      String tempStr = null;

      while(true) {
        System.out.print("Enter a path to process, or \"quit\" to quit: ");
        tempStr = stdReader.readLine();
        if(tempStr.equals("quit") ) {
          break;
        } else {
          MJMX = new XPathArray( tempStr );
          System.out.println( MJMX.toString() );
          System.out.println( "StepCount: " + MJMX.getStepCount() );
          System.out.println( "Attribute? " + MJMX.isAttribute() );
          if( MJMX.isAttribute() )
          {
            System.out.println( "Attribute name: \"" + MJMX.getAttributeName() + "\"");
          }
          System.out.println( "Floater? " + MJMX.isFloater() );
        }
      }
    } catch(Exception e) {
      e.printStackTrace();
      System.out.println( e.toString() );
    }
  }

}
