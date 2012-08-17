/**
 * Simple Text input field, lets random strings be put into a report query.
 *  Example uses: see how many times a certain last name has scored.
 *
 * @author mammelma
 *
 */
public class TextDynamicField extends AbstractDynamicField {
  private String validationPattern = "^.*$";

  public TextDynamicField( String name, String label, String params )
  {
    super(name,label);

    if( params != null && params.trim().length() > 0 )
    {
      this.validationPattern = params;
    }
  }

  public String renderHtml(VQMLContext context, boolean submitOnChange) throws RuntimeException
  {
    String retVal = null;
    String val = context.getRequestParamValue( this.name );

    if( val != null && val.matches( this.validationPattern ) )
    {
      retVal = this.buildFormInner( submitOnChange, val );
      context.putFinal( this.name, val );
    }
    else
    {
      retVal = this.buildFormInner( submitOnChange, null );
    }

    return retVal;
  }

  private String buildFormInner( boolean submitOnChange, String value )
  {
    StringBuilder retVal = new StringBuilder();

    retVal.append( "<tr>\n" );
    retVal.append( "<td>" ).append( this.label ).append( "</td>" );
    retVal.append( "<td>" ).append( "<input " + (submitOnChange && value != null ? "readonly " : "")+ "type=\"text\" id=\"" ).append( this.name ).append( "\" name=\"" ).append( this.name ).append( "\"" );

    if( value != null )
    {
      retVal.append( " value=\"" ).append( value ).append( "\"" );
    }

    retVal.append( "></input>\n");

    if( submitOnChange && value != null )
    {
      retVal.append( "<input type=\"hidden\" name=\"").append(this.name).append("\" value=\"").append(value).append("\"></input>");
    }

    retVal.append("</td>\n" );

    if( submitOnChange && value == null )
    {
      retVal.append("<td><input type=\"submit\" name=\"" ).append( this.name + "_submit" ).append( "\" value=\"Go\"></input></td>");
    }

    retVal.append( "</tr>\n" );

    return retVal.toString();
  }

}

