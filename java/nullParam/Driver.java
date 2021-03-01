public class Driver {
  public static void main( String [] args ) {
    NullParam n = NullParam.NullString;
    System.out.println( n.getClass().getName() );
    n = NullParam.NullFloat;
    System.out.println( n.getClass().getName() );
  } 
}
