package org.mjm.euchre.game;

import org.mjm.euchre.card.*;

import java.util.List;
import java.util.Arrays;
import java.util.Stack;

public class Game {
  // There are 13 potential rounds in a game:
  // pickup/pass*4, make/pass*4, 5 cards played.
  // Note that some rounds will be null, loners, no make rounds, etc.
  // The last 5 rounds are designated for the actual play
  private Stack<Play> gameStack = new Stack<Play>();
  private Hand p1Hand = null;
  private Hand p2Hand = null;
  private Hand p3Hand = null;
  private Hand p4Hand = null;
  private Kitty kitty = null;

  private CardSuit trump = null;

  public Game() {
    Deck deck = new Deck();   
    Card [] deal = deck.deal();

    List<Card> cardList = Arrays.asList( deal );

    p1Hand = new Hand(cardList.subList(0,5).toArray(new Card [0]));
    p2Hand = new Hand(cardList.subList(5,10).toArray(new Card[0]));
    p3Hand = new Hand(cardList.subList(10,15).toArray(new Card[0]));
    p4Hand = new Hand(cardList.subList(15,20).toArray(new Card[0]));
    kitty = new Kitty(cardList.subList(20,24).toArray(new Card[0]));
  } 

  public Hand getPlayerHand( int i )
  {
    Hand retVal = null;

    switch( i ) {
      case 1:
       retVal = this.p1Hand;
       break;
      case 2:
       retVal = this.p2Hand;
       break;
      case 3:
       retVal = this.p3Hand;
       break;
      case 4:
       retVal = this.p4Hand;
       break;
      default:
        throw new RuntimeException( "No player " + i + " exists!" );
    }

    return retVal;
  }

  public Kitty getKitty() { return this.kitty; }

  public static void main( String [] args )
  {
    Game G = new Game();
    for( int i = 1; i < 5; i++ )
    {
      System.out.println( "Player " + i + ":" + G.getPlayerHand(i) );
    }

    System.out.println( "Kitty: " + G.getKitty() );
    
  }
}
