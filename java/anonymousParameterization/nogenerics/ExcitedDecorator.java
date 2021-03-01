public class ExcitedDecorator extends AllCapsDecorator {
  public String decorate( String str ) {
    str = super.decorate(str);
    if( str != null ) {
      str = str + "!!!";
    }
    return str;
  }
}
