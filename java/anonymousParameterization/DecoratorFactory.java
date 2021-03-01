public class DecoratorFactory {
  public static Decorator getDecorator( String s ) {
    Decorator retVal = new LowerCaseDecorator();

    if( s != null ) {
      s = s.toLowerCase();
      if( s.startsWith("l") ) {
        retVal = new LowerCaseDecorator();
      } else if( s.startsWith( "c" ) ) {
        retVal = new AllCapsDecorator();
      } else if( s.startsWith( "e" ) ) {
        retVal = new ExcitedDecorator();
      }
    }

    return retVal;
  }
}
