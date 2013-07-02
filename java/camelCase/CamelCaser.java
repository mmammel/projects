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
 
  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    String input_str = "";

    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( "Enter some strings to make camel case" );

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
          System.out.println( CamelCaser.toCamelCase( input_str ) );
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

