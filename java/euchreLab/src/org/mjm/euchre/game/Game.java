package org.mjm.euchre.game;

import org.mjm.euchre.card.*;

import java.util.List;
import java.util.Arrays;

public class Game {
  // There are 13 potential rounds in a game:
  // pickup/pass*4, make/pass*4, 5 cards played.
  // Note that some rounds will be null, loners, no make rounds, etc.
  // The last 5 rounds are designated for the actual play
  /*
   * 
   * TODO:
   * Create a "Play" class, that has a type, and some context as to what was done
   * based on the type.  Examples:
   * 
   * Round1Pass
   * Round1Order
   * Discard
   * Round2Call
   * Round2Pass
   * LeadTrickCard
   * PlayTrickCard
   * 
   * A PlayFactory will generate a list of possible plays based on the previous play.
   * 
   * The PlayFactory will apply rules, e.g. always follow suit, never under-trump a played trump
   *    unless that is the only possible play, etc.
   * 
   * Game play will be recursive, each possible play will be made, and the next play will happen, etc.
   * Then the next possible play is made, etc.
   * 
   * Results stored in a "statistics" objcet on game, breaking down number of points won for each each
   * team.
   *
   */
  private Hand [] hands = null;
  private Trick [] tricks = null;
  private Kitty kitty = null;
  
  /*
   * score[0] - team 0,2
   * score[1] - team 1,3
   */
  private int [] score = null;
  private int currTrick = -1;
  private Card turnCard = null;

  private CardSuit trump = null;

  public Game() {
    this.score = new int [2];
    Deck deck = new Deck();   
    Card [] deal = deck.deal();
    this.hands = new Hand[4];
    this.tricks = new Trick[5];

    List<Card> cardList = Arrays.asList( deal );

    for( int i = 0; i < 4; i++ ) {
      this.hands[i] = new Hand(cardList.subList(i*5,i*5+5).toArray(new Card[0]));
    }

    this.kitty = new Kitty(cardList.subList(20,24).toArray(new Card[0]));
    this.turnCard = this.kitty.getTurnCard();
    
  } 

  public void setTrump( CardSuit suit ) {
    this.trump = suit;
  }
  
  public void setTrump( Card card ) {
    this.trump = card.suit();
  }
  
  public Trick getCurrentTrick() {
    return this.tricks[this.currTrick];
  }
  
  public CardSuit getTrump() {
    return this.trump;
  }
  
  public Card getTurnCard() {
    return turnCard;
  }
  
  public void setTurnCard(Card turnCard) {
    this.turnCard = turnCard;
  }

  public Hand getPlayerHand( int i )
  {
    Hand retVal = null;
    
    if( i >= 0 && i <= 3 ) { 
      retVal = this.hands[i];
    } else {
      throw new RuntimeException( "No player " + i + " exists!" );
    }
    
    return retVal;
  }

  public Kitty getKitty() { return this.kitty; }
  
  public void executePlay( Play play ) {
    switch( play.getPlayType() ) {
      case Round1Pass:
        break;
      case Round1Order:
        this.setTrump( this.kitty.getTurnCard() );
        break;
      case Discard:
        this.kitty.discard(play.getCardPlayed());
        this.getPlayerHand(play.getPlayer()).addCard(this.turnCard);
        break;
      case Round2Call:
        this.setTrump(play.getSuitPlayed());
        break;
      case Round2Pass:
        break;
      case LeadTrickCard:
        this.currTrick++;
        this.getCurrentTrick().addCard(play.getCardPlayed());
        this.getPlayerHand(play.getPlayer()).removeCard(play.getCardPlayed());
        break;
      case PlayTrickCard:
        this.getCurrentTrick().addCard(play.getCardPlayed());
        this.getPlayerHand(play.getPlayer()).removeCard(play.getCardPlayed());
        break;
    }
  }
  
  public void unExecutePlay( Play play ) {
    switch( play.getPlayType() ) {
      case Round1Pass:
        break;
      case Round1Order:
        this.setTrump( (CardSuit)null );
        break;
      case Discard:
        this.getPlayerHand(play.getPlayer()).addCard(this.kitty.discard(this.turnCard));
        break;
      case Round2Call:
        this.setTrump((CardSuit)null);
        break;
      case Round2Pass:
        break;
      case LeadTrickCard:
        this.getCurrentTrick().removeCard(play.getCardPlayed());
        this.getPlayerHand(play.getPlayer()).addCard(play.getCardPlayed());
        this.currTrick--;
        break;
      case PlayTrickCard:
        this.getCurrentTrick().removeCard(play.getCardPlayed());
        this.getPlayerHand(play.getPlayer()).addCard(play.getCardPlayed());
        break;
    }
  }
  
  public int[] getScore() {
    return score;
  }

  public void setScore(int[] score) {
    this.score = score;
  }

  public static void main( String [] args )
  {
    Game G = new Game();

    // Ask if each player wants to pick it up. Dealer is player 3.
    // If they do, prompt dealer (player 4) for discard, set final deck status.
    for( int i = 0; i < 4; i++ ) { 
      System.out.println( "Turn Card: " + G.getKitty().getTurnCard() );
      System.out.println( "Player " + i + ", order it up?");
    }
  }
  
  public void printHands() {
    StringBuilder retVal = new StringBuilder();
    retVal.append("Player 0: " ).append( this.getPlayerHand(0) ).append("\n");
    retVal.append("Player 1: " ).append( this.getPlayerHand(1) ).append("\n");
    retVal.append("Player 2: " ).append( this.getPlayerHand(2) ).append("\n");
    retVal.append("Player 3: " ).append( this.getPlayerHand(3) ).append("\n");
    retVal.append("Kitty: " ).append( this.getKitty() );
    
    System.out.println( retVal.toString());
  }
}
