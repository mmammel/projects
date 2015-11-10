public class Child extends Parent {
  public Child() {
    super();
    this.name = "child";
  }

  public void foobar() {
    super.foobar();
    System.out.println( "Child's foobar: name:" + this.name );
  }
}
