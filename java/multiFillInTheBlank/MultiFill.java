import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.*;

public class MultiFill
{
  public static final Pattern FIELD_PATTERN = Pattern.compile( "\\$\\{(.*?)\\}" );

  public static void main( String [] args )
  {
    BufferedReader input_reader = null;
    String input_str = "";
    MultiFill MF = new MultiFill();

    try
    {
      input_reader = new BufferedReader( new InputStreamReader ( System.in ) );

      System.out.println( "Hello!" );

      System.out.print("$> ");

      while( (input_str = input_reader.readLine()) != null )
      {

        if( input_str.equalsIgnoreCase( "quit" ) )
        {
          break;
        }
        else
        {
          System.out.println( MF.renderAsForm( input_str ) );
        }

        System.out.print( "\n$> " );
      }

      System.out.println( "seeya" );

    }
    catch( Exception e )
    {
      System.out.println( "Caught an exception: " + e.toString() );
      e.printStackTrace();
    }
  }

  public String renderAsForm( String answer )
  {
    int fieldLen = 0;
    int count = 0;
    StringBuffer retVal = new StringBuffer();
    Matcher m = FIELD_PATTERN.matcher( answer );

    while( m.find() )
    {
      fieldLen = m.group(1).length();
      m.appendReplacement( retVal, "<input name=\"someId_" + (++count) + "\" size=\"" + fieldLen + "\" type=\"text\"></input>" );
    }

    m.appendTail(retVal);

    return retVal.toString();
  }

}
