import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;

class Fancy {
   
    // given some seq index, which op should I start with?
    // the index given will be the last one that was applied prior to
    // this element being appended.
    private static final long MOD = 1000000007l;
    private static final int [] INV_MODS = getInverseMods();
    
    private List<Long> seq = new ArrayList<Long>();

    private long addNum = 0;
    private long multNum = 1;
    private long inverseMultNum = 1;
    public static void main( String [] args ) {
      String fname = args[0];
      BufferedReader input_reader = null;
      String commands = null, params = null;

      try
      {
        input_reader = new BufferedReader( new FileReader ( fname ) );

        commands = input_reader.readLine();
        params = input_reader.readLine();
      }
      catch( Exception e )
      {
        System.out.println( "Caught an exception: " + e.toString() );
        e.printStackTrace();
      } finally {
        try {
          input_reader.close();
        } catch( Exception e2 ) {
        }
      }

      commands = commands.replaceAll("[\\[\\]]","");
      commands = commands.replaceAll("\"","");
      String [] commandList = commands.split(",");

      params = params.replaceAll("\\[\\]", "0"); // get rid of opening bracket.
      params = params.replaceAll("[\\[\\]]","");
      System.out.println( commands + " - " + params );
      String [] paramList = params.split(",");

      Fancy F = new Fancy();
      List<Integer> results = new ArrayList<Integer>();
      int p = 0;
      try {
        for( int i = 0; i < commandList.length; i++ ) {
          p = Integer.parseInt( paramList[i] );
          if( commandList[i].equals("Fancy") ) {
            results.add(null);
          } else if( commandList[i].equals("append") ) {
            F.append(p);
            results.add(null);
          } else if( commandList[i].equals("addAll") ) {
            F.addAll(p);
            results.add(null);
          } else if( commandList[i].equals("multAll") ) {
            F.multAll(p);
            results.add(null);
          } else if( commandList[i].equals("getIndex") ) {
            results.add( F.getIndex(p) );
          }
        }
      } catch( Exception e ) {
        e.printStackTrace();
        System.out.println(e.toString());
      }

      System.out.println( results );
    }


    public Fancy() {
       
    }
   
    static int modInverse(int x, int mod)
    {
      long y = 1, m = MOD - 2, p = x;
      //Similar to the fast exponentiation operation, if the modulus is taken for each step of the judgment, it is a fast exponentiation modular operation.
      for (int i = 0; 1L << i < m; i++, p = p * p % mod)
          if ((m >> i & 1) == 1) y = y * p % mod;
      return (int) y;
    }

    static int [] getInverseMods() {
      int [] retVal = new int [101];
      retVal[0] = 0;
      retVal[1] = 1;
      for( int i = 2; i < 101; i++ ) {
        retVal[i] = modInverse( i, (int)MOD );
      }

      return retVal;
    }

    public void append(int val) {
      long v = (long)val;
      v = (v - this.addNum + MOD) * this.inverseMultNum % MOD;
      this.seq.add( v );
    }
   
    public void addAll(int inc) {
      this.addNum = (this.addNum + inc) % MOD;
    }
   
    public void multAll(int m) {
      this.multNum = (this.multNum * m) % MOD;
      this.addNum = (this.addNum * m) % MOD;
      this.inverseMultNum = (this.inverseMultNum * INV_MODS[m]) % MOD;
    }
   
    public int getIndex(int idx) {
      if( idx > this.seq.size() - 1 ) {
        return -1;
      }
      long num = this.seq.get(idx);
      num = (num * this.multNum) % MOD;
      num = (num + this.addNum) % MOD;
      return (int)num;
    }
   
  }

/**
 * Your Fancy object will be instantiated and called as such:
 * Fancy obj = new Fancy();
 * obj.append(val);
 * obj.addAll(inc);
 * obj.multAll(m);
 * int param_4 = obj.getIndex(idx);
 */
