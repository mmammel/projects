
public class CamelCase {
  public static final void main( String [] args ) {
    String retVal = null;
    if( args.length == 1 ) {
      if( args[0].matches( "^[A-Za-z]+$" ) ) {
        retVal = args[0].replaceAll( "([a-z])((?=[A-Z]))|([A-Z])([A-Z](?=[a-z]))","$1$3 $2$4" );
        retVal = ""+Character.toUpperCase(retVal.charAt(0))+(retVal.length() > 1 ? retVal.substring(1) : "");
        System.out.println( retVal );
      } else {
        System.out.println( "Not valid input, must be all upper and lower case." );
      }
    } else {
      System.out.println( "Usage: java CamelCase <camelCaseString>" );
    }
  }  
}
