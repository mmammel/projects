import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.*;

public class RegExValidate
{

  private static final Pattern NUMERIC = Pattern.compile("[0-9]{1}[0-9]*");
  private static final Pattern OPERATOR = Pattern.compile("[+-]");
  private static final Pattern UNITS = Pattern.compile("(?i)MILLIS|SECONDS|MINUTES|HOURS|DAYS|WEEKS|MONTHS|YEARS|DATE\\[.*?\\]");
  private static final Pattern VARIABLE = Pattern.compile("(?i)expireDate|startDate|lastAction|batchTimeStamp|now");

  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    String input_str = "";

    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( "Wassssssaaaap!" );

      System.out.print("> ");

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "quit" ) )
        {
          break;
        }
        else
        {
          if( NUMERIC.matcher( input_str ).matches() )
          {
            System.out.println( input_str + " is a numeric." );
          }
          else if( OPERATOR.matcher( input_str ).matches() )
          {
            System.out.println( input_str + " is an operator." );
          }
          else if( UNITS.matcher( input_str ).matches() )
          {
            System.out.println( input_str + " is a unit." );
          }
          else if( VARIABLE.matcher( input_str ).matches() )
          {
            System.out.println( input_str + " is a variable." );
          }
          else
          {
            System.out.println( input_str + " is not known." );
          }
        }

        System.out.print( "\n> " );
      }

      System.out.println( "Goodbye." );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }
}