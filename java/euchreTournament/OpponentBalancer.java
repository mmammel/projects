import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class OpponentBalancer {
  
  public static String [][] TOURNAMENT = {
//  
//        { "AB", "EF", "CH", "DG" },
//        { "AF", "BE", "GH", "CD" },
//        { "BC", "FG", "AD", "EH" },
//        { "BG", "CF", "AH", "DE" },
//        { "BD", "FH", "AC", "EG" },
//        { "BF", "DH", "AE", "CG" },
//        { "BH", "DF", "AG", "CE" }
        
        { "AB", "EF", "GH", "CD" },
        { "AF", "BE", "CH", "DG" },
        { "BC", "FG", "AD", "EH" },
        { "BG", "CF", "AH", "DE" },
        { "BD", "FH", "AC", "EG" },
        { "BF", "DH", "AE", "CG" },
        { "BH", "DF", "AG", "CE" }
  };
  
  public static int [][] TRANSFORMS = {
      //{ 0, 2, 1, 3 },
      //{ 0, 3, 2, 1 },
      //{ 0, 2, 3, 1 }
      {0,2,1,3},
      {2,3,1,0},
      {2,1,3,0},
      {2,3,1,0},
      {2,1,0,3}
  };
  
  Map<String,Integer> counts = new HashMap<String,Integer>();
  Map<String,Map<String,Integer>> tableCounts = new HashMap<String,Map<String,Integer>>();
  
  public OpponentBalancer() {
    tableCounts.put("TABLE1",new HashMap<String,Integer>() );
    tableCounts.put("TABLE2",new HashMap<String,Integer>() );
  }
  
  private void getCounts( String [][] array ) {
   
    this.counts.clear();
    this.tableCounts.get("TABLE1").clear();
    this.tableCounts.get("TABLE2").clear();
    String [] row = null;
    String currTable = null;
    Integer tempCount = null;
    Map<String,Integer> tableMap = null;
    for( int i = 0; i < array.length; i++ ) {
      row = array[i];
      
      for( int j = 0; j < row.length; j += 2 ) {
        if( j == 0 ) { 
          tableMap = this.tableCounts.get("TABLE1");
        } else {
          tableMap = this.tableCounts.get("TABLE2");
        }

        this.accumulateCount(this.normalize(row[j].charAt(0), row[j+1].charAt(0)));
        this.accumulateCount(this.normalize(row[j].charAt(0), row[j+1].charAt(1)));
        this.accumulateCount(this.normalize(row[j].charAt(1), row[j+1].charAt(0)));
        this.accumulateCount(this.normalize(row[j].charAt(1), row[j+1].charAt(1)));

        if( (tempCount = tableMap.get( ""+row[j].charAt(0) )) == null ) {
          tableMap.put( ""+row[j].charAt(0), 1 );
        } else {
          tableMap.put( ""+row[j].charAt(0), tempCount + 1 );
        }

        if( (tempCount = tableMap.get( ""+row[j].charAt(1) )) == null ) {
          tableMap.put( ""+row[j].charAt(1), 1 );
        } else {
          tableMap.put( ""+row[j].charAt(1), tempCount + 1 );
        }

        if( (tempCount = tableMap.get( ""+row[j+1].charAt(0) )) == null ) {
          tableMap.put( ""+row[j+1].charAt(0), 1 );
        } else {
          tableMap.put( ""+row[j+1].charAt(0), tempCount + 1 );
        }

        if( (tempCount = tableMap.get( ""+row[j+1].charAt(1) )) == null ) {
          tableMap.put( ""+row[j+1].charAt(1), 1 );
        } else {
          tableMap.put( ""+row[j+1].charAt(1), tempCount + 1 );
        }
      } 
    }
  }
  
  private void accumulateCount( String pair ) {
    Integer temp = null;
    if( (temp = this.counts.get(pair)) == null ) {
      this.counts.put(pair, 1);
    } else {
      this.counts.put(pair, temp+1);
    }
  }
  
  private String normalize( char a, char b ) {
    return a < b ? ""+a+b : ""+b+a;
  }
  
  public void rearrangeAndCount() {
    this.rearrangeAndCountInner(0);
  }
  
  private void rearrangeAndCountInner( int currRow ) {
    if( currRow >= TOURNAMENT.length ) {
      this.getCounts(TOURNAMENT);
      System.out.print( "NUMELEMENTS: " + this.counts.size() + " DIFFMINMAX: " + this.getMinMaxCountDiff() + " AVG: " + this.getAverageCount() + " " );
      System.out.println( this.counts );
      System.out.println( this.tableCounts );
      this.printTournament();
    } else {
      for( int i = 0; i < TRANSFORMS.length; i++ ) {
        this.transformRow(currRow, TRANSFORMS[i]);
        this.rearrangeAndCountInner( currRow + 1);
      }
    }
  }
  
  private void printTournament() {
    for( int i = 0; i < TOURNAMENT.length; i++ ) {
      for( int j = 0; j < TOURNAMENT[i].length; j++ ) {
        System.out.print(TOURNAMENT[i][j] + " ");
      }
      System.out.println("");
    }
  }
  
  private void transformRow( int row, int [] transform ) {
    System.out.println("Pre-transform: " + Arrays.toString(TOURNAMENT[row]));
    String [] arr = new String [4];
    for( int i = 0; i < 4; i++ ) {
      arr[i] = TOURNAMENT[row][transform[i]];
    }
    
    for( int i = 0; i < 4; i++ ) {
      TOURNAMENT[row][i] = arr[i];
    }
    System.out.println("Post-transform: " + Arrays.toString(TOURNAMENT[row]));
  }
  
  
  private double getAverageCount() {
    double n = 0.0;
    double sum = 0.0;
    double tmp = 0.0;
    Integer i = 0;
    for( String key : this.counts.keySet() ) {
      i = this.counts.get(key);
      tmp = i.doubleValue();
      sum += tmp;
      n++;
    }
    
    return sum / n;
  }
  
  private int getMinMaxCountDiff() {
    int min = 100;
    int max = 0;
    int tmp = 0;
    
    for( String key: this.counts.keySet() ) {
      tmp = this.counts.get(key);
      if( tmp < min ) min = tmp;
      if( tmp > max ) max = tmp;
    }
    
    return max - min;    
  }
  
  public static void main( String [] args ) {
    OpponentBalancer OB = new OpponentBalancer();
    OB.rearrangeAndCount();
  }
  
}
