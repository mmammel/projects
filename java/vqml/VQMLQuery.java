import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Visual Query Markup Language (VQML)
-----------------------------------

${name~label~type(typeParameters)}
${ref:VQML Name}

name: ^[A-z0-9]+$
label: ^[ A-z0-9-:]+$
type:  text|select|[multiselect|multiselect_quoted]|multiselect_unquoted|datetime
typeParameters:

text:
  optional: validation regex (default: .*)

select:
  List of values: [name1[|value1],name2[|value2],name3[|value3],...,nameN[|valueN]]
  -- or --
  VQML query: select X as name[, Y as value] from ....  // Note you must use the "as name" and "as value" to tell it what to use in the optionlist
multiselect:
  List of values: [name1[|value1],name2[|value2],name3[|value3],...,nameN[|valueN]]
  -- or --
  VQML query: select X as name[, Y as value] from .... // Note you must use the "as name" and "as value" to tell it what to use in the optionlist

datetime:
  optional: date/time format (default: yyyy-MM-dd)


Examples:

Select all score data for some given account, some selected test within the account, between some timeframe:

SELECT * from scores (NOLOCK) WHERE
  parentKey = '${accountId~Account ID~text}' and
  testKey = '${testKey~Test Key~select(SELECT t.testName as name, t.tableKey as value from test t, TestList tl WHERE tl.parentKey = '${ref:accountId}' and t.tableKey = tl.testKey)}' and
  DBDATE >= ${startDate~Start Date~datetime(yyyyMMdd)} and
  DBDATE <= ${endDate~End Date~datetime(yyyyMMdd)}

Select all score data for some selected level 3 account, some selected test within the account, between some timeframe:

SELECT * from scores (NOLOCK) WHERE
  parentKey = '${level3Account~Level 3 Account ID~select(
                      SELECT c.CustomerName as name, c.tableKey as value FROM Customer c
                        WHERE parentKey = '${level2Account~Level 2 Account~select(
                          SELECT c.CustomerName as name, c.tableKey as value FROM Customer c
                            WHERE parentKey = '${level1Account~Level 1 Account~select(
                              SELECT c.CustomerName as name,c.tableKey as value from Customer c
                                WHERE c.customerLevel = 1)}')}')}' and
  testKey = '${testKey~Test Key~select(SELECT t.testName as name, t.tableKey as value from test t, TestList tl WHERE tl.parentKey = '${level3Account}' and t.tableKey = tl.testKey)}' and
  DBDATE >= ${startDate~Start Date~datetime(yyyyMMdd)} and
  DBDATE <= ${endDate~End Date~datetime(yyyyMMdd)}

 * @author mammelma
 *
 */
public class VQMLQuery {
  // The query, with the field definitions removed for simple placeholders.
  private String query;
  // To maintain the context on the parent form
  private String reportId = "-1";
  // the Dynamic fields
  private List<DynamicField> dynamicFields;
  //"soft" dependencies - just name refs to predefined dynamic fields
  private List<String> fieldRefs;

  private static final Pattern QUERY_VAR_PATTERN = Pattern.compile("\\$\\{(.*?)\\}");

  /**
   * The main constructor.
   * @param query
   * @param id
   */
  public VQMLQuery(String query, String id )
  {
    // Get rid of newlines.
    query = query.replaceAll( "[\r\n\f]","" );
    this.dynamicFields = new ArrayList<DynamicField>();
    this.fieldRefs = new ArrayList<String>();
    StringBuilder queryBuilder = new StringBuilder();
    VQMLParser parser = new VQMLParser();
    // Now have a list of "tokens", chunks of the query
    // that contain either plan text or a full dynamic field definition.
    List<String> tokens = parser.parse( query );
    DynamicField tempField = null;

    for( String tok : tokens )
    {
      if( tok.startsWith( "${" ) )
      {
        if( tok.matches( "^\\$\\{[^~}]+\\}$" ) )
        {
          // It's just a name reference, don't build a DynamicField.
          queryBuilder.append( tok );
          // However, this means that something is dependent on this field, so make sure it gets on the
          // "submit me after I change" list.
          String fieldName = tok.replaceAll(QUERY_VAR_PATTERN.pattern(), "$1"); // extract the name.
          this.fieldRefs.add(fieldName);
        }
        else
        {
          // It's a field definition, build a field and put the placeholder in.
          // The fields have recursive constructors, so nested fields are built here too.
          tempField = DynamicFieldFactory.getDynamicField( tok );
          queryBuilder.append( "${" ).append( tempField.getName() ).append( "}" );
          this.dynamicFields.add( tempField );
        }
      }
      else
      {
        // It's just plain query text, add it to the final query.
        queryBuilder.append( tok );
      }
    }

    this.query = queryBuilder.toString();
    this.reportId = id;
  }

  public VQMLQuery(String query)
  {
    this(query, "-1");
  }

