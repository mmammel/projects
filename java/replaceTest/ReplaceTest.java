public class ReplaceTest {
  public static void main( String [] args ) {
    //String in = "JOMAMA.O'KEEFE.FOO'BAR.YOMAMA";
    //String out = in.replaceAll( "'", "_" );

    //String in = "com.jomama.yomama.Q_LikerImpl";

    //String out = in.replaceAll( ".*?\\.([^.]+)$", "$1" );
    for( int i = 0; i < 1000000; i++ ) {
      String in = "com.jomama.yomama.Q_LikerImpl";
      String out = in.replaceAll( ".*?\\.([^.]+)$", "$1" );
      //String out = in.substring(in.lastIndexOf('.')+1) + "_123";
    }


    //System.out.println( out );
  }
}
