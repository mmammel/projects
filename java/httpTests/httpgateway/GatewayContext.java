import java.util.HashMap;
import java.util.Map;

public class GatewayContext {
  private Map<String,String> context = null;
  
  public GatewayContext( Map<String,String> context )
  {
    this.context = new HashMap<String,String>();
    this.context.putAll(context);
  }
  
  public String getProperty( String property ) {
    String retVal = null;
    try {
      String prefix = this.context.get(Gateway.PREFIX_PROP);
      if( prefix == null ) {
        prefix = "";
      } else {
        prefix += ".";
      }
      TemplateUtil templateProcessor = new TemplateUtil( this.context.get(prefix + property) );
      retVal = templateProcessor.processTemplate(this.context);
    }
    catch( Exception e ) {
      retVal = null;
    }
    
    return retVal;
  }
  
}

