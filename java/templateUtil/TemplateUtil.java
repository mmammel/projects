import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.*;

public class TemplateUtil {
  private static final String DEFAULT_PREFIX = "${";
  private static final String DEFAULT_POSTFIX = "}";

  private String prefix;
  private String postfix;
  private Pattern pattern;
  private String template;

  public TemplateUtil( String template, String prefix, String postfix )
  {
    this.prefix = prefix;
    this.postfix = postfix;
    this.template = template;
    this.pattern = Pattern.compile("\\Q"+this.prefix+"\\E(.*?)\\Q"+this.postfix+"\\E");
  }

  public TemplateUtil( String template )
  {
    this(template,DEFAULT_PREFIX,DEFAULT_POSTFIX);
  }

  public String processTemplate(Map<String, String> variables) throws IOException {
    StringBuffer retVal = new StringBuffer();
    Matcher m = this.pattern.matcher(this.template);
    String tempVar = null;
    String tempReplacement = null;

    while( m.find() )
    {
      tempVar = m.group(1);
      tempReplacement = variables.get(tempVar);

      if( tempVar != null && tempVar.length() > 0 && tempReplacement != null )
      {
        m.appendReplacement(retVal, tempReplacement);
      }
      else
      {
        m.appendReplacement(retVal, Matcher.quoteReplacement(m.group(0)));
      }
    }

    m.appendTail(retVal);

    return retVal.toString();
  }

  public String getPostfix() {
    return postfix;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getTemplate() {
    return template;
  }

  public static void main( String [] args )
  {
    String template = "This is a ${TEST}, and my ${FOOBAR} name is ${FIRST} ${LAST}";

    Map<String,String> vars = new HashMap<String,String>();
    vars.put("TEST","FOOBAR");
    vars.put("FIRST","Max");
    vars.put("LAST","Mammel");

    Map<String,String> vars2 = new HashMap<String,String>();
    vars2.put("TEST","Yomama");
    vars2.put("FIRST","Jevne");
    vars2.put("FOOBAR","");
    vars2.put("LAST","Bohnke");


    TemplateUtil TU = new TemplateUtil( template );

    try
    {
      System.out.println( TU.processTemplate( vars ) );
      System.out.println( TU.processTemplate( vars2 ) );
    }
    catch( Exception e )
    {
      System.out.println( "Exception! " + e.toString() );
    }
  }

}
