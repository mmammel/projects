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
      { 0, 2, 1, 3 },
      { 0, 3, 2, 1 },
      { 0, 2, 3, 1 }
  };
  
  Map<String,Integer> counts = new HashMap<String,Integer>();
  
  public OpponentBalancer() {
  }
  
  private void getCounts( String [][] array ) {
   
    this.counts.clear();
    String [] row = null;
    for( int i = 0; i < array.length; i++ ) {
      row = array[i];
      
      for( int j = 0; j < row.length; j += 2 ) {
        this.accumulateCount(this.normalize(row[j].charAt(0), row[j+1].charAt(0)));
        this.accumulateCount(this.normalize(row[j].charAt(0), row[j+1].charAt(1)));
        this.accumulateCount(this.normalize(row[j].charAt(1), row[j+1].charAt(0)));
        this.accumulateCount(this.normalize(row[j].charAt(1), row[j+1].charAt(1)));
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
    String [] arr = new String [4];
    for( int i = 0; i < 4; i++ ) {
      arr[i] = TOURNAMENT[row][transform[i]];
    }
    
    for( int i = 0; i < 4; i++ ) {
      TOURNAMENT[row][i] = arr[i];
    }
    
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
