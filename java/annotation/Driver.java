import java.util.Map;
import java.util.List;

public class Driver {
  public static void main( String [] args ) {
    Person p = new Person("Max", "Mammel", 38);
    p.addFriend( new Person( "Sophia", "Mammel", 6));
    p.addFriend( new Person( "Jevne", "Mammel", 36));

    Programmer prog = new Programmer( "Max", "Mammel", 38, "Java" );

    Map<String,Object> mymap = prog.getMap();

    for( String key : mymap.keySet() ) {
      System.out.println( key + " -> " + mymap.get(key) );
    }

    List<Person> friends = (List<Person>)mymap.get("friends");   
  
    if( friends != null ) {
      for( Person friend : friends ) {
        System.out.println( "Friend: " + friend );
      }
    }
  }
}
