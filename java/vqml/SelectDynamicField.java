import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/**
 * Select a single value from list (static or DB driven) to use in the report query.
 * @author mammelma
 *
 */
public class SelectDynamicField extends AbstractDynamicField
{
  protected VQMLQuery subQuery = null;
  protected List<String> optionList = null;

  public SelectDynamicField( String name, String label, String params ) {
    super( name, label );

    if( params != null && params.trim().length() > 0 )
    {
      if( params.trim().startsWith("[") )
      {
        // It's a static list - each "param" might be a single string (foo) or a name value pair (foo|bar) - split later when rendering.
        optionList = new ArrayList<String>();
        params = params.substring( 1, params.lastIndexOf("]") );
        for( String param : params.split(",") )
        {
           optionList.add( param );
        }
      }
      else
      {
        this.subQuery = new VQMLQuery( params );
      }
    }
    else
    {
      // Exception?
    }

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

    String val = context.getRequestParamValue( this.name );

    List<String []> optionList = this.buildOptionList( context );
    String [] defaultOption = { "Select One", "" };
    optionList.add(0, defaultOption ); // Add the default option for single select.
    Set<String> selectedOption = new HashSet<String>();

    if( val != null && val.length() > 0 )
    {
      selectedOption.add( val );
      context.putFinal( this.name, val );
    }

    retVal.append( this.buildFormInner( optionList, selectedOption, submitOnChange ) );

    return retVal.toString();
  }

  protected String buildFormInner( List<String []> options, Set<String> params, boolean submitOnChange )
  {
    StringBuilder retVal = new StringBuilder();

    retVal.append( "<tr>\n" );
    retVal.append( "<td>" ).append( this.label ).append( "</td>" );
    retVal.append( "<td>" ).append( "<select " + (submitOnChange && options.size() > 1 && params.size() > 0 ? "disabled " : "") + "id=\"" ).append( this.name ).append( "\" name=\"" ).append( this.name ).append( "\">\n" );

    retVal.append( this.buildOptionsHtml(options, params) );

    retVal.append("</select>");

    if( submitOnChange && options.size() > 1 && params.size() > 0 )
    {
      // append a bunch of hidden fields for all of the values.
      for( String param : params )
      {
        retVal.append( "<input type=\"hidden\" name=\"").append(this.name).append("\" value=\"").append(param).append("\"></input>\n");
      }
    }

    retVal.append("</td>\n" );

    if( submitOnChange && options.size() > 1 && params.size() == 0 )
    {
      retVal.append("<td><input type=\"submit\" name=\"" ).append( this.name + "_submit" ).append( "\" value=\"Go\"></input></td>");
    }

    retVal.append( "</tr>\n" );

    return retVal.toString();
  }

  protected String buildOptionsHtml( List<String []> options, Set<String> params )
  {
    StringBuilder retVal = new StringBuilder();

    for( String [] opt : options )
    {
      retVal.append( "<option " ).append( params.contains( opt[1] ) ? "selected " : "" ).append( "value=\"" ).append( opt[1] ).append( "\">").append( opt[0] ).append( "</option>\n" );
    }

    return retVal.toString();
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder(super.toString());
    sb.append( "[Params:\n" );
    if( this.optionList != null )
    {
      for( String option : this.optionList )
      {
        sb.append( "[" ).append( option ).append("]\n");
      }
    }
    else if( this.subQuery != null )
    {
      sb.append( this.subQuery );
    }

    sb.append( "\n]");

    return sb.toString();
  }

  protected List<String []> buildOptionList( VQMLContext context )
  {
    List<String []> retVal = new ArrayList<String []>();
    String [] tempPair = null;

    if( this.optionList != null )
    {
      String [] tempSplit = null;
      // a static option list was passed.
      for( String opt : this.optionList )
      {
        tempSplit = opt.split( "\\|" );

        if( tempSplit.length == 2 )
        {
          retVal.add( tempSplit );
        }
        else
        {
          tempPair = new String [2];
          tempPair[0] = opt;
          tempPair[1] = opt;
          retVal.add( tempPair );
        }
      }
    }
    else if( this.subQuery != null )
    {
      // It's a VQMLQuery, try to run it - might not have all of it's params satisfied.
      // If this is the case, just return the empty list, otherwise execute it.
      Object queryResults = VQMLQuery.executeQuery(this.subQuery, context);
      if( queryResults != null )
      {
        // The query was satisfied, and we have the results.
        Object tempResult = null;
        String name = null, value = null;
        /*while( queryResults.getReportResults().hasNext() )
        {
          tempResult = queryResults.getReportResults().next();
          name = tempResult.getStringVal("name");
          value = tempResult.getStringVal("value");
          tempPair = new String [2];
          if( name != null ) tempPair[0] = name;
          tempPair[1] = value == null ? name : value;
          retVal.add(tempPair);
        }*/
      }
    }
    else
    {
      //error!  shouldn't happen.
    }

    return retVal;
  }

  public Set<String> getFieldRefs()
  {
    Set<String> retVal = new HashSet<String>();

    if( this.subQuery != null )
    {
      retVal.addAll(this.subQuery.getAllRefs());
    }

    return retVal;
  }
}