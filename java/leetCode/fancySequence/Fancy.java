import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;

class Fancy {

    private static final int INIT = 0;
    private static final int APPEND = 1;
    private static final int ADD = 2;
    private static final int MULT = 3;
   
    // given some seq index, which op should I start with?
    // the index given will be the last one that was applied prior to
    // this element being appended.
    private Map<Integer,Integer> opIndex = new HashMap<Integer,Integer>();
    private int currOpIndex = -1;
    private int prevCommand = INIT;
    
    
    private List<OP> ops = new ArrayList<OP>();
    private List<Long> seq = new ArrayList<Long>();

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
        System.out.println(e.toString());
      }

      System.out.println( results );
    }


    public Fancy() {
       
    }
   
    static long safeMult(long a, long b )
    {
        long res = 0; // Initialize result
        long mod = 1000000007l;
        a = a % mod;
        while (b > 0)
        {
            // If b is odd, add 'a' to result
            if (b % 2 == 1)
            {
                res = (res + a) % mod;
            }
 
            // Multiply 'a' with 2
            a = (a * 2) % mod;
 
            // Divide b by 2
            b /= 2;
        }
 
        // Return result
        return res % mod;
    }

    public void append(int val) {
      this.seq.add( Integer.valueOf(val).longValue() );
//System.out.println( "Appended " + val + " -> " + this.seq );
      opIndex.put( this.seq.size() - 1, currOpIndex );
      this.prevCommand = APPEND;
    }
   
    public void addAll(int inc) {
//System.out.println( "Pushing +" + inc);
      this.addOpInner(new OP( 1, inc, this.seq.size() - 1 ));
      this.prevCommand = ADD;
//System.out.println( "Ops -> " + this.ops );
    }
   
    public void multAll(int m) {
//System.out.println( "Pushing *" + m);
      this.addOpInner(new OP( m, 0, this.seq.size() - 1 ));
      this.prevCommand = MULT;
//System.out.println( "Ops -> " + this.ops );
    }
   
    private void addOpInner(OP op) {
      if( this.seq.size() > 0 ) {
        if( this.prevCommand == MULT || this.prevCommand == ADD ) {
          ops.get(ops.size() - 1).merge(op);
        } else {
          ops.add(op);
          currOpIndex++;
        }
        
        if( this.ops.size() >= 2 ) {
          for( int i = 0; i < this.ops.size() - 1; i++ ) {
            this.ops.get(i).merge(op);
          }
        }
      }
    }

    public int getIndex(int idx) {
//System.out.println( "Getting index " + idx );
      int retVal = 0;
      if( idx >= this.seq.size() || idx < 0 ) {
        retVal = -1;
      } else {
        long seqVal = this.seq.get(idx);
        OP op = null;
        int opIdx = opIndex.get(idx);
        if( (opIdx+1) < ops.size() ) {
          // for( int i = opIdx + 1; i < ops.size(); i++ ) {
          //    op = ops.get(i);
          //    bigVal = op.apply(bigVal);
          // }
          op = ops.get(opIdx+1);
          seqVal = op.apply(seqVal);
        }
           
        retVal = (int)seqVal;
        // update the entry
        // this.seq.set(idx, Integer.valueOf(retVal).longValue());
        // // update the index
        // opIndex.put( idx, ops.size() - 1 );
      }
      return retVal;
    }
   
    public static class OP {
      public OP( int mult, int add, int idx ) {
        this.mult = (long)mult;
        this.add = (long)add;
        this.idx = idx;
      }
       
      long add = 0l, mult = 1l;
      int idx = -1; // the index this op was applied at
       
      void merge( OP op ) {
//System.out.print("Merging " + this + " and " + op + " -> ");
        this.mult = safeMult(this.mult,op.mult);
        this.add = this.add + op.add;
        this.add = safeMult(this.add,op.mult);
//System.out.println( this );
      }
       
      long apply( long n ) {
//System.out.print( "Applying  " + this + " to " + n + "-> ");
        n = safeMult(this.mult,n);
        n = this.add + n;
//System.out.println( n );
        return n;
      }

      public String toString() {
        return "[*" + this.mult + ",+" + this.add + "," + this.idx + "]";
      }
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
