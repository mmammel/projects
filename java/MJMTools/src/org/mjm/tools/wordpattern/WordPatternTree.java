package org.mjm.tools.wordpattern;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.mjm.tools.lookuptree.*;

public class WordPatternTree
{
	private Map<String,LookupTree> mTree = new HashMap<String,LookupTree>();

	public WordPatternTree( String dictionaryFile )
	{
		String tempStr = null;
		File inFile = null;
		BufferedReader br = null;

		try
		{
		  inFile = new File(dictionaryFile);

		  br = new BufferedReader(
				new InputStreamReader(
				 new FileInputStream(inFile)));

		}
		catch(FileNotFoundException fe)
		{
		  System.out.println( fe.toString() );
		  return;
		}

		try
		{
	      LookupTree tempLookupTree;
	      String pattern;
		  while( (tempStr = br.readLine()) != null )
		  {
			  pattern = this.getPattern( tempStr );
			  if( (tempLookupTree = this.mTree.get( pattern )) == null )
			  {
				  tempLookupTree = new LookupTree();
				  tempLookupTree.addWord( tempStr );
				  this.mTree.put( pattern, tempLookupTree );
			  }
			  else
			  {
				  tempLookupTree.addWord( tempStr );
			  }
		  }

		}
		catch(IOException ioe)
		{
		  System.out.println( ioe.toString() );
		}
	}

	public void printWordsForPattern( String pattern )
	{
		String patt = this.getPattern( pattern );
		LookupTree tree = this.mTree.get( patt );

		if( tree != null )
		{
			tree.printTable();
		}
		else
		{
			System.out.println( "Pattern not found!" );
		}
	}

	public void printWordsForPattern( String pattern, String hint )
	{
		String patt = this.getPattern( pattern );
		LookupTree tree = this.mTree.get( patt );

		if( tree != null )
		{
			tree.processWords( new MatchingWordPrinter( hint ) );
		}
		else
		{
			System.out.println( "Pattern not found!" );
		}
	}

  public List<String> collectWordsWithHint( String pattern, String hint )
  {
    List<String> retVal = null;
    String patt = this.getPattern(pattern);
    LookupTree tree = this.mTree.get( patt );
    WordCollector collector = new WordCollector(); 

    if( tree != null )
    {
      tree.processWords( collector, hint );
      retVal = collector.getWords();
    }  

    return retVal;    
  }

        public List<String> collectWordsForPattern( String pattern )
        {
          List<String> retVal = null;
          String patt = this.getPattern(pattern);
          LookupTree tree = this.mTree.get( patt );
          WordCollector collector = null;
 
          if( tree != null )
          {
            collector = new WordCollector();
            tree.processWords(collector);
            retVal = collector.getWords();
          }  

          return retVal;
        }

        public List<String> collectWordsForPattern( String pattern, String hint )
        {
          List<String> retVal = null;
          String patt = this.getPattern(pattern);
          LookupTree tree = this.mTree.get( patt );
          WordCollector collector = new WordCollector( hint ); 

          if( tree != null )
          {
            tree.processWords( collector );
            retVal = collector.getWords();
          }  

          return retVal;
        }

	public String getPattern( String word )
	{
		Map<Character,Character> seen = new HashMap<Character,Character>();
		StringBuilder retVal = new StringBuilder();

		Character c;
		Character s;
		char replace = 'a';
		word = word.toLowerCase();
		for( int i = 0; i < word.length(); i++ )
		{
			c = word.charAt(i);
			if( c == '\'' )
			{
				retVal.append( c );
			}
			else if( (s = seen.get(c)) == null )
			{
				retVal.append( replace );
				seen.put( c, replace );
				replace++;
		    }
		    else
		    {
				retVal.append( s );
			}
		}

		return retVal.toString();
	}

	public static void main( String [] args )
	{
		WordPatternTree WPT = new WordPatternTree( args[0] );

		try
		{
		  BufferedReader stdReader = new BufferedReader(
									 new InputStreamReader( System.in ));

		  String tempStr = null;
		  String [] inputs = null;

		  while(true)
		  {
			System.out.print("Enter a pattern,regex to lookup, or \"quit\" to quit: ");
			tempStr = stdReader.readLine();
			inputs = tempStr.split(",");
			if(inputs[0].equals("quit") )
			{
			  break;
			}
			else
			{
			  WPT.printWordsForPattern( inputs[0],inputs[1] );
			}
		  }
		}
		catch(Exception e)
		{
		  e.printStackTrace();
		  System.out.println( e.toString() );
		}
	}

}
