import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.ArrayList;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class Payload {
  @XmlElement(name = "name")
  private String name = null;
  @XmlElement(name = "title")
  private String title = null;
  @XmlElement(name = "age")
  private int age = 0;
  @XmlElement(name = "skills")
  private List<String> skills;

  public Payload() {
    this.skills = new ArrayList<String>();
  }

  public String getName() {
    return this.name;
  }

  public void setName( String name ) {
    this.name = name;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle( String title ) {
    this.title = title;
  }

  public int getAge() {
    return this.age;
  }

  public void setAge( int age ) {
    this.age = age;
  }

  public List<String> getSkills() {
    return this.skills;
  }

  public void setSkills( List<String> skills ) {
    this.skills = skills;
  }

  public void addSkill( String skill ) {
    if( this.skills == null ) this.skills = new ArrayList<String>();

    this.skills.add( skill );
  }
}