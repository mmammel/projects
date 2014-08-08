import java.util.UUID;

public class GenerateUUID {

  public static void main( String [] args ) {
    String seed = "foobar";
    if( args.length > 0 ) seed = args[0];

    System.out.println( "Random: " + UUID.randomUUID().toString() );
    System.out.println( "From seed: " + UUID.fromString( seed ).toString() );
  }

}
