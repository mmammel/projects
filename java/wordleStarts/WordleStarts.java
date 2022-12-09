
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class WordleStarts {
  private static final String HELP_TEXT = "Find a set of words of length <wordlen> in the wordlist <wordfile> that uniquely contain the given letters <letters>\n"
   + "Usage: java WordleStarts -f <wordfile> [ -l <letters> -s <wordlen> ] | -h[elp]\n"
   + "   -f <wordfile> (required)\n"
   + "      the name of the file containing a lower-case word on each line\n"
   + "   -l <letters> (optional)\n"
   + "      the letter pool to use.  Default: aeiourstlnypghd\n"
   + "   -s <wordlen> (optional)\n"
   + "      the size of words desired.  Default: 5\n"
   + "   -h[elp]\n"
   + "      display this help and exit";
  
  public static void showHelpAndExit() {
    showHelpAndExit(null);
  }
  
  public static void showHelpAndExit( String error ) {
    int exitCode = 0;
    if( error != null ) {
      System.out.println("Bad option: " + error );
      exitCode = 1;
    }
    System.out.println( HELP_TEXT );
    System.exit(exitCode); 
  }
  
  private static String buildDesiredLetterPattern( String letters, int len ) {
    return "^["+letters+"]{" + len + "}$";
  }
  
  private static String buildUniqueLetterPattern( String letters ) {
    StringBuilder retVal = new StringBuilder();
    retVal.append("^.*(?:");
    boolean first = true;
    char tempLet = 0;
    for( int i = 0; i < letters.length(); i++ ) {
      if( first ) {
        first = false;
      } else {
        retVal.append("|");
      }
      
      tempLet = letters.charAt(i);
      
      retVal.append( tempLet ).append(".*").append( tempLet );
    }
    
    retVal.append(").*$");
    
    return retVal.toString();
  }
  
  public static void main( String [] args ) {
    
    String letters = "aeiourstlnypghd";
    String wordLen = "5";
    int wordLenNum = 5;
    String wordFile = null;
    
    try {
      for ( int i = 0; i < args.length; i++ ) {  
        if( args[i].equals( "-f") ) {
          wordFile = args[++i];
        } else if( args[i].equals("-l") ) {
          letters = args[++i];
        } else if( args[i].equals("-s") ) {
          wordLen = args[++i];
        } else if( args[i].matches("^-h(?:e?(?:l?(?:p?)))$") ) {
          showHelpAndExit();
        } else {
          showHelpAndExit( "Bad option: " + args[i] );
        }
      }
    } catch( Exception e ) {
      showHelpAndExit("Error: Bad arguments: " + e.toString() );
    }
    
    try {
      wordLenNum = Integer.parseInt( wordLen );
    } catch( NumberFormatException nfe ) {
      showHelpAndExit("Error: wordLen must be numeric");
    }
    
    // validate
    if( wordFile == null ) {
      showHelpAndExit("Error: No word file specified" );
    } else if( letters.length() == 0 ) {
      showHelpAndExit("Error: Must provide non-zero length list of letters" ); 
    } else if( wordLenNum <= 1 ) {
      showHelpAndExit("Error: wordLen must be 2 or greater" );
    } else if( letters.length() % wordLenNum != 0 ) {
      showHelpAndExit("Error: Number of desired letters must be a multiple of wordLen");
    }
    
    String desiredLetterPattern = buildDesiredLetterPattern( letters, wordLenNum );
    String uniqueLetterPattern = buildUniqueLetterPattern( letters );
    
    BufferedReader input_reader = null;
    String input_str = "";
    int wordCount = 0;
    long letterCount = 0l;
    WordleTrie root = new WordleTrie(null);
    
    System.out.println( "Filename: " + wordFile );
    System.out.println( "letters: " + letters );
    System.out.println( "wordLen: " + wordLenNum );
    System.out.println( "Desired Letter Pattern: " + desiredLetterPattern );
    System.out.println( "Uniqueness Pattern: " + uniqueLetterPattern );
    
    try {
      input_reader = new BufferedReader( new FileReader ( wordFile ) );
      while( (input_str = input_reader.readLine()) != null )
      {
        if( input_str.matches(desiredLetterPattern) && !input_str.matches(uniqueLetterPattern ) ) {
          wordCount++;
          letterCount += input_str.length();
          root.add(input_str);
        }
      }
    } catch( IOException ioe ) {
      System.out.println("Error: " + ioe.toString() );
      System.exit(2);
    }

    System.out.println( "Added " + wordCount + " words with " + letterCount + " letters. Trie has " + root.getCount() + " nodes - " + WordleTrie.getWordCount() + " words in Trie" );
  
    //WordleTrieProcessor printer = new WordleTriePrinter();
    //printer.process(root);
    WordleTrieProcessor finder = new WordleTrieStarterFinder(letters, wordLenNum);
    finder.process(root); 
  }
}
