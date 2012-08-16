import java.io.BufferedReader;
import java.io.InputStreamReader;

public class DateFormatDetector {
    private static final String [][] FORMAT_MAPPING = {
            {"[0-9]{2}/[0-9]{2}/[0-9]{4}","MM/dd/yyyy"},
            {"[0-9]{2}-[0-9]{2}-[0-9]{4}","MM-dd-yyyy"},
            {"[0-9]{2}-[a-zA-Z]{3}-[0-9]{4}","dd-MMM-yyyy"},
            {"[0-9]{4}-[0-9]{2}-[0-9]{2}", "yyyy-MM-dd"},
            {"[0-9]{4}/[0-9]{2}/[0-9]{2}", "yyyy/MM/dd"}
    };


    public static void main( String [] args )
      {
        BufferedReader input_reader = null;
        String input_str = "";

        DateFormatDetector DFD = new DateFormatDetector();
        try
        {
          input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

          System.out.println( "Hola!" );

          System.out.print("Enter a Date: ");

          while( (input_str = input_reader.readLine()) != null )
          {

            if( input_str.equalsIgnoreCase( "quit" ) )
            {
              break;
            }
            else
            {
              System.out.println( "Found: " + DFD.getFormat( input_str ) );

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

    public String getFormat( String dateString )
    {
      String retVal = null;
      for( String [] fmts : FORMAT_MAPPING )
      {
        if( dateString.matches(fmts[0]) )
        {
          retVal = fmts[1];
          break;
        }
      }

      return retVal;
    }
}