import java.util.List;
import java.util.ArrayList;

public class Person extends AbstractMappable {
  @MapKey("first")
  protected String fname;
  @MapKey("last")
  protected String lname;
  @MapKey("age")
  protected int age;
  @MapKey("friends")
  protected List<Person> friends;

  public Person( String f, String l, int a ) {
    this.fname = f;
    this.lname = l;
    this.age = a;
  }

  public String getFName() {
    return this.fname;
  }

  public void setFName( String fname ) {
    this.fname = fname;
  }

  public String getLName() {
    return this.lname;
  }

  public void setLName( String lname ) {
    this.lname = lname;
  }

  public int getAge() {
    return this.age;
  }

  public void setAge( int age ) {
    this.age = age;
  }

  public void addFriend( Person p ) {
    if( this.friends == null ) {
      this.friends = new ArrayList<Person>();
    }
    
    this.friends.add( p );
  }

  public String toString() {
    return "[" + this.fname + " " + this.lname + ", " + this.age + "]";
  }
}
