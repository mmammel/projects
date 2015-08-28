package org.mjm.euchre.card;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.FileReader;

public class Deck
{
  private static final Pattern HAND_PATTERN = Pattern.compile("^Player [0-3]: \\[(.*?)\\]\\[(.*?)\\]\\[(.*?)\\]\\[(.*?)\\]\\[(.*?)\\]$");
  private static final Pattern KITTY_PATTERN = Pattern.compile("^Kitty: \\[(.*?)\\]\\[(.*?)\\]\\[(.*?)\\]\\[(.*?)\\]$");
  private Card [] deck = null;
  public Deck() {}
  
  public Deck( String fileName ) {
    List<Card> cards = new ArrayList<Card>();
    BufferedReader input_reader = null;
    String input_str = "";
    int lineCount = 0;
    Matcher m = null;
    try
    {
      String matchStr = null;
      input_reader = new BufferedReader( new FileReader ( fileName ) );
      while( (input_str = input_reader.readLine()) != null && lineCount < 5 )
      {
        if( lineCount++ < 4 ) {
          m = HAND_PATTERN.matcher(input_str);
          if( m.matches() ) {
            for( int i = 1; i <=5; i++ ) {
              matchStr = m.group(i);
              cards.add(Card.lookupCard(matchStr));
            }
          }
        } else {
          m = KITTY_PATTERN.matcher(input_str);
          if( m.matches() ) {
            for( int i = 1; i <=4; i++ ) {
              matchStr = m.group(i);
              cards.add(Card.lookupCard(matchStr));
            }
          }
        }
      }
    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
    
    this.deck = cards.toArray(new Card [0]);
  }
  
  public Card [] deal()
  {
    Card [] retVal = null;
    
    if( this.deck == null ) {
      Card [] init = Card.values();
      retVal = new Card [init.length];
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
    } else {
      retVal = this.deck;
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
