import java.util.*;

public class Driver {
  public static void main( String [] args ) {
    Child c = new Child();
System.out.println("---");
    Parent p = new Parent();
System.out.println("---");
    Parent p2 = new Child();

    c.foobar();

    System.out.println(TimeZone.getTimeZone("America/Indiana").getID());
    System.out.println(TimeZone.getTimeZone("GMT+130").getID());
    System.out.println(TimeZone.getTimeZone("GMT+120").getID());

    List<String> tzs = new ArrayList<String>();

    for( String tz : TimeZone.getAvailableIDs() ) {
      tzs.add(tz);
    }

    Collections.sort( tzs );
    for( String tz2 : tzs ) { System.out.println( tz2 ); }
  }
}
