 
package org.mjm.tools.lookuptree;

import java.util.Set;

/**
 * The iterator class.  Maintains state and can be reset.
 */
public class LookupTreeIterator 
{
  private final int BUFFER_SIZE     = 1024;
  private LookupTreeNode mInitialTreeNode = null;
  private LookupTreeNode mCurrentTreeNode = null;
  private Set mCurrentNodeKeys      = null;
  private char [] mCurrentWord      = null;
  private int mCurrentWordCount     = 0;
  private boolean mValidState       = true;
  private boolean mIsWord           = false;

  /**
   * Start at node rootNode, initialize word buffer, set initial node for reset later.
   */
  public LookupTreeIterator( LookupTreeNode rootNode ) 
  {
    mCurrentTreeNode  = mInitialTreeNode = rootNode;
    mCurrentNodeKeys  = mCurrentTreeNode.getLetterSet();
    mCurrentWord      = new char [ BUFFER_SIZE ];
    mCurrentWordCount = 0;
  }

  /**
   * See if the character c exists at the iterator's current location in the tree.
   */
  public boolean tryCharacter( char c ) 
  {
    boolean returnVal = false;
    mCurrentWord[ mCurrentWordCount++ ] = c;
    if( mCurrentNodeKeys.contains( new Character(c) ) ) 
    {
      returnVal = true;
      gotoNextNode(c);
    } 
    else 
    {
      mValidState = false;
      mIsWord = false;
    }
    return returnVal;
  }

  /**
   * Iterate to the next letter, only called if that letter exists.
   */
  private void gotoNextNode( char c ) 
  {
    mCurrentTreeNode = mCurrentTreeNode.getNodeForLetter(c);
    mCurrentNodeKeys = mCurrentTreeNode.getLetterSet();
    mIsWord = mCurrentTreeNode.getIsWord();
  }

  /**
   * Get the current buffer containing all looked up letters, including any that didn't exist.
   */
  public String getCurrentBuffer() 
  {
    //MJM return mCurrentWord.toString();
    return new String( mCurrentWord, 0, mCurrentWordCount );
  }

  /**
   * Is the word currently held in the buffer a complete word in the tree?
   */
  public boolean isWord() 
  {
    return mIsWord;
  }

  /**
   * Is the iterator currently in a valid state?  i.e. did the last letter checked exist,
   * meaning that what is currently in the buffer can be traced in through the tree?
   */
  public boolean isValid() 
  {
    return mValidState;
  }

  /**
   * Reset the iterator
   */
  public void reset() 
  {
    mCurrentTreeNode = mInitialTreeNode;
    mCurrentNodeKeys = mCurrentTreeNode.getLetterSet();
    mCurrentWordCount = 0;
    mValidState = true;
  }

}

