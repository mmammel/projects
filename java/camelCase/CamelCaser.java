import java.io.BufferedReader;
import java.io.InputStreamReader;

public class CamelCaser {
  private static final String CAP_CHARS = "\t \"-|_()['].$#@!*=+}{,<>?/\\&^%";

  public  static String toCamelCase(String s) {
    StringBuilder sb = new StringBuilder( s.toLowerCase() );
    boolean capitalize = true;
    for( int i = 0; i < sb.length(); i++ )
    {
      if( capitalize ) sb.setCharAt(i, Character.toUpperCase( sb.charAt(i) ) );
      capitalize = CAP_CHARS.indexOf( sb.charAt(i) ) > -1;
    }

    return sb.toString();
  }

  public static String fromCamelCase( String s ) {
    String retVal = s;
    if( s.matches("^[A-Za-z]+$") ) {
        retVal = s.replaceAll( "([a-z])((?=[A-Z]))|([A-Z])([A-Z](?=[a-z]))","$1$3 $2$4" );
        retVal = ""+Character.toUpperCase(retVal.charAt(0))+(retVal.length() > 1 ? retVal.substring(1) : "");
    }

    return retVal;
  }
 
  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    String input_str = "";

    if( args.length < 1 ) {
      System.err.println( "Usage: java CamelCaser [to|from]" );
      System.exit(1);
    }

    boolean to = true;

    if( args[0].toUpperCase().equals( "FROM" ) ) {
      to = false;
    }

    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );
      
      if( to ) {
        System.out.println( "Enter some strings to make camel case" );
      } else {
        System.out.println( "Enter some camelCase to turn into labels" );
      }

      System.out.print(">> ");

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "quit" ) )
        {
          break;
        }
        else
        {
          // Process Input
          if( to ) {
            System.out.println( CamelCaser.toCamelCase( input_str ) );
          } else {
            System.out.println( CamelCaser.fromCamelCase( input_str ) );
          }
        }

        System.out.print( "\n>> " );
      }

      System.out.println( "Adios." );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

