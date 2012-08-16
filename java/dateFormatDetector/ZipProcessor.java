import java.io.BufferedReader;
import java.io.InputStreamReader;

public class ZipProcessor {

    public static void main( String [] args )
      {
        BufferedReader input_reader = null;
        String input_str = "";

        ZipProcessor ZP = new ZipProcessor();
        try
        {
          input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

          System.out.println( "Hola!" );

          System.out.print("Enter a zip: ");

          while( (input_str = input_reader.readLine()) != null )
          {

            if( input_str.equalsIgnoreCase( "quit" ) )
            {
              break;
            }
            else
            {
              System.out.println( "Got: " + ZP.fixUSPostalCode( input_str ) );

            }

            System.out.print( "\nEnter a Date: " );
          }

          System.out.println( "Adios!" );

        }
        catch( Exception e )
        {
          System.out.println( "Caught an exception: " + e.toString() );
          e.printStackTrace();
        }
  }


    public String fixUSPostalCode( String zip )
    {
        StringBuilder sb = null;
        if( zip != null && zip.matches("[0-9]+?\\.0") )
        {
            zip = zip.substring(0,zip.indexOf(".0"));
            int len = zip.length();
            for( int i = 0; i < 5 - len; i++ )
            {
                zip = "0" + zip;
            }
        }
        
        return zip;
    }
}

