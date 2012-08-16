import java.io.BufferedReader;
import java.io.InputStreamReader;

public class XPathStack 
{
  private static final int INITIAL_ELEMENT_ARRAY_SIZE = 32;

  protected int mElementCount = 0;
  protected String [] mElements = new String [ INITIAL_ELEMENT_ARRAY_SIZE ];

  public void push( String element )
  {
    if( mElementCount == mElements.length )
    {
      this.expandElementArray();
    }

    mElements[ mElementCount++ ] = element;
  }

  private void expandElementArray()
  {
    String [] newArray = new String [ mElements.length + INITIAL_ELEMENT_ARRAY_SIZE ];

    for( int i = 0; i < mElementCount; i++ )
    {
      newArray[ i ] = mElements[ i ];
    }

    mElements = newArray;
  }

  public String pop()
  {
    String returnEle = null;
 
    if( mElementCount > 0 )
    {
      returnEle = mElements[ mElementCount-- ];
    }

    return returnEle;
  }

  StringArrayComparator mComparator = new StringArrayComparator();

  public boolean compareWithXPath( XPathArray path )
  {
    boolean returnVal = false;

    if( mElementCount >= path.getStepCount() )
    {
      if( path.isFloater() )
      {
        returnVal = this.compareFloater( path );
      }
      else
      {
        returnVal = this.compareNonFloater( path );
      }
    }

    return returnVal;
  }

  private boolean compareFloater( XPathArray path )
  {
    boolean retVal = false;

    String [][] sections = path.getSections();

    String [] last = sections[ sections.length - 1 ];
    
    int idx = mComparator.lastIndexOf( last, mElements, mElementCount - 1 );

    if( idx != -1 && idx == (mElementCount - last.length) )
    {
      if( sections.length == 1 )
      {
        retVal = true;
      }
      else
      {
        retVal = findFit( sections, 0, sections.length - 2, 0, idx );
      }
    } 

    return retVal;
  }

  private boolean compareNonFloater( XPathArray path )
  {
    boolean retVal = false;

    String [][] sections = path.getSections();

    String [] last  = sections[ sections.length - 1 ];
    String [] first = sections[ 0 ];

    int lastidx  = mComparator.lastIndexOf( last, mElements, mElementCount - 1 );
    int firstidx = mComparator.indexOf( first, mElements );

    if( lastidx == (mElementCount - last.length) && firstidx == 0 )
    {
      if( sections.length == 1 || sections.length == 2 )
      {
        retVal = true;
      }
      else
      {
        retVal = findFit( sections, 1, sections.length - 2, first.length, lastidx );
      }
    }

    return retVal;
  }

  private boolean findFit( String [][] fitMe, int currArrayIdx, int maxArrayIdx, int from, int to )
  {
    boolean retVal = false;
    int idx = 0;

    while( idx != -1 )
    {
      idx = mComparator.indexOf( fitMe[ currArrayIdx ], mElements, from, to );

      if( idx != -1 )
      {
        if( currArrayIdx < maxArrayIdx )
        {
          retVal = this.findFit( fitMe, currArrayIdx + 1, maxArrayIdx, idx + fitMe[ currArrayIdx ].length, to );
        }
        else
        {
          retVal = true;
        }

        if( retVal )
        {
          break;
        }

        from += fitMe[ currArrayIdx ].length;
      }
    }
  
    return retVal;
  }

  public static void main( String [] args )
  {
    try
    {
      XPathStack XPS = new XPathStack();
      XPS.push( "aaa" );
      XPS.push( "bbb" );
      XPS.push( "ccc" );
      XPS.push( "ddd" );
      XPS.push( "eee" );
      XPS.push( "eee" );
      XPS.push( "eee" );
      XPS.push( "fff" );

      BufferedReader stdReader = new BufferedReader(
                                 new InputStreamReader( System.in ));

      String tempStr = null;

      while(true) {
        System.out.print("Enter an xpath, or \"quit\" to quit: ");
        tempStr = stdReader.readLine();
        if(tempStr.equals("quit") ) {
          break;
        } else {
          System.out.println( tempStr );
          System.out.println( XPS.compareWithXPath( new XPathArray( tempStr ) ) );
        }
      }

    }
    catch( Exception e )
    {
      e.printStackTrace();
      System.out.println( e.toString() );
    }

  }
    

}
