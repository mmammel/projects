public class SecretBanner extends Banner {
  public SecretBanner( String message ) {
    super(message);
    this.decorator = new LowerCaseDecorator();
  }

  public String prepareMessage() {
    return "This is a secret banner: " + this.message;
  }
}
