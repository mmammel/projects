import java.util.List;
import java.util.ArrayList;

public class ParenStringTokenizer
{
  public static final char LPAREN = '(';
  public static final char RPAREN = ')';
  public static final char COMMA = ',';

  public List<String> tokenize( String parenString )
  {
    List<String> retVal = new ArrayList<String>();

    boolean inStr = false;
    StringBuffer tempBuff = new StringBuffer();

    for( char c : parenString.toCharArray() )
    {
      if( !inStr && Character.isSpaceChar(c) ) continue;

      switch(c)
      {
        case LPAREN:
          this.handleStringEnd( inStr, tempBuff, retVal );
          inStr = false;
          retVal.add( new String( "" + LPAREN ) );
          break;
        case RPAREN:
          this.handleStringEnd( inStr, tempBuff, retVal );
          inStr = false;
          retVal.add( new String( "" + RPAREN ) );
          break;
        case COMMA:
          this.handleStringEnd( inStr, tempBuff, retVal );
          inStr = false;
          retVal.add( new String( "" + COMMA ) );
          break;
        default:
          if( !inStr )
          {
            tempBuff = new StringBuffer();
            inStr = true;
          }
          
          tempBuff.append(c);
          break;
      }
    }

    this.handleStringEnd( inStr, tempBuff, retVal );
    
    return retVal;
  }

  private void handleStringEnd( boolean inStr, StringBuffer buff, List<String> tokens )
  {
    if( inStr )
    {
      tokens.add(buff.toString());
    }
  }
  
  public static void main( String [] args )
  {
    ParenStringTokenizer PST = new ParenStringTokenizer();
    
    List<String> toks = PST.tokenize( args[0] );
    
    for( String str : toks )
    {
      System.out.println( str );
    }
  }
}