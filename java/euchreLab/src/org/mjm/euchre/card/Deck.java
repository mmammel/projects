package org.mjm.euchre.card;

import java.util.List;
import java.util.ArrayList;

public class Deck
{
  public Card [] deal()
  {
    Card [] init = Card.values();
    Card [] retVal = new Card [init.length];
    List<Card> initList = new ArrayList<Card>();
    for( int i = 0; i < init.length; i++ )
    {
      initList.add( init[i] );
    }

    int count = init.length;
    int randIdx = 0;
    while( count > 0 )
    {
      randIdx = (int)(Math.random() * count);
      retVal[init.length - count--] = initList.get(randIdx);
      initList.remove(randIdx);
    }

    return retVal;
  }

  public static void main( String [] args )
  {
    Deck D = new Deck();

    Card [] deal = D.deal();

    for( int i = 0; i < deal.length; i++ )
    {
      System.out.println( deal[i] );
    }
  }
}
