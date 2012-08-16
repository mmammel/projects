import java.io.*;

public class OTIntern {

  public boolean isBeg( String str )
  {
    String internal = str.intern();
        return (internal == "BEG" || internal == "DEB" || internal == "PRI" || internal == "INC" || internal == "ANF"
             ||internal == "INI" || internal == "NYB");

  }


  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    String input_str = "";
    OTIntern OTI = new OTIntern();

    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( "Hola" );

      System.out.print("> ");

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "quit" ) )
        {
          break;
        }
        else
        {
           System.out.println( OTI.isBeg( input_str ) ? "Is Beginner" : "Is Not Beginner" );
        }

        System.out.print( "\n> " );
      }

      System.out.println( "adios" );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}

