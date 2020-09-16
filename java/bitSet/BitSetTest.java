import java.util.BitSet;
import java.util.Set;
import java.util.HashSet;

public class BitSetTest {
  public static void main( String [] args ) {
    BitSet bs = null;
    if( args.length == 1 && args[0].matches( "^[01]+$") ) {
      bs = makeBitSetFromString( args[0]);
    }

    System.out.println( bs );

    BitSet a = new BitSet(7);
    BitSet b = new BitSet(7);

    a.set(0); b.set(0);
    a.set(2); b.set(2);
    a.set(3); b.set(3);
    a.set(5); b.set(5);

    Set<BitSet> bss = new HashSet<BitSet>();
    bss.add(a);
    bss.add(b);
    System.out.println( bss.size() );
  }

  public static BitSet makeBitSetFromString( String bits ) {
    BitSet retVal = new BitSet( bits.length() );
    for( int i = 0; i < bits.length(); i++ ) {
      if( bits.charAt(i) == '1' ) retVal.set(i);
    }

    return retVal;
  }
}
