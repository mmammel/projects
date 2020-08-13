public class PathReplace {
  public static void main( String [] args ) {
    String url = "https://gamma.skillcheck.com/onlinetesting/servlet/com.skillcheck.session_management.SK_Servlet";
    System.out.println( url.replaceAll( "(^.*?)/servlet.*$", "$1/maize/flashassets/HTML/?eticket=1236416346123416234") );
  }
}
