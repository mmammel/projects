public class ReplaceTest {
  public static void main( String [] args ) {
      //String in = "com.jomama.yomama.Q_LikerImpl";
      //String out = in.replaceAll( ".*?\\.([^.]+)$", "$1" );
      //String in = args[0].toUpperCase();
      String in = args[0];
      //System.out.println( in + " -> " + in.replaceAll("^(.+?)-.*$", "$1") );
      System.out.println(in + " -> " + in.replaceAll("^.*?://(.*?)/.*$", "tcp://$1:61616"));
  }
}
