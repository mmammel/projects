import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public enum Letter {
  A(2l),
  B(3l),
  C(5l),
  D(7l),
  E(11l),
  F(13l),
  G(17l),
  H(19l),
  I(23l),
  J(29l),
  K(31l),
  L(37l),
  M(41l),
  N(43l),
  O(47l),
  P(53l),
  Q(59l),
  R(61l),
  S(67l),
  T(71l),
  U(73l),
  V(79l),
  W(83l),
  X(89l),
  Y(97l),
  Z(101l);

  private final BigInteger primeVal;
  private static Map<Character,Letter> letterMap = new HashMap<Character,Letter>();


  Letter( long prime ) {
    this.primeVal = BigInteger.valueOf(prime);
  }

  public BigInteger getPrimeVal() {
    return this.primeVal;
  }

  static {
    for( Letter l : Letter.values() )
    {
      letterMap.put( (char)(65 + l.ordinal()), l ); 
    }
  }

  public static List<Letter> listForWord( String w ) {
    List<Letter> retVal = new ArrayList<Letter>();
    w = w.toUpperCase();
    for( char c : w.toCharArray() ) {
      retVal.add( letterMap.get( c ) );
    }
    return retVal;
  }

  public static BigInteger getWordKey( String w ) { 
    BigInteger retVal = BigInteger.ONE;
    w = w.toUpperCase();
    for( char c : w.toCharArray() ) {
      retVal = retVal.multiply( letterMap.get(c).getPrimeVal() );
    }

    return retVal;
  }

}
