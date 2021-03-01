public abstract class Banner {
  protected Decorator decorator;
  protected String message;
  protected Banner( String message ) {
    this.message = message;
  }
  public void printMessage() {
    System.out.println( this.decorator.decorate( this.prepareMessage() ) );
  }
  public abstract String prepareMessage();

  public void setDecorator( Decorator d ) {
    this.decorator = d;
  }
}
