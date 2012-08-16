import java.util.*;
import java.util.regex.*;
import java.io.*;

public class RuleParse
{
  public static final int MAX_TOKENS = 32;
  public static final int UNKNOWN_TYPE = -1;
  public static final int STRING_TYPE = 1;
  public static final int INTEGER_TYPE = 2;
  public static final String OPERATORS = "*&()-+/";
  public static final String OPERATORS_REGEX = ".*[()+-/&*].*";
  public static final Pattern OPERATORS_PATTERN = Pattern.compile(OPERATORS_REGEX);
  public static Matcher OPERATORS_MATCHER = OPERATORS_PATTERN.matcher("");

  public String processWhenRule( List tokens, RuleObject rule )
  {
    //assumes a list of tokens, the first of which is "WHEN"
    int tokenCount = tokens.size();
    int currentIdx = 1;  //skip the WHEN
    String currentToken = null;
    StringBuffer translated = new StringBuffer();
    
    while( currentIdx < tokenCount )
    {
      currentToken = (String)tokens.get(currentIdx);
      
      if( currentToken.equals( "data" ) )
      {
        currentIdx += 3;
        translated.append( "exist(#" + (String)tokens.get(currentIdx) + ")" );
      }
      else if( currentToken.equalsIgnoreCase("or") )
      {
        translated.append( " |\n" );
      }
      else if( currentToken.equalsIgnoreCase("and") )
      {
        translated.append( " &\n" );
      }
      else if( currentToken.startsWith("'") )
      {
        translated.append( "\"" + currentToken.substring( 1, currentToken.length() - 1 ) + "\"" );
      }
      else if( currentToken.startsWith("$") )
      {
        translated.append( currentToken.substring(1) );
      }
      else if( currentToken.equalsIgnoreCase( "equals" ) || currentToken.equals("=") )
      {
        translated.append( " = " );
      }
      else if( currentToken.startsWith("S") || currentToken.startsWith("D") )
      {
        translated.append( "#" + currentToken );
      }
      
      currentIdx++;
    }
    
    return translated.toString();
  }

  public String processMapRule( String ruleStr, RuleObject rule ) throws Exception
  {
    String assignSource = ruleStr.substring( 0, ruleStr.indexOf( " TO " ) ).trim();
    System.out.println( "assignSrc: " + assignSource );
    String assignTarget = ruleStr.substring( ruleStr.indexOf( " TO " ) ).trim();
    System.out.println( "target: " + assignTarget );
    AssignSource sourceResult = null;
    StringBuffer variables = new StringBuffer();
    StringBuffer tempAssigns = new StringBuffer();
    
    List sourceTokens = this.tokenize( assignSource );
    
    sourceResult = this.processMapRuleSource( sourceTokens.iterator(), variables, tempAssigns );
    return variables.toString() + "\n" + tempAssigns.toString() + "\n" + sourceResult.asString;
  }

  public AssignSource processMapRuleSource( Iterator tokenIter, RuleObject rule ) throws Exception
  {
    String currentToken = null;
    StringBuffer retVal = new StringBuffer();

    while( tokenIter.hasNext() )
    {
      currentToken = (String)tokenIter.next();
      
      System.out.println( "Processing token: \"" + currentToken + "\"" );
      
      if( currentToken.equalsIgnoreCase( "MAP" ) )
      {
        //do nothing
        continue;
      }
      else if( currentToken.equalsIgnoreCase( ";" ) )
      {
        //do nothing
        continue;
      }
      else if( currentToken.equalsIgnoreCase( "generic" ) || currentToken.equalsIgnoreCase( "specific" ) )
      {
        // MAP [ generic | specific ] synonym of source S798 using the list named 'CA_P19QUAL' ; TO D457 $S798 ...
        String tempSynVarName = null, codeListName = null, codeListSource = null;
        for( int i = 0; i < 3; i++ ) tokenIter.next();
        codeListSource = (String)tokenIter.next();
        for( int i = 0; i < 4; i++ ) tokenIter.next();
        currentToken = (String)tokenIter.next();
        codeListName = currentToken.substring(1, currentToken.length() - 1 );
        tempSynVarName = codeListSource + "_Syn";
        variables.append( "string [32] " + tempSynVarName + ";\n" );
        temp_assigns.append("select TEXT1 into " + tempSynVarName + " from CODELIST where NAME = &quot;" +
                           codeListName + "&quot; and SENDERCODE = #" + codeListSource + ";\n" );
        retVal.append(tempSynVarName).append(" ");
      }

      else if( currentToken.startsWith("$") )
      { 
        // global variable
        retVal.append(currentToken.substring(1) + " ");
      }
      else if( currentToken.startsWith( "D" ) )
      { 
        // destination field
        //TODO: Get type of Destination field
        retVal.append( "addressOf(" + currentToken + ") " );
      }
      else if( currentToken.startsWith( "S" ) )
      { 
        // source field
        retVal.append("#" + currentToken + " ");
          //TODO: get type of source field
      }
      else if( currentToken.startsWith( "'" ) )
      {
        // String literal
        retVal.append("\"" + currentToken.substring( 1, currentToken.length() - 1 ) + "\" ");
      }
      else if( currentToken.equals("&") )
      {
        //string append
        retVal.append("+ ");
        //TODO: set source type to string
      }
      else if( currentToken.equals("+") )
      {
        //addition
        retVal.append("+ ");
      }
      else if( currentToken.equals("-") )
      {
        // subtraction
        retVal.append("- ");
      }
      else if( currentToken.equals( "/" ) )
      {
        // division
        retVal.append("/ ");
      }
      else if( currentToken.equals( "*" ) )
      {
        retVal.append("* ");
      }
      else if( currentToken.equals( "(" ) )
      {
        retVal.append( "( " + this.processMapRuleSource( tokenIter, rule ) + " ) " );
      }
      else if( currentToken.equals( ")" ) )
      {
        break;
      }
      else
      {
        System.out.println("Found an unexpected token: \"" + currentToken + "\"" );
        retVal.append(currentToken + " ");
      }
      
    }
   
    return retVal.toString().trim();
  }

