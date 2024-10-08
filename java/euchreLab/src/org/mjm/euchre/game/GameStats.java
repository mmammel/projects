package org.mjm.euchre.game;

import java.util.HashMap;
import java.util.Map;

/**
 * Team 0:
 *   Player X called:
 *      3-2:
 *        2400
 *   Player Y called:
 *      4-1:
 *        4500
 * ...
 * 
 * @author mmammel
 *
 */
public class GameStats {
  private Game game;
  private Map<String,Map<String,Map<String,Integer>>> winMap = null;
  private Map<String,String> sampleTricks = new HashMap<String,String>();
  
  public GameStats(Game g) {
    this.game = g;
    this.winMap = new HashMap<String,Map<String,Map<String,Integer>>>();
  }
  
  public void addWin( String team, String call, String score ) {
    Map<String,Map<String,Integer>> tempCallMap = null;
    Map<String,Integer> tempScoreMap = null;
    Integer wins = null;
    
    if( (tempCallMap = this.winMap.get(team)) == null ) {
      tempCallMap = new HashMap<String,Map<String,Integer>>();
      this.winMap.put( team, tempCallMap);
    }
    
    if( (tempScoreMap = tempCallMap.get(call)) == null ) {
      tempScoreMap = new HashMap<String,Integer>();
      tempCallMap.put( call, tempScoreMap);
    }
    
    if( (wins = tempScoreMap.get(score)) == null ) {
      wins = 0;
    }
    
    String key = team + "-" + call + "-" + score;
    
    if( !this.sampleTricks.containsKey( key ) ) {
      this.sampleTricks.put( key, this.game.tricksToString());
    }
    
    tempScoreMap.put( score, wins + 1);    
  }
  
  public String toString() {
    String key = null;
    StringBuilder sb = new StringBuilder();
    Map<String,Map<String,Integer>> tempCallMap = null;
    Map<String,Integer> tempScoreMap = null;
    
    for( String s0 : this.winMap.keySet() ) {
      sb.append(s0).append(":\n");
      tempCallMap = this.winMap.get(s0);
      for( String s1 : tempCallMap.keySet() ) {
        sb.append("  ").append( s1 ).append(":\n");
        tempScoreMap = tempCallMap.get(s1);
        for( String s2 : tempScoreMap.keySet() ) {
          sb.append("    ").append(s2).append(": ").append( tempScoreMap.get(s2)).append("\n      e.g. ");
          key = s0 + "-" + s1 + "-" + s2;
          sb.append(this.sampleTricks.containsKey(key) ? this.sampleTricks.get(key) : "<no sample>").append("\n");
        }
      }
    }
    
    return sb.toString();
  }
}
