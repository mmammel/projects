
package org.mjm.tools.lookuptree;

import java.util.*;

/**
 * Represents one node of a tree class that makes
 * incremental word-lookup easy.
 */
public class LookupTreeNode 
{
  //true when the word ending with the letter that is this
  //TreeNode's key is a complete word.
  private boolean isWord = false;
  private HashMap letterMap = new HashMap();

  public LookupTreeNode() {}

  /**
   * Returns an iterator over the set of keys in this Node.
   * The set of keys is the set of letters represented at this
   * level of the tree.
   * @returns Iterator The keySet iterator
   */
  public Iterator getLetterIterator() 
  {
    return letterMap.keySet().iterator();
  }

  /**
   * Returns the Set of keys int his Node.  The keys represent
   * the letters represented at this level of the tree.
   * returns Set The set of letters in this Node.
   */
  public Set getLetterSet() 
  {
    return letterMap.keySet();
  }
  /**
   * Returns the Set of keys int his Node.  The keys represent
   * the letters represented at this level of the tree.
   * returns Set The set of letters in this Node.
   */

  public boolean containsLetter( char c ) 
  {
    return letterMap.containsKey(new Character(c));
  }

  /**
   * Adds a new letter at this level, and returns the corresponding
   * Node that is created.
   * @param c The letter to be added
   * @param isword If true, the isWord flag will be set for true in the returned Node,
   *        indicating that the added letter completed a word.
   * @returns LookupTreeNode The added node.
   */
  public LookupTreeNode addLetter(char c, boolean isword) 
  {
    LookupTreeNode newNode = new LookupTreeNode();

    letterMap.put(new Character(c), newNode);

    return newNode;
  }

  public boolean getIsWord() { return this.isWord; }
  public void setIsWord( boolean isword ) { isWord = isword; }

  /**
   * See if this node contains some letter c.  If it does, return the letter's
   * corresponding node.
   * @param c The letter to look up
   * @returns LookupTreeNode the Node corresponding to the letter c, or null
   */
  public LookupTreeNode getNodeForLetter(char c) 
  {
    return (LookupTreeNode)letterMap.get(new Character(c));
  }
}