  private List tokenize( String rule )
  {
    List retList = new ArrayList();
    StringBuffer tempToken = new StringBuffer();
    boolean foundOperator = false;
    char currentChar = 0;
    int i = 0;
    
    while( i < rule.length() )
    {
      currentChar = rule.charAt( i );
      
      if( OPERATORS.indexOf( currentChar ) >= 0 )
      {
        if( tempToken.length() > 0 )
        {
          retList.add( tempToken.toString() );
          tempToken = new StringBuffer();
        }        
        
        retList.add( new String( "" + currentChar ) );
      }
      else if( Character.isWhitespace( currentChar ) )
      {
        if( tempToken.length() > 0 )
        {
          retList.add( tempToken.toString() );
          tempToken = new StringBuffer();
        }  
      }
      else if( currentChar == '\'' )
      {
        System.out.println( "Found a single quote in rule: " + rule );
        if( tempToken.length() > 0 )
        {
          retList.add( tempToken.toString() );
          tempToken = new StringBuffer();
        }
        
        tempToken.append(currentChar);
        i++;
        while( (currentChar = rule.charAt(i++)) != '\'' )
        {
          System.out.println( "Appending: \"" + currentChar + "\"" ); 
          tempToken.append( currentChar );
        }
        
        tempToken.append( "'" );
        continue;
      }
      else
      {
        tempToken.append( currentChar );
      }
      
      i++;
    }
    
    if( tempToken.length() > 0 )
    {
      retList.add( tempToken.toString() );
    }
    
    return retList;
  }

  public class AssignSourceElement
  {
    public int type = UNKNOWN_TYPE;
    private StringBuffer text = new StringBuffer();
    
    public void append( String str )
    {
      text.append(str).append( " " );
    }
   
  }

  public class AssignSource
  {
    private List sourceElements = new ArrayList();
    
    public void addElement( AssignSourceElement element )
    {
      sourceElements.add( element );
    }
  }

  public class AssignTarget
  {
    public int type = UNKNOWN_TYPE;
    public String name = null;
  }

  public class AssignmentContainer
  {
    private List assignTargets = new ArrayList();
    private List assignSources = new ArrayList();
    
    public void addTarget( AssignTarget target )
    {
      assignTargets.add( target );
    }
    
    public void addSource( AssignSource source )
    {
      assignSources.add( source );
    }
  }

  public class RuleObject
  {
    public StringBuffer variables = new StringBuffer();
    public StringBuffer tempAssigns = new StringBuffer();
    public StringBuffer conditions = new StringBuffer();
    public StringBuffer body = new StringBuffer();
    
    public addVariable( String variable )
    {
      variables.append( variable ).append( "\n" );
    }
    
    public addTempAssign( String tempassign )
    {
      tempAssigns.append( tempassign ).append( "\n" );
    }
    
    public addCondition( String condition )
    {
      conditions.append( condition ).append( "\n" );
    }
    
    public addBodyElement( String element )
    {
      body.append( element ).append( "\n" );
    }
    
    public String toString()
    {
      StringBuffer result = new StringBuffer();
      
      result.append( variables.toString() ).append( "\n" );
      
      if( conditions.length() > 0 )
      {       
        result.append( "if " ).append( conditions.toString() ).append( " then\nbegin\n" );
      }
      
      result.append( tempAssigns.toString() ).append( "\n" );
      result.append( body.toString() ).append( "\n" );
      
      if( conditions.length() > 0 )
      {       
        result.append( "end\n" );
      }
      
      return result.toString();
      
    }
  }
  


  public static void main( String [] args )
  {
    RuleParse rp = new RuleParse();
    String inputStr = null;
    List tempList = null;
    BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );
    
    try
    { 
      while( (inputStr = reader.readLine()) != null )
      {
        inputStr = inputStr.trim();
        System.out.println( "--------" );
        System.out.println( "| RULE |" );
        System.out.println( "--------" );
        System.out.println( "[ " + inputStr + " ]" );

        if( inputStr.startsWith("WHEN") )
        {
          System.out.println( rp.processWhenRule( rp.tokenize(inputStr) ) );
        }
        else if( inputStr.startsWith("MAP") )
        {
          System.out.println( rp.processMapRule( inputStr ) );
        }
     
      }
    }
    catch( Exception ioe )
    {
      System.out.println( "Exception: " + ioe.toString() );
      ioe.printStackTrace();
    }
  }

}
