public class LowerCaseDecorator extends Decorator {
  public String decorate( String str ) {
    return str != null ? str.toLowerCase() : null;
  }
}

