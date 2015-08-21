package org.mjm.euchre.game;

import org.mjm.euchre.card.Card;
import org.mjm.euchre.card.CardSuit;

public class Play {
  private int player;
  private Card cardPlayed;
  private CardSuit suitPlayed;
  private PlayType playType;
  
  public Play( int player, PlayType type ) {
    this.player = player;
    this.playType = type;
    this.cardPlayed = null;
    this.suitPlayed = null;
  }
  
  public Play( int player, PlayType type, Card c ) {
    this.player = player;
    this.playType = type;
    this.cardPlayed = c;
    this.suitPlayed = c.suit();
  }
  
  public Play( int player, PlayType type, CardSuit s ) {
    this.player = player;
    this.playType = type;
    this.cardPlayed = null;
    this.suitPlayed = s;
  }
  
  public CardSuit getSuitPlayed() {
    return suitPlayed;
  }

  public void setSuitPlayed(CardSuit suitPlayed) {
    this.suitPlayed = suitPlayed;
  }

  public Card getCardPlayed() {
    return cardPlayed;
  }

  public void setCardPlayed(Card cardPlayed) {
    this.cardPlayed = cardPlayed;
  }

  public int getPlayer() {
    return player;
  }
  
  public void setPlayer(int player) {
    this.player = player;
  }
  
  public PlayType getPlayType() {
    return playType;
  }
  
  public void setPlayType(PlayType playType) {
    this.playType = playType;
  }
}
