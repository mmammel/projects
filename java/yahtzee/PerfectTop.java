public class PerfectTop {
  public int scores [] = { 0,0,0,0,0,0 };

  public static void main( String [] args ) {
    PerfectTop PT = new PerfectTop();
    PT.run(0);
  }

  public void run( int num ) {
    if( num > 5 ) {
      this.check();
      return;
    }
    for( int i = 0; i < 6; i++ ) {
      this.scores[num] = i*(num+1);
      this.run( num + 1 );
    }
  } 

  public void check() {
    int sum = 0;
    for( int i = 0; i < scores.length; i++ ) {
      sum += scores[i];
    }

    if( sum == 63 ) {
      System.out.println( "63: " + scores[0] + "," + scores[1] + "," + scores[2] + "," + scores[3] + "," + scores[4] + "," + scores[5] );
    }
  }

  public void print() {
    System.out.println( scores[0] + "," + scores[1] + "," + scores[2] + "," + scores[3] + "," + scores[4] + "," + scores[5] );
  }
}
