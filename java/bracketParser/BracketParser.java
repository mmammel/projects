public class BracketParser
{
  protected String mStr = null;
  protected int mCurrIdx = 0;
  protected String mStart = "[";
  protected String mEnd = "]";
  private static final char QUOTE = '"';
  private static final char ESCAPE = '\\';

  public BracketParser( String str )
  {
    this.mStr = str;
  }

  public BracketParser( String str, String start, String end )
  {
    this.mStr = str;
    this.mStart = start;
    this.mEnd = end;
  }

  public List<Object> process()
  {
    int depth = 0;
    int marker = 0;
    Stack<List> stack = new Stack<List>();
    List<Object> retVal;
    List<Object> tempList;

    for( int i = 0; i < this.mStr.length(); i++ )
    {
      if( this.matchesStart(i) )
      {
        if( stack.empty() )
        {
          marker = i;
        }

        tempList = new ArrayList<Object>();
        stack.push(tempList);
        mCurrIdx += this.mStart.length();
      }
      else if( this.matchesEnd(i) )
      {

        if( stack.empty() )
        {
          retVal.clear();
          logger.error( "Mismatched variable tag: unexpected \"" + this.mEnd + "\" at index " + i );
          break;
        }

        tempList = stack.pop();

        if( stack.empty() )
        {
          retVal.add( this.mStr.substring( marker, i + this.mEnd.length() ) );
        }


      }
    }

    if( depth > 0 )
    {
      retVal.clear();
      logger.error( "Unexpected EOF: unmatched \"" + this.mStart + "\" at index " + marker + "." );
    }

    return retVal;
  }

  protected boolean matchesStart(int i)
  {
    return mStr.indexOf( this.mStart, i ) == i;
  }

  protected boolean matchesEnd(int i)
  {
    return mStr.indexOf( this.mEnd, i ) == i;
  }


}
