import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public enum LetterSet {
  A('A',1),
  AA('A',2),
  AAA('A',3),
  AAAA('A',4),
  AAAAA('A',5),
  AAAAAA('A',6),
  AAAAAAA('A',7),
  B('B',1),
  BB('B',2),
  BBB('B',3),
  BBBB('B',4),
  BBBBB('B',5),
  BBBBBB('B',6),
  BBBBBBB('B',7),
  C('C',1),
  CC('C',2),
  CCC('C',3),
  CCCC('C',4),
  CCCCC('C',5),
  CCCCCC('C',6),
  CCCCCCC('C',7),
  D('D',1),
  DD('D',2),
  DDD('D',3),
  DDDD('D',4),
  DDDDD('D',5),
  DDDDDD('D',6),
  DDDDDDD('D',7),
  E('E',1),
  EE('E',2),
  EEE('E',3),
  EEEE('E',4),
  EEEEE('E',5),
  EEEEEE('E',6),
  EEEEEEE('E',7),
  F('F',1),
  FF('F',2),
  FFF('F',3),
  FFFF('F',4),
  FFFFF('F',5),
  FFFFFF('F',6),
  FFFFFFF('F',7),
  G('G',1),
  GG('G',2),
  GGG('G',3),
  GGGG('G',4),
  GGGGG('G',5),
  GGGGGG('G',6),
  GGGGGGG('G',7),
  H('H',1),
  HH('H',2),
  HHH('H',3),
  HHHH('H',4),
  HHHHH('H',5),
  HHHHHH('H',6),
  HHHHHHH('H',7),
  I('I',1),
  II('I',2),
  III('I',3),
  IIII('I',4),
  IIIII('I',5),
  IIIIII('I',6),
  IIIIIII('I',7),
  J('J',1),
  JJ('J',2),
  JJJ('J',3),
  JJJJ('J',4),
  JJJJJ('J',5),
  JJJJJJ('J',6),
  JJJJJJJ('J',7),
  K('K',1),
  KK('K',2),
  KKK('K',3),
  KKKK('K',4),
  KKKKK('K',5),
  KKKKKK('K',6),
  KKKKKKK('K',7),
  L('L',1),
  LL('L',2),
  LLL('L',3),
  LLLL('L',4),
  LLLLL('L',5),
  LLLLLL('L',6),
  LLLLLLL('L',7),
  M('M',1),
  MM('M',2),
  MMM('M',3),
  MMMM('M',4),
  MMMMM('M',5),
  MMMMMM('M',6),
  MMMMMMM('M',7),
  N('N',1),
  NN('N',2),
  NNN('N',3),
  NNNN('N',4),
  NNNNN('N',5),
  NNNNNN('N',6),
  NNNNNNN('N',7),
  O('O',1),
  OO('O',2),
  OOO('O',3),
  OOOO('O',4),
  OOOOO('O',5),
  OOOOOO('O',6),
  OOOOOOO('O',7),
  P('P',1),
  PP('P',2),
  PPP('P',3),
  PPPP('P',4),
  PPPPP('P',5),
  PPPPPP('P',6),
  PPPPPPP('P',7),
  Q('Q',1),
  QQ('Q',2),
  QQQ('Q',3),
  QQQQ('Q',4),
  QQQQQ('Q',5),
  QQQQQQ('Q',6),
  QQQQQQQ('Q',7),
  R('R',1),
  RR('R',2),
  RRR('R',3),
  RRRR('R',4),
  RRRRR('R',5),
  RRRRRR('R',6),
  RRRRRRR('R',7),
  S('S',1),
  SS('S',2),
  SSS('S',3),
  SSSS('S',4),
  SSSSS('S',5),
  SSSSSS('S',6),
  SSSSSSS('S',7),
  T('T',1),
  TT('T',2),
  TTT('T',3),
  TTTT('T',4),
  TTTTT('T',5),
  TTTTTT('T',6),
  TTTTTTT('T',7),
  U('U',1),
  UU('U',2),
  UUU('U',3),
  UUUU('U',4),
  UUUUU('U',5),
  UUUUUU('U',6),
  UUUUUUU('U',7),
  V('V',1),
  VV('V',2),
  VVV('V',3),
  VVVV('V',4),
  VVVVV('V',5),
  VVVVVV('V',6),
  VVVVVVV('V',7),
  W('W',1),
  WW('W',2),
  WWW('W',3),
  WWWW('W',4),
  WWWWW('W',5),
  WWWWWW('W',6),
  WWWWWWW('W',7),
  X('X',1),
  XX('X',2),
  XXX('X',3),
  XXXX('X',4),
  XXXXX('X',5),
  XXXXXX('X',6),
  XXXXXXX('X',7),
  Y('Y',1),
  YY('Y',2),
  YYY('Y',3),
  YYYY('Y',4),
  YYYYY('Y',5),
  YYYYYY('Y',6),
  YYYYYYY('Y',7),
  Z('Z',1),
  ZZ('Z',2),
  ZZZ('Z',3),
  ZZZZ('Z',4),
  ZZZZZ('Z',5),
  ZZZZZZ('Z',6),
  ZZZZZZZ('Z',7);

  private final char rootChar;
  private final int count;

  private Map<LetterSet, Integer> rels = new HashMap<LetterSet, Integer>();

  LetterSet( char c, int count ) {
    this.rootChar = c;
    this.count = count;
  }
  
  public List<LetterSet> explode() {
    List<LetterSet> retVal = new ArrayList<LetterSet>();
    // If I am AAA, return [A, A, A]
    // If I am A, return [A]
    if( this.count == 1 ) {
      retVal.add( this );
    } else {
      LetterSet single = index.get(""+this.rootChar+"1");
      for( int i = 0; i < this.count; i++ ) {
        retVal.add(single);
      }
    }
    
    return retVal;
  }

  public String getKey() {
    return ""+this.rootChar+this.count;
  }

  private static Map<String, LetterSet> index = new HashMap<String,LetterSet>();
  
  static {
    for( LetterSet l : LetterSet.values() ) {
      index.put( l.getKey(), l );
    }
  }

  public static LetterSet lookup( char c, int count ) {
    return index.get( ""+c+count );
  }

  public static Set<LetterSet> setForWord( String w ) {
    Set<LetterSet> retVal = EnumSet.noneOf( LetterSet.class ); 
    w = w.toUpperCase();
    LetterSet temp = null;
    char [] lets = w.toCharArray();
    Arrays.sort( lets );
    char prev = 0;
    int count = 0;
    for( char c : lets ) {
      if( c != prev && prev != 0 ) {
        count = 0;
      }

      prev = c;
      count += 1;
      temp = lookup( prev, count );
      retVal.add( temp );
    }

    // get the last one.
    temp = lookup( prev, count );
    retVal.add( temp );

    return retVal;
  }
}
