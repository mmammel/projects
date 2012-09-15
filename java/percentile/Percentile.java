public class Percentile
{
  public static void main( String [] args )
  { 
    int pScore = Integer.parseInt( args[0] );
    int percentile = 0;
    int [] percents = {0,40,50,55,55,60,60,60,65,65,65,65,70,70,70,70,70,70,75,75,75,75,75,75,75,75,75,75,80,80,80,80,80,80,80,80,80,80,80,80,80,80,85,85,85,85,85,85,85,85,85,85,85,85,85,85,85,85,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,90,95,95,95,95,95,95,95,95,95,95,95,95,95,95,95,95,99,99,99,99,99,99,99,99,99,99 };

    for( int n=99; n>=1; n-- ) {
      int pValue = percents[n];
      if( pScore >= pValue ) {
        percentile = n;					
        break;
      }
    }
    System.out.println( "Percentile: " + percentile );  
  }
}

