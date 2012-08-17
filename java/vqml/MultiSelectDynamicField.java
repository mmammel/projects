import java.util.Set;
import java.util.HashSet;
import java.util.List;

/**
 * Select multiple values to be included in the report query.
 * e.g. if Red, Green, and Blue are selected, the "final" val that gets placed
 * in the report query is:  'Red','Green','Blue'
 *
 * It is up to the author of the query to make sure that the Dynamic field is
 * inside an appropriate in clause:
 *
 * WHERE foo in (${...multiselect(...)}) and ...
 *
 * @author mammelma
 *
 */
public class MultiSelectDynamicField extends SelectDynamicField
{
  private boolean quoted = true;

  public MultiSelectDynamicField( String name, String label, String params, boolean quoted ) {
    super(name,label,params);
    this.quoted = quoted;
  }

  public String renderHtml(VQMLContext context, boolean submitOnChange) throws RuntimeException
  {
    StringBuilder retVal = new StringBuilder();

    if( this.subQuery != null )
    {
      for( DynamicField subQueryField : this.subQuery.getDynamicFields() )
      {
        retVal.append( subQueryField.renderHtml( context, true ) ).append( "\n" );
      }
    }

    String [] vals = context.getRequestParamValues( this.name );

    List<String []> optionList = this.buildOptionList( context );
    Set<String> selectedOptions = new HashSet<String>();

    if( vals != null && vals.length > 0 )
    {
      StringBuilder finalStr = new StringBuilder();
      for( String selected : vals )
      {
        selectedOptions.add( selected );
        if( this.quoted ) finalStr.append("'");
        finalStr.append(selected);
        if( this.quoted ) finalStr.append("'");
        finalStr.append(",");
      }

      context.putFinal(this.name, finalStr.toString().substring(0, finalStr.length() - 1));
    }

    retVal.append(this.buildFormInner( optionList, selectedOptions, submitOnChange ) );

    return retVal.toString();
  }

  protected String buildFormInner( List<String []> options, Set<String> params, boolean submitOnChange )
  {
    StringBuilder retVal = new StringBuilder();

    retVal.append( "<tr>\n" );
    retVal.append( "<td>" ).append( this.label ).append( "</td>" );
    retVal.append( "<td>" ).append( "<select " + (submitOnChange && options.size() > 0 && params.size() > 0 ? "disabled " : "") + "multiple=\"multiple\" size=\"7\" id=\"" ).append( this.name ).append( "\" name=\"" ).append( this.name ).append( "\">\n" );


    retVal.append( this.buildOptionsHtml(options, params) );

    retVal.append("</select>\n");

    if( submitOnChange && options.size() > 0 && params.size() > 0 )
    {
      // append a bunch of hidden fields for all of the values.
      for( String param : params )
      {
        retVal.append( "<input type=\"hidden\" name=\"").append(this.name).append("\" value=\"").append(param).append("\"></input>\n");
      }
    }

    retVal.append("</td>\n" );

    if( submitOnChange && options.size() > 0 && params.size() == 0 )
    {
      retVal.append("<td><input type=\"submit\" name=\"" ).append( this.name + "_submit" ).append( "\" value=\"Go\"></input></td>");
    }

    retVal.append( "</tr>\n" );

    return retVal.toString();
  }
}
