public class BuildEnums {
  public static void main( String [] args ) {
    String [] words = { "Zero", "One", "Two", "Three", "Four", "Five", "Six" };
    
    for( int i = 0; i < words.length; i++ ) {
      for( int j = 0; j <= i; j++ ) {
        System.out.println( words[i] + words[j] + "("+i+","+j+")," );
      }
    }
  }
}
