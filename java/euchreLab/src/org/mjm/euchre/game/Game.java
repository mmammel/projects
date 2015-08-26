package org.mjm.euchre.game;

import org.mjm.euchre.card.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class Game {
  // There are 13 potential rounds in a game:
  // pickup/pass*4, make/pass*4, 5 cards played.
  // Note that some rounds will be null, loners, no make rounds, etc.
  // The last 5 rounds are designated for the actual play
  private static final int [] TEAM_ONE = {0,2};
  private static final int [] TEAM_TWO = {1,3};
  
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
   * Results stored in a "statistics" object on game, breaking down number of points won for each each
   * team.
   *
   */
  private Hand [] hands = null;
  private Trick [] tricks = null;
  private Kitty kitty = null;
  
  private int currTrick = 0;
  private Card turnCard = null;
  private Team [] teams = null;

  private CardSuit trump = null;
  private int playerWhoOrdered = -1;
  private int playerWhoCalled = -1;
  private GameStats gameStats = null;

  public Game() {
    this.teams = new Team[2];
    this.teams[0] = new Team(TEAM_ONE,0);
    this.teams[1] = new Team(TEAM_TWO,1);
    Deck deck = new Deck();   
    Card [] deal = deck.deal();
    this.hands = new Hand[4];
    this.tricks = new Trick[5];
    this.gameStats = new GameStats();

    List<Card> cardList = Arrays.asList( deal );
    
    for( int j = 0; j < 5; j++ ) {
      this.tricks[j] = new Trick();
    }
    
    for( int i = 0; i < 4; i++ ) {
      this.hands[i] = new Hand(cardList.subList(i*5,i*5+5).toArray(new Card[0]));
    }

    this.kitty = new Kitty(cardList.subList(20,24).toArray(new Card[0]));
    this.turnCard = this.kitty.getTurnCard();
    
  } 

  public void setTrump( CardSuit suit ) {
    this.trump = suit;
    for( Hand h : this.hands ) {
      h.setTrump(suit);
    }
    
    for( Trick t : this.tricks ) {
      t.setTrump(suit);
    }
  }
  
  public void setTrump( Card card ) {
    this.setTrump(card.suit());
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
  
  public List<Play> executePlay( Play play ) {
    //System.out.println("Executing: " + play);
    List<Play> retVal = null;
    int trickWinner = -1;
    switch( play.getPlayType() ) {
      case Round1Pass:
        break;
      case Round1Order:
        this.setTrump( play.getSuitPlayed() );
        this.setPlayerWhoOrdered(play.getPlayer());
        this.setPlayerWhoCalled(-1);
        break;
      case Discard:
        this.kitty.discard(play.getCardPlayed());
        this.getPlayerHand(play.getPlayer()).removeCard(play.getCardPlayed());
        this.getPlayerHand(play.getPlayer()).addCard(this.turnCard);
        break;
      case Round2Call:
        this.setTrump(play.getSuitPlayed());
        this.setPlayerWhoCalled(play.getPlayer());
        this.setPlayerWhoOrdered(-1);
        break;
      case Round2Pass:
        break;
      case PlayTrickCard:
        this.getCurrentTrick().playCard(play.getPlayer(),play.getCardPlayed());
        this.getPlayerHand(play.getPlayer()).removeCard(play.getCardPlayed());
        if( (trickWinner = this.getCurrentTrick().getWinner(this.getTrump())) != -1 ) {
          retVal = new ArrayList<Play>();
          retVal.add(new Play(trickWinner,PlayType.TrickWon));
        }
        break;
      case TrickWon:
        Team w = this.teams[0].isMember(play.getPlayer()) ? this.teams[0] : this.teams[1];
        w.addWonTrick(this.getCurrentTrick());
        if( this.currTrick == 4 ) {
          // game over.
          retVal = new ArrayList<Play>();
          retVal.add( new Play(trickWinner,PlayType.GameWon));
        }
        this.currTrick++;
        break;
      case GameWon:
        this.addWin();
        break;
    }
    
    if( retVal == null ) {
      retVal = PlayFactory.getInstance().getPlays(this, play);
    }
    
    return retVal;
  }
  
  public void unExecutePlay( Play play ) {
    //System.out.println("Unexecuting: " + play);    
    switch( play.getPlayType() ) {
      case Round1Pass:
        break;
      case Round1Order:
        this.setTrump( (CardSuit)null );
        this.setPlayerWhoOrdered(-1);
        break;
      case Discard:
        this.getPlayerHand(play.getPlayer()).removeCard(this.turnCard);
        this.getPlayerHand(play.getPlayer()).addCard(play.getCardPlayed());
        this.getKitty().removeCard(play.getCardPlayed());
        this.getKitty().addCard(this.turnCard);
        break;
      case Round2Call:
        this.setTrump((CardSuit)null);
        this.setPlayerWhoCalled(-1);
        break;
      case Round2Pass:
        break;
      case PlayTrickCard:
        this.getCurrentTrick().unplayCard(play.getPlayer());
        this.getPlayerHand(play.getPlayer()).addCard(play.getCardPlayed());
        break;
      case TrickWon:
        this.currTrick--;
        Team t = this.teams[0].isMember(play.getPlayer()) ? this.teams[0] : this.teams[1];
        t.removeWonTrick();
        break;
      case GameWon:
        break;
    }
  }
  
  /**
   * Find out if a player's team called it.
   * @param player
   * @return
   */
  public boolean didPlayerCallTrump( int player ) {
    return this.getTeamForPlayer(player).isCalledTrump();
  }
  
  /**
   * Get the team that a player is on
   * @param player
   * @return
   */
  public Team getTeamForPlayer( int player ) {
    return this.teams[0].isMember(player) ? this.teams[0] : this.teams[1];
  }

  public void play() {
    Play start = new Play(-1, PlayType.Round1Pass);
    List<Play> plays = PlayFactory.getInstance().getPlays(this, start);
    this.playInner(plays);
  }
  
  private void playInner( List<Play> plays ) {
    List<Play> nextPlays = null;
    for( Play play : plays ) {
      nextPlays = this.executePlay(play);
      this.playInner(nextPlays);
      this.unExecutePlay(play);
    }
  }
  
  public Team getWinningTeam() {
    return this.teams[0].getScore() > 2 ? this.teams[0] : this.teams[1]; 
  }
  
  public void addWin() {
    Team t = this.getWinningTeam();
    String callString = null;
    String scoreString = null;
    
    if( this.playerWhoCalled > -1 ) {
      callString = "Player " + this.playerWhoCalled + " called it " + this.getTrump();
    } else {
      callString = "Player " + this.playerWhoOrdered + " ordered up " + this.getTrump();
    }

    scoreString = t.getScore() + "-" + (5-t.getScore());
    
    this.gameStats.addWin(t.toString(), callString, scoreString);
  }
  
  public int getPlayerWhoOrdered() {
    return playerWhoOrdered;
  }

  public void setPlayerWhoOrdered(int playerWhoOrdered) {
    this.playerWhoOrdered = playerWhoOrdered;
  }

  public int getPlayerWhoCalled() {
    return playerWhoCalled;
  }

  public void setPlayerWhoCalled(int playerWhoCalled) {
    this.playerWhoCalled = playerWhoCalled;
  }

  public GameStats getGameStats() {
    return gameStats;
  }

  public void setGameStats(GameStats gameStats) {
    this.gameStats = gameStats;
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
