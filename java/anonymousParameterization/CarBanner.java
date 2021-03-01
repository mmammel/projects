public class CarBanner extends Banner<ExcitedDecorator> {
  public CarBanner( String message ) {
    super(message);
    this.decorator = new ExcitedDecorator();
  }

  public String prepareMessage() {
    return "This is a car banner: " + this.message;
  }
}
