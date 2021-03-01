public class Driver {
  public static void main( String [] args ) {
    String decoratorString = args.length >= 1 ? args[0] : "l";
    Decorator dynamicDecorator = DecoratorFactory.getDecorator(decoratorString);
    CarBanner CB = new CarBanner( "Honda Pilots are neat" );
    CB.printMessage();
    SecretBanner SB = new SecretBanner( "Shh...be very quiet" );
    SB.printMessage();

    Banner<Decorator> dynamic = new Banner<Decorator>("Dynamic Message") {
      public String prepareMessage() {
        return "Dynamic: " + this.message;
      }

      public void setDecorator( Decorator d ) {
        this.decorator = d;
      }
    };

    dynamic.setDecorator( dynamicDecorator );
    dynamic.printMessage();
    dynamic.setDecorator( new ExcitedDecorator() );
    dynamic.printMessage();
      
  }
}
