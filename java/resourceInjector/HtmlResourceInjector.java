import java.util.regex.*;

public class HtmlResourceInjector
{
  public static final Pattern RESOURCE_PATTERN = Pattern.compile("(?si)(<(?:script|link|img).*?(?:src|href)=\")(.*?)\"");

  private String baseUrl;
  private String identifier;

  public HtmlResourceInjector( String baseUrl, String identifier )
  {
    this.baseUrl = baseUrl;
    this.identifier = identifier;
  }

  public StringBuffer doReplace( StringBuffer src )
  {
    StringBuffer retVal = new StringBuffer();
    Matcher m = RESOURCE_PATTERN.matcher( src );

    while( m.find() )
    {
      m.appendReplacement( retVal, "$1" + this.baseUrl + "/" + this.identifier + "/$2\"" );
    }

    m.appendTail(retVal);

    return retVal;
  }

  public static void main( String [] args )
  {
    HtmlResourceInjector HRI = new HtmlResourceInjector("http://abc.123.com","mammel");

    StringBuffer test = new StringBuffer();
    test.append( "Masdfasdjflkasjdf<script type=\"text/javascript\" src=\"js/foobar/foo.js\"></script>sadfasdf\n");
    test.append( "asdfaasda<link rel=\"css\" type=\"text/css\" href=\"css/styles/mystyle.css\" />sdfasdfasdn\n");
    test.append( "asdfasfasdfasdfasdfasdf<img src=\"some/file.jpg\" />sadfasf");

    System.out.println( HRI.doReplace( test ).toString() );
  }
}