  /**
   * Walk the tree and get all name refs
   *
   * Q0
   * |
   * D0-Q1
   * |  |
   * D1 D2-Q2
   * |     |
   * D3-Q3 D4
   * |  ||    <----  || is a name reference, so D0 has to be incrementally-submitted.
   * D5 D0
   * |
   * D7
   *
   * @return
   */
  public Set<String> getAllRefs()
  {
    Set<String> retVal = new HashSet<String>();

    for( String ref : this.fieldRefs )
    {
      retVal.add(ref);
    }

    for( DynamicField field : this.dynamicFields )
    {
      retVal.addAll( field.getFieldRefs());
    }

    return retVal;
  }

  /**
   * Build the form
   * @param action The form action (OT URL)
   * @param requestParams The POST variables from the previous request that landed us here
   * @return VQMLContext that holds the final form
   */
  public VQMLContext renderHTMLForm(String action, Map requestParams )
  {
    VQMLContext context = new VQMLContext();
    context.setRequestParams( requestParams );
    StringBuilder form = new StringBuilder();
    Set<String> refs = this.getAllRefs();

    form.append( "<form id=\"vqmlForm\" action=\""+action+"\" method=\"post\">\n" );
    form.append( "  <input type=\"hidden\" name=\"reportId\" value=\"").append(this.reportId).append("\"></input>");
    form.append( "  <table>\n" );

    for( DynamicField field : this.dynamicFields )
    {
      // If the "getAllRefs" tree walk put a name in the set, flag it as submitOnChange
      form.append( field.renderHtml( context, refs.contains(field.getName()) ) );
    }

    form.append( "    <tr><td colspan=\"2\"><input type=\"submit\" name=\"final_submit\" value=\"Submit\"></input></td></tr>" );
    form.append( "  </table>" );

    form.append( "</form>" );

    context.setHtmlForm(form.toString());

    return context;
  }

  /**
   * Actually run a vquery - but only if all of the dynamic fields have been satisfied.
   * @param query
   * @param context
   * @return Object some result.
   * @throws RuntimeException
   */
  public static Object executeQuery( VQMLQuery query, VQMLContext context ) throws RuntimeException
  {
    Object retVal = null;
    String populatedQuery = query.getPopulatedQuery(context);

    if( populatedQuery != null )
    {
      //retVal = query a database.
    }

    return retVal;
  }

  /**
   * Given a context that has request params, try to satisfy all of the
   * dynamic field references in the query.
   *
   * @param context
   * @return The fully satisfied query, or null
   */
  public String getPopulatedQuery( VQMLContext context )
  {
    StringBuffer retVal = new StringBuffer();
    Matcher m = QUERY_VAR_PATTERN.matcher(this.query);
    String tempVar = null;
    String tempReplacement = null;
    boolean satisfied = true;

    while( m.find() )
    {
      tempVar = m.group(1);
      tempReplacement = context.getFinal(tempVar);

      if( tempVar != null && tempVar.length() > 0 && tempReplacement != null )
      {
        m.appendReplacement(retVal, tempReplacement);
      }
      else
      {
        satisfied = false;
      }
    }

    m.appendTail(retVal);

    return satisfied ? retVal.toString() : null;
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder();

    sb.append( "Query: [" + this.query + "]" ).append("\n");
    sb.append( "Dynamicfields:\n" );
    for( DynamicField field : this.dynamicFields )
    {
      sb.append( field ).append("\n");
    }

    return sb.toString();
  }

  public List<DynamicField> getDynamicFields()
  {
    return this.dynamicFields;
  }

  public List<String> getFieldRefs()
  {
    return this.fieldRefs;
  }

  public String getReportId() {
    return reportId;
  }

  public void setReportId(String reportId) {
    this.reportId = reportId;
  }

  public static void main( String [] args )
  {
    VQMLQuery vq = new VQMLQuery( "SELECT * from scores (NOLOCK) WHERE parentKey = '${level3Account~Level 3 Account ID~select( SELECT c.CustomerName, c.tableKey FROM Customer c WHERE parentKey = '${level2Account~Level 2 Account~select( SELECT c.CustomerName, c.tableKey FROM Customer c WHERE parentKey = '${level1Account~Level 1 Account~select( SELECT c.CustomerName,c.tableKey from Customer c WHERE c.customerLevel = 1)}')}')}' and  testKey = '${testKey~Test Key~multiselect(SELECT t.testName, t.tableKey from test t, TestList tl WHERE tl.parentKey = '${level3Account}' and t.tableKey = tl.testKey)}' and  DBDATE >= ${startDate~Start Date~datetime(yyyyMMdd)} and  DBDATE <= ${endDate~End Date~datetime(yyyyMMdd)}" );
    //System.out.println( vq );
    System.out.println( vq.renderHTMLForm( "foobar", new HashMap() ) );
  }
}
