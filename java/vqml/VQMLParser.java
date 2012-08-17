import java.util.List;
import java.util.ArrayList;

/**
 * This class breaks down a VQML query into chunks of plain query text
 * and DynamicField specifications.  These chunks can be sent to the appropriate
 * builders to build out a complete VQML Tree.
 */
class VQMLParser {

  private StringBuilder characters = new StringBuilder();

  private int currPos = 0;

  public List<String> parse( String vquery )
  {
    char currChar = 0;
    List<String> retVal = new ArrayList<String>();

    while( this.currPos < vquery.length() )
    {
      currChar = vquery.charAt(this.currPos);

      if( vquery.indexOf("${",this.currPos ) == this.currPos )
      {
        retVal.add( this.characters.toString() );
        this.characters = new StringBuilder();
        //Found the start of a dynamic field, consume until we find the closing bracket.
        retVal.add(this.consumeDynamicField(vquery));
      }
      else
      {
        this.characters.append(currChar);
        this.currPos++;
      }
    }

    if( this.characters.toString().length() > 0 )
    {
      retVal.add( this.characters.toString() );
    }

    return retVal;
  }

  // The pointer is pointing at "${"
  private String consumeDynamicField(String vquery)
  {
    StringBuilder retVal = new StringBuilder();
    int bracketDepth = 0;
    char currChar = 0;
    while( this.currPos < vquery.length() )
    {
      currChar = vquery.charAt( this.currPos );
      if( vquery.indexOf("${",this.currPos ) == this.currPos )
      {
        retVal.append("${");
        this.currPos += 2;
        bracketDepth++;
      }
      else if( currChar == '}' )
      {
        retVal.append("}");
        this.currPos++;
        bracketDepth--;
      }
      else
      {
        retVal.append(currChar);
        this.currPos++;
      }

      if( bracketDepth == 0 ) break;
    }

    return retVal.toString();
  }

  public static void main( String [] args )
  {
    VQMLParser vp = new VQMLParser();

    List<String> result = vp.parse( args[0] );

    for( String res : result )
    {
      System.out.println( res );
    }
  }


}