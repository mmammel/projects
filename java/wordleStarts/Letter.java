import java.util.HashMap;
import java.util.Map;

public enum Letter {
  A('a'),
  B('b'),
  C('c'),
  D('d'),
  E('e'),
  F('f'),
  G('g'),
  H('h'),
  I('i'),
  J('j'),
  K('k'),
  L('l'),
  M('m'),
  N('n'),
  O('o'),
  P('p'),
  Q('q'),
  R('r'),
  S('s'),
  T('t'),
  U('u'),
  V('v'),
  W('w'),
  X('x'),
  Y('y'),
  Z('z');
 
  private final char let;
  
  private static final Map<Character,Letter> charIndex = new HashMap<Character,Letter>();

  static {
    for( Letter l : Letter.values() ) {
      Letter.charIndex.put(l.getLet(), l);
    }
  }
  
  private Letter( char let ) {
    this.let = let;
  }

  public static Letter getLetterFromChar( char c ) {
    return Letter.charIndex.get(c);
  }
  
  public char getLet() {
    return this.let;
  }
}
