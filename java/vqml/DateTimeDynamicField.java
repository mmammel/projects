import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Stick a cool jquery datepicker into the form to select a date for inclusion
 * in the report query.
 * @author mammelma
 *
 */
public class DateTimeDynamicField extends AbstractDynamicField
{
  private String dateFormat = "yyyyMMdd"; //dbdate format
  // The format returned by jquery's date picker.
  private static final String DATE_PARAM_PATTERN = "^[0-9]{2}/[0-9]{2}/[0-9]{4}$";
  private static final SimpleDateFormat PARAM_FORMAT = new SimpleDateFormat( "MM/dd/yyyy" );

  public DateTimeDynamicField( String name, String label, String params )
  {
    super( name, label );

    if( params != null && params.trim().length() > 0 )
    {
      this.dateFormat = params.trim();
    }
  }

  public String renderHtml(VQMLContext context, boolean submitOnChange) throws RuntimeException
  {
    String retVal = null;
    context.addHeader( this.buildJQueryHeader() );
    String date = context.getRequestParamValue( this.name );

    if( date != null && date.matches( DATE_PARAM_PATTERN ) )
    {
      retVal = this.buildFormInner(submitOnChange, date );
      context.putFinal( this.name, this.buildFinal(date) );
    }
    else
    {
      retVal = this.buildFormInner(submitOnChange, null );
    }

    return retVal;
  }

  private String buildFormInner( boolean submitOnChange, String date )
  {
    StringBuilder retVal = new StringBuilder();

    retVal.append( "<tr>\n" );
    retVal.append( "<td>" ).append( this.label ).append( "</td>" );
    retVal.append( "<td>" ).append( "<input " + (submitOnChange && date != null ? "disabled " : "") + "type=\"text\" id=\"" ).append( this.name ).append( "\" name=\"" ).append( this.name ).append( "\"" );

    if( date != null )
    {
      retVal.append( " value=\"" ).append( date ).append( "\"" );
    }

    retVal.append( "></input>\n" );

    if( submitOnChange && date != null )
    {
      retVal.append( "<input type=\"hidden\" name=\"").append(this.name).append("\" value=\"").append(date).append("\"></input>");
    }

    retVal.append("</td>\n" );

    if( submitOnChange && date == null )
    {
      retVal.append("<td><input type=\"submit\" name=\"" ).append( this.name + "_submit" ).append( "\" value=\"Go\"></input></td>");
    }


    retVal.append( "</tr>\n" );

    return retVal.toString();
  }

  private String buildJQueryHeader()
  {
    StringBuilder header = new StringBuilder();
    header.append( "<script type=\"text/javascript\">\n" );
    header.append( "    $(function() {\n" );
    header.append( "          $( \"#" + this.name + "\" ).datepicker();\n" );
    header.append( "    });\n" );
    header.append( "</script>\n" );

    return header.toString();
  }

  private String buildFinal( String date )
  {
     String retVal = null;
     Date theDate = null;
     try
     {
       theDate = PARAM_FORMAT.parse( date );
       retVal = new SimpleDateFormat( this.dateFormat ).format( theDate );
     }
     catch( ParseException e )
     {
       // Error, got a bad param.
     }

     return retVal;
  }
}

