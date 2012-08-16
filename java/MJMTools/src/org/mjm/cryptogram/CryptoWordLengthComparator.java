package org.mjm.cryptogram;

import java.util.Comparator;

public class CryptoWordLengthComparator implements Comparator<CryptoWord>
{
  public int compare( CryptoWord w1, CryptoWord w2 )
  {
    int retVal = 0;
    if( !w1.equals(w2) )
    {
      Integer i1 = w1.getLength();
      Integer i2 = w2.getLength();
      retVal = i2.compareTo(i1) == 0 ? (w1.getOriginal()+w1.getIndex()).compareTo(w2.getOriginal()+w2.getIndex()) : i2.compareTo(i1);
    }
    else
    {
      retVal = 0;
    }
    
    return retVal;
  }

  public boolean equals( Object obj )
  {
    return (obj instanceof CryptoWordLengthComparator);
  }
}