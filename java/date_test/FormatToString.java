import java.text.SimpleDateFormat;

public class FormatToString {
  public static void main( String [] args ) {
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    System.out.println( "toString: " + sdf );
    System.out.println( "toPattern: " + sdf.toPattern() );
  }

}
