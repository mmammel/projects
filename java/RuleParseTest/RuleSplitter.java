

import java.io.*;
import java.util.*;


public class RuleSplitter
{
  public void splitMapRule( String rule )
  {  
    String source = rule.substring( rule.indexOf( "MAP " ) + 3, rule.indexOf( " TO " ) );
    String targets = rule.substring( rule.indexOf( " TO " ) + 4 );
    
    StringTokenizer tok = new StringTokenizer( targets );
    
    while( tok.hasMoreTokens() )
    {
      System.out.println( "   " + tok.nextToken() + " = " + source + "\n" );
    }
  }

  public static void main( String [] args )
  {
    RuleSplitter RS = new RuleSplitter();
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

        if( inputStr.startsWith("MAP") )
        {
          RS.splitMapRule( inputStr );
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