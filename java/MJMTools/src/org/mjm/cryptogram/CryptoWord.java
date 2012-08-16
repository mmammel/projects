package org.mjm.cryptogram;

public class CryptoWord
{
  private String mOriginal;
  private String mDecrypted;
  private int mIndex;
  //private String mHintPattern;
  //private String mPattern;
  private boolean mFull;

  private int mLength;

  public CryptoWord( String word, int index )
  {
    word = word.replaceAll( "[-?!,:;\".()&]","");
    this.mOriginal = this.mDecrypted = word;
    this.mLength = this.mOriginal.length();
    this.mIndex = index;
    //this.mHintPattern = word.replaceAll( ".",".");
  }

  public boolean equals( Object o )
  {
    if( this == o ) return true;
    if( !(o instanceof CryptoWord) ) return false;
    CryptoWord cw = (CryptoWord)o;
    return (cw.mOriginal.equals( this.mOriginal ) && cw.mIndex == this.mIndex);
  }
  
  public int hashCode()
  {
    return this.mOriginal.hashCode();
  }

  /**
   *  Returns the mOriginal property
   *  @return The mOriginal property
   */
  public String getOriginal()
  {
      return mOriginal;
  }

  /**
   *  Sets the mOriginal property
   *  @param mOriginal The property to set
   */
  public void setOriginal(String mOriginal)
  {
      this.mOriginal = mOriginal;
  }

  /**
   *  Returns the mDecrypted property
   *  @return The mDecrypted property
   */
  public String getDecrypted()
  {
      return mDecrypted;
  }

  /**
   *  Sets the mDecrypted property
   *  @param mDecrypted The property to set
   */
  public void setDecrypted(String mDecrypted)
  {
      this.mDecrypted = mDecrypted;
  }

  /**
   *  Returns the mHintPattern property
   *  @return The mHintPattern property
   */
  public String getHintPattern(CipherTable cipher)
  {
      StringBuilder retVal = new StringBuilder();
      for( int i = 0; i < this.mOriginal.length(); i++ )
      {
        if( cipher.isMapped( this.mOriginal.charAt(i)) )
        {
          retVal.append( cipher.get(this.mOriginal.charAt(i)) );
        }
        else
        {
          retVal.append("[").append( cipher.getUnMapped() ).append("]");
        }
      }
      
      return retVal.toString();
  }

  public int getIndex()
  {
    return this.mIndex;
  }

  /**
   *  Returns the mFull property
   *  @return The mFull property
   */
  public boolean getFull()
  {
      return mFull;
  }

  /**
   *  Sets the mFull property
   *  @param mFull The property to set
   */
  public void setFull(boolean mFull)
  {
      this.mFull = mFull;
  }

  /**
   *  Returns the mLength property
   *  @return The mLength property
   */
  public int getLength()
  {
      return mLength;
  }

  /**
   *  Sets the mLength property
   *  @param mLength The property to set
   */
  public void setLength(int mLength)
  {
      this.mLength = mLength;
  }

}