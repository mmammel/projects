import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RegionMatches
{
    public String createValidWebURL( String url )
    {
        String retVal = url;

        if( url != null )
        {
            retVal = url.trim();

            if( !retVal.regionMatches(true,0,"http://",0,7))
            {
                retVal = "http://" + retVal;
            }
        }

        return retVal;
    }

  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    String input_str = "";
    RegionMatches RM = new RegionMatches();

    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( "Hola.  Enter URLs to see if they need http in front." );

      System.out.print("> ");

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "quit" ) )
        {
          break;
        }
        else
        {
          if( input_str.equalsIgnoreCase( "null" ) )
          {
            System.out.println( RM.createValidWebURL( null ) );
          }
          else
          {
            System.out.println( RM.createValidWebURL( input_str ) );
          }

        }

        System.out.print( "\n> " );
      }

      System.out.println( "Adios!" );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
    

}
