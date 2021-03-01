public class AllCapsDecorator extends Decorator {
  public String decorate( String str ) {
    return str != null ? str.toUpperCase() : null;
  }
}
