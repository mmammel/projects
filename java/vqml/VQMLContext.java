import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * The context holds four main things:
 *
 * 1. The request params from the previous submit
 * 2. The final values of the dynamic fields that have been able to be run so far
 * 3. The headers that need to be added to the top of the page in order for the date
 *    picker fields to work.
 * 4. The HTML form as it could be built from the given request params.
 *
 * @author mammelma
 *
 */
public class VQMLContext
{
  private List<String> headers = new ArrayList<String>();
  private Map requestParams = null;
  private Map<String,String> finals = new HashMap<String,String>();
  private String htmlForm = null;

  /*
   * Because I wanted to use the fancy datepicker stuff,
   * We need to add some javascript at the top of the page to
   * "datepickerize" an input field if we add a date field.
   */
  public List<String> getHeaders() {
    return this.headers;
  }

  public void addHeader( String header )
  {
    this.headers.add( header );
  }

  public Map getRequestParams() {
    return this.requestParams;
  }

  public String [] getRequestParamValues( String name )
  {
    String [] retVal = null;

    if( this.requestParams != null )
    {
      retVal = (String [])this.requestParams.get( name );
    }

    return retVal;
  }

  public String getRequestParamValue( String name )
  {
    String retVal = null;
    String [] result = this.getRequestParamValues( name );

    if( result != null )
    {
      retVal = result[0];
    }

    return retVal;
  }

  public void setRequestParams( Map requestParams )
  {
    this.requestParams = requestParams;
  }

  public Map<String,String> getFinals()
  {
    return this.finals;
  }

  public String getFinal( String name )
  {
    return this.finals.get( name );
  }

  public void putFinal( String name, String val )
  {
    this.finals.put( name, val );
  }

  public String getHtmlForm() {
    return htmlForm;
  }

  public void setHtmlForm(String htmlForm) {
    this.htmlForm = htmlForm;
  }

}
