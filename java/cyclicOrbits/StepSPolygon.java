import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class StepSPolygon {
  // items are 0..v-1; returns the orbit of the step-s polygon {0,s,2s,...,(k-1)s} mod v
  static List<int[]> cyclicOrbit(int v, int k, int s) {
    // build base
    boolean[] seen = new boolean[v];
    int[] base = new int[k];
    int x = 0;
    for (int i = 0; i < k; i++) {
        if (seen[x]) throw new IllegalArgumentException("k must be <= v/gcd(s,v)");
        seen[x] = true;
        base[i] = x;
        x = (x + s) % v;
    }
    // generate orbit
    List<int[]> out = new ArrayList<>();
    boolean[][] used = new boolean[v][v]; // simple dedup guard
    for (int shift = 0; shift < v; shift++) {
        int[] t = new int[k];
        for (int i = 0; i < k; i++) t[i] = (base[i] + shift) % v;
        Arrays.sort(t);
        // skip duplicates from symmetry
        String key = Arrays.toString(t);
        if (out.stream().anyMatch(a -> Arrays.equals(a, t))) continue;
        out.add(t);
    }
    return out;
  }

  public static void main( String [] args ) {
    int v = 0, k = 0, s = 0;

    try {
      v = Integer.parseInt(args[0]);
      k = Integer.parseInt(args[1]);
      s = Integer.parseInt(args[2]);
    } catch( Exception e ) {
      System.err.println( "Bad input");
      System.exit(1);
    }

    List<int[]> result = cyclicOrbit( v, k, s);
    for( int [] orbit : result ) {
      System.out.println( Arrays.toString( orbit ) );
    }
  }

}
