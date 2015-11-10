public class Programmer extends Person {
  @MapKey("language")
  private String language;

  public Programmer( String f, String l, int age, String lang ) {
    super( f, l, age );
    this.language = lang;
  }
}
