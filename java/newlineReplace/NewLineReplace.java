public class NewLineReplace {
  public static void main( String [] args ) {
    StringBuilder sb = new StringBuilder();
    sb.append( "Hello my name is\r\nMax, what is your\n name?");

    System.out.println( sb.toString() );
    System.out.println( replaceNewLines(sb.toString()) );
  }

  public static String replaceNewLines( String s ) {
    s = s.replaceAll( "\n", "'+CHAR(10)+'" );
    s = s.replaceAll( "\r", "'+CHAR(13)+'" );
    return s;
  }
}
