public class RelativePathReplace {
  public static final String RELATIVE_IMAGE_PATHS = "(?<=[\"'])((?:/onlinetesting)?/images/)";

  public static final String TEST_INPUT = "blah blah <img src='/onlinetesting/images/foo.jpg'> <img src=\"/onlinetesting/images/foo.jpg\"> <img src='/images/foobar.jpg'> <img src=\"/images/foo.jpg\"> <img src=\"https://server/images/foobar.jpg\"";

  public static void main( String [] args  )
  {
    String result = TEST_INPUT.replaceAll( RELATIVE_IMAGE_PATHS, "https://www.jomama.com$1" );
    System.out.println( result );
  }
}
