import java.util.List;
import java.util.ArrayList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "myObject")
@XmlAccessorType(XmlAccessType.NONE)
public class MyObject {

  @XmlElement(name = "id")
  private int id;

  @XmlElement(name = "name")
  private String name;

  @XmlElement(name = "words")
  private List<String> words;

  public MyObject( int id, String name, String ... words ) {
    this.id = id;
    this.name = name;
    this.words = new ArrayList<String>();
    for( String word : words  ) {
      this.words.add( word );
    }
  }

  public int getId() {
    return this.id;
  }
  public void setId( int id ) {
    this.id = id;
  }
  public String getName() {
    return this.name;
  }
  public void setName( String name ) {
    this.name = name;
  }
  public List<String> getWords() {
    return this.words;
  }
  public void setWords( List<String> words ) {
    this.words = words;
  }
}
