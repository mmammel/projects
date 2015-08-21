package org.mjm.euchre;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Set;

import org.mjm.euchre.card.Card;
import org.mjm.euchre.card.CardSuit;
import org.mjm.euchre.game.Game;
import org.mjm.euchre.game.Hand;

public class Driver {
  
  private static BufferedReader input_reader = new BufferedReader( new InputStreamReader(System.in) );
  
  private static String [] YES_NO = { "y", "n" };
  private static String [] DISCARD_CHOICE = { "1","2","3","4","5" };

  
  public static void main( String [] args ) {
    Game G = new Game();
    String input = null;
    boolean trumpSet = false;
    G.printHands();
    for( int i = 0; i < 4; i++ ) { 
      System.out.println( "Turn Card: " + G.getKitty().getTurnCard() );
      System.out.print( "Player " + i + " " + G.getPlayerHand(i) + ", "+ (i == 3 ? "pick " : "order ") + G.getKitty().getTurnCard() + " up? [y/n]: ");
      input = readInput( YES_NO );
      if( input == null ) {
        doExit();
      } else if( input.toUpperCase().equals("Y") ) {
        G.setTrump( G.getKitty().getTurnCard() );
        trumpSet = true;
        handleDiscard( G );
        break;
      } else {
        System.out.println( "Passed" );
      }
    }
    
    Set<CardSuit> suitSet = null;
    if( !trumpSet ) {
      // nobody ordered it up
      for( int i = 0; i < 4; i++ ) {
        int count = 1;
        suitSet = G.getPlayerHand(i).getSuitSet();
        suitSet.remove(G.getKitty().getTurnCard().suit());
        CardSuit [] suitArray = new CardSuit[ suitSet.size() ];
        int idx = 0;
        for( CardSuit suit : suitSet ) {
          System.out.println( ""+count++ + ". " + suit );
          suitArray[idx++] = suit;
        }
        
        System.out.print( "Player " + i + " make it" + (i==3 ? "? : " : " or pass (p)? : "));
        input = readInput( getMakeItOptions(suitSet.size(), (i!=3) ) );
        
        if( input == null ) {
          doExit();
        } else if( !input.toUpperCase().equals("P")) {
          G.setTrump(suitArray[Integer.parseInt(input) - 1] );
        }
      }
    }
    
    G.printHands();
  }
  
  private static String readInput( String [] expected ) {
    String retVal = null;
    try {
      while( (retVal = input_reader.readLine()) != null ) {
        if( expected != null ) {
          for( String str : expected ) {
            if( retVal.toUpperCase().equals(str.toUpperCase())) {
              return retVal;
            }
          }
          
          if( retVal.toUpperCase().equals("Q") ) {
            retVal = null;
            break;
          } else {
            System.out.print( "Invalid entry: Must be: " + getExpected(expected) + ", try again: " );
          }
        }
      }      
    } catch( IOException ioe ) {
      retVal = null;
    }
    
    return retVal;
  }
  
  private static String getExpected( String [] exp ) {
    StringBuilder retVal = new StringBuilder();
    for( String str : exp ) {
      if( retVal.length() > 0 ) retVal.append(" or " );
      retVal.append( str );
    }
    
    return retVal.toString();
  }
  
  private static void handleDiscard( Game G ) {
    Hand h = G.getPlayerHand(3); // the dealer
    int cardCount = 1;
    String choiceStr = null;
    Card [] cardArray = new Card[h.getNumCards()];
    System.out.println( "Dealer, you must discard, getting: " + G.getKitty().getTurnCard() );
    int count = 0;
    for( Card c : h.getCards() ) {
      System.out.println(""+cardCount++ + ": " + c );
      cardArray[count++] = c;
    }
    System.out.println( "Choose a discard [1-5]: ");
    choiceStr = readInput( DISCARD_CHOICE );
    if( choiceStr == null ) {
      doExit();
    } else {
      int choice = Integer.parseInt(choiceStr);
      Card swap = cardArray[choice - 1];
      h.addCard(G.getKitty().getTurnCard());
      G.getKitty().addCard(swap);
    }
  }
  
  private static String [] getMakeItOptions( int n, boolean canPass ) {
    String [] retVal = new String [ n + (canPass ? 1 : 0) ];
    for( int i = 0; i < n; i++ ) {
      retVal[i] = ""+(i+1);
    }
    
    if( retVal.length > n ) retVal[ retVal.length - 1 ] = "p";
    
    return retVal;
  }
  
  private static void doExit() {
    System.out.println( "l8tr m8");
    System.exit(0);
  }
}
