public abstract class Banner<T extends Decorator> {
  protected T decorator;
  protected String message;
  protected Banner( String message ) {
    this.message = message;
  }
  public void printMessage() {
    System.out.println( this.decorator.decorate( this.prepareMessage() ) );
  }
  public abstract String prepareMessage();

  public void setDecorator( T d ) {
    this.decorator = d;
  }
}
