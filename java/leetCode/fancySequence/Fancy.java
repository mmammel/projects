import java.util.*;
import java.io.BufferedReader;
import java.io.FileReader;

class Fancy {

    private static final long MODULO = 1000000007l;
    private static final String OP_PATTERN = "^([ma])([0-9]+?)_([0-9]+)$";
    
    
    private List<OP> ops = new ArrayList<OP>();
    private List<Long> seq = new ArrayList<Long>();
    private List<Integer> output = new ArrayList<Integer>();

    public static void main( String [] args ) {
      String fname = args[0];
      BufferedReader input_reader = null;
      String input_str = "";
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
    
    public void append(int val) {
        this.seq.add( Integer.valueOf(val).longValue() );
        output.add(null);
    }
    
    public void addAll(int inc) {
        if( this.seq.size() > 0 ) {
          ops.add(new OP( 'a', inc, this.seq.size() - 1 ));
        } 
        
        output.add(null);
    }
    
    public void multAll(int m) {
        if( this.seq.size() > 0 ) {
          ops.add(new OP( 'm', m, this.seq.size() - 1 ));
        } 
        
        output.add(null);
    }
    
    public int getIndex(int idx) {
        int retVal = 0;
        if( idx >= this.seq.size() ) {
            retVal = -1;
        } else {
            long seqVal = this.seq.get(idx);
System.out.println( "Root val: " + seqVal );
            for( OP op : ops ) {
System.out.println( "Looking at: " + op );
                if( op.idx >= idx ) {
System.out.println( "Applying to: " + seqVal + "...");
                    seqVal = op.apply(seqVal);
System.out.println( "...Got: " + seqVal );
                }
            }
            
            retVal = (int)seqVal;
        }
        output.add(retVal);
        return retVal;
    }
    
    public static class OP {
        public OP( char type, int val, int idx ) {
            this.type = type;
            this.val = val;
            this.idx = idx;
        }

        public String toString() {
          return "["+this.type+","+this.val+","+this.idx+"]";
        }
        
        char type = 'a'; // a or m
        int val = 0;
        int idx = -1; // the index this op was applied at
        int apply( long n ) {
            if( type == 'a' ) {
                return (int)((n + val) % MODULO);
            } else {
                return (int)((n * val) % MODULO);
            }
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
