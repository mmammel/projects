
public class Driver {
  public static void main( String [] args ) {
    MyObject MO = new MyObject( 1, "One", "Max", "Mammel", "Jevne", "Sophia", "Malla" );
    String xml = JacksonUtil.getXMLString( MO );
    System.out.println( xml );
  }
}
