public class Driver {
  public static void main( String [] args ) {
    CarBanner CB = new CarBanner( "Honda Pilots are neat" );
    CB.printMessage();
    SecretBanner SB = new SecretBanner( "Shh...be very quiet" );
    SB.printMessage();

    Banner dynamic = new Banner("Dynamic Message") {
      public String prepareMessage() {
        return "Dynamic: " + this.message;
      }
    };

    dynamic.setDecorator(new ExcitedDecorator() );
    dynamic.printMessage();
    dynamic.setDecorator(new LowerCaseDecorator() );
    dynamic.printMessage();
      
  }
}
