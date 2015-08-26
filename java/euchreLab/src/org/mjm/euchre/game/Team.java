package org.mjm.euchre.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Team {
  private int score = 0;
  private int teamId = 0;
  private boolean calledTrump = false;
  private Set<Integer> members = null;
  private List<Trick> wonTricks = null;
  
  public Team( int [] members, int id ) {
    this.members = new HashSet<Integer>();
    this.wonTricks = new ArrayList<Trick>();
    this.teamId = id;
    for( int i : members ) {
      this.members.add(i);
    }
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public int addWonTrick( Trick won ) {
    this.wonTricks.add(won);
    return ++this.score;
  }
  
  public int removeWonTrick() {
    this.wonTricks.remove(this.wonTricks.size() - 1);
    return --this.score;
  }
  
  public boolean isCalledTrump() {
    return calledTrump;
  }

  public void setCalledTrump(boolean calledTrump) {
    this.calledTrump = calledTrump;
  }

  public Set<Integer> getMembers() {
    return members;
  }

  public void setMembers(Set<Integer> members) {
    this.members = members;
  }
  
  public boolean isMember( int player ) {
    return this.members.contains(player);
  }
  
  /**
   * 0 -> 2
   * 1 -> 3
   * 2 -> 0
   * 3 -> 1
   * @param player
   * @return
   */
  public static int getPartner( int player ) {
    return (player + 2) % 4;
  }
  
  public String toString() {
    return "Team " + this.teamId;
  }
}
