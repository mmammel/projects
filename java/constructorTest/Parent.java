public class Parent {
  protected String name = null;

  public Parent() {
    this.name = "parent";
    this.foobar();
  }

  public void foobar() {
    System.out.println( "Parent's foobar: name: " +this.name);
  }
}
