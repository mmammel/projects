import java.util.BitSet;

public class BitSetTest {
  public static void main( String [] args ) {
    BitSet b = null;
    if( args.length == 1 && args[0].matches( "^[01]+$") ) {
      b = makeBitSetFromString( args[0]);
    }

    System.out.println( b );
  }

  public static BitSet makeBitSetFromString( String bits ) {
    BitSet retVal = new BitSet( bits.length() );
    for( int i = 0; i < bits.length(); i++ ) {
      if( bits.charAt(i) == '1' ) retVal.set(i);
    }

    return retVal;
  }
}
