
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * A collection of LookupTreeNodes that represents a list of words
 * layed out in such a way that incremental lookup by letter is convenient.
 */
public class LookupTree 
{
  private LookupTreeNode mRootNode = new LookupTreeNode();

  public void addWord(String word) 
  {
    LookupTreeNode nodeIter = mRootNode, tempIter = null;
    char currentChar = (char)0;

    for(int i = 0; i < word.length(); i++) 
    {
      currentChar = word.charAt(i);

      if( (tempIter = nodeIter.getNodeForLetter(currentChar)) == null ) 
      {
        nodeIter = nodeIter.addLetter( currentChar, false );
      } 
      else 
      {
        nodeIter = tempIter;
      }
    }

    if( nodeIter.getIsWord() ) 
    {
      System.out.println( "Word: " + word + " already exists in table" );
    } 
    else 
    {
      nodeIter.setIsWord(true);
    }
  }

  /**
   * Given a file containing a list or words seperated by newlines fill up the
   * tree with those words.
   * @param fileName The name of the file to read words from.
   */
  public void loadWordsFromFile(String fileName) 
  {
    String tempStr = null;
    File inFile = null;
    BufferedReader br = null;

    try 
    {
      inFile = new File(fileName);

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
      while( (tempStr = br.readLine()) != null ) 
      {
        this.addWord( tempStr );
      }
    } 
    catch(IOException ioe) 
    {
      System.out.println( ioe.toString() );
    }
  }

  /**
   * Given String return true if the word is in the tree, false otherwise.
   * @param word The word to check the existence of.
   * @returns boolean true if the word is in the tree, false otherwise.
   */
  public boolean wordExists( String word ) 
  {
    LookupTreeNode nodeIter = mRootNode;
    boolean returnVal = false;
    char currentChar = (char)0;

    for( int i = 0; i < word.length(); i++) 
    {
      currentChar = word.charAt(i);
      nodeIter = nodeIter.getNodeForLetter(currentChar);
      if( nodeIter == null ) 
      {
        break;
      }
    }

    if( nodeIter != null && nodeIter.getIsWord() ) 
    {
      returnVal = true;
    }

    return returnVal;
  }

  /**
   * Walk the table and perform an action on each word. Order is not guaranteed.
   * @param node The node to start from.
   * @param currWord The starting String, used for recursion
   * @param action A method in the form void action(String arg) to be performed on each string found
   */
  private void walkTable(LookupTreeNode node, String currWord, Method action) 
  {
    Iterator iter = node.getLetterIterator();
    LookupTreeNode nextNode = null;
    Object [] arg = new Object[1];
    StringBuffer currentWord = new StringBuffer(currWord);
    currentWord.setLength( currWord.length() + 1 );
    char currentChar = (char)0;

    while( iter.hasNext() ) 
    {
      currentChar = ((Character)(iter.next())).charValue();
      currentWord.setCharAt(currWord.length(), currentChar);
      nextNode = node.getNodeForLetter( currentChar );
      if( nextNode.getIsWord() ) 
      {
        arg[0] = currentWord.toString();
        if( action != null && this.validateActionMethod( action ) ) 
        {
          try 
          {
            action.invoke( this, arg );
          } 
          catch( Exception e ) 
          {
            System.out.println( "Error while trying to execute method: " + action.toString() +
                                ": " + e.toString() );
          }
        } 
        else 
        {
          System.out.println( "Action method null or invalid" );
        }
      }

      this.walkTable( nextNode, currentWord.toString(), action );
    }
  }

  /**
   * Make sure that the method passed as action to the walker is of the right format.
   */
  private boolean validateActionMethod( Method action ) 
  {
    Class [] params = action.getParameterTypes();
    boolean returnVal = true;

    try 
    {
      if( params.length != 1 || !params[0].equals(Class.forName("java.lang.String")) ) 
      {
        returnVal = false;
      }
    } 
    catch( Exception e ) 
    {
      System.out.println( "Invalid class for parameter found" );
      returnVal = false;
    }

    return returnVal;
  }

  /**
   * Walk the tree and print each word
   */
  public void printTable() 
  {
    try 
    {
      this.walkTable( mRootNode, new String(), this.getActionMethodFromName( "printWord" ) );
    } 
    catch(Exception e) 
    {
      System.out.println( e.toString() );
    }
  }

  protected void printWord( String arg ) 
  {
    System.out.println( arg );
  }

  /**
   * utility for retrieving the Method object from the method name from *this* class
   */
  private Method getActionMethodFromName( String funcName ) 
  {
    Method returnMethod = null;

    try 
    {
      Class [] params = { new String().getClass() };
      returnMethod = this.getClass().getDeclaredMethod( funcName, params );
    } 
    catch( Exception e ) 
    {
      System.out.println( "Warning - returning null method: " + e.toString() );
    }

    return returnMethod;
  }


  /**
   * Return an iterator that can be used to incrementally (letter by letter)
   * check if a word exists in the list.
   */
  public LookupTreeIterator getIterator() 
  {
    return new LookupTreeIterator(mRootNode);
  }

  /**
   * Return an iterator that can be used to incrementally (letter by letter)
   * check if a word exists in the list.
   */
  public boolean isRootChar( char c) 
  {
    return mRootNode.containsLetter( c );
  }


  public static void main( String [] args ) 
  {
    try 
    {
      LookupTree LT = new LookupTree();
      LT.loadWordsFromFile("dictionary.txt");
      LT.printTable();

      BufferedReader stdReader = new BufferedReader(
                                 new InputStreamReader( System.in ));

      String tempStr = null;

      while(true) 
      {
        System.out.print("Enter a word to lookup, or \"quit\" to quit: ");
        tempStr = stdReader.readLine();
        if(tempStr.equals("quit") ) 
        {
          break;
        } 
        else 
        {
          System.out.println( LT.wordExists( tempStr ) );
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

