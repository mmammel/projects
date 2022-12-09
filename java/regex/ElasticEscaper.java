public class ElasticEscaper {
  private static String ELASTIC_RESERVED_PATTERN = "([\\Q+-=&|!(){}\\][^\"~*<>?:/\\\\E])";

  public String escapeReservedCharacters( String s ) {
    String retVal = s.replaceAll( ELASTIC_RESERVED_PATTERN, "\\\\$1" );
    return retVal;
  }

  public static void main( String [] args ) {
    ElasticEscaper EE = new ElasticEscaper();
    System.out.println( EE.escapeReservedCharacters( args[0] ) );
  }
}
