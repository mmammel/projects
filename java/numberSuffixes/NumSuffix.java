public class NumSuffix {
  public static void main( String [] args ) {
    for( int i = 1; i < 100; i++ ) {
      System.out.println( "" + i + getSuffix(i) );
    }
  }

  public static String getSuffix( int num ) {
    String retVal = "th";
    if( num > 10 && num < 20 ) {
      retVal = "th";
    } else if( num % 10 == 1 ) {
      retVal = "st";
    } else if( num % 10 == 2 ) {
      retVal = "nd";
    } else if( num % 10 == 3 ) {
      retVal = "rd";
    } else {
      retVal = "th";
    }
  
    return retVal;
  }
}
