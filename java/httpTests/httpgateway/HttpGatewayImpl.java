import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Expects the following protocol properties:
 *   gatewayPropertyPrefix = some prefix, like "kenexa.outbound"
 *   <gatewayPropertyPrefix>.http.url = https://...
 *   <gatewayPropertyPrefix>.http.method = POST|GET (optional: default POST)
 *   <gatewayPropertyPrefix>.http.connectTimeout = <millis> (optional: default 30000)
 *   <gatewayPropertyPrefix>.http.readTimeout = <millis> (optional: default 30000)
 *   <gatewayPropertyPrefix>.http.headers = h0=v0|h1=v1|...|hN=vN (optional)
 *   <gatewayPropertyPrefix>.http.encoding = UTF-8|ISO-8859-1|... (optional: default utf-8)
 *   <gatewayPropertyPrefix>.http.template = <the template>
 *   <gatewayPropertyPrefix>.http.successResponsePattern = <regex> (optional)
 *   <gatewayPropertyPrefix>.http.errorResponsePattern = <regex> (optional)
 *   <gatewayPropertyPrefix>.http.templateParamName = <name> (optional)
 *   <gatewayPropertyPrefix>.http.urlEncodeTemplate = true|false (optional: default false)
 *   
 *   all protocol properties are "templates", and can reference other context properties with ${prop}
 * @author mmammel
 *
 */
public class HttpGatewayImpl extends AbstractGatewayImpl {
  
  private static final Pattern HEADER_PATTERN = Pattern.compile("^(.+?)=(.+)$");

  private static final boolean doDebug = true;

  private static final String URL_PROP = "http.url";
  private static final String METHOD_PROP = "http.method";
  private static final String HEADER_PROP = "http.headers";
  private static final String ENCODING_PROP = "http.encoding";
  private static final String TEMPLATE_PROP = "http.template";
  private static final String CONNECT_TIMEOUT_PROP = "http.connectTimeout";
  private static final String READ_TIMEOUT_PROP = "http.readTimeout";
  private static final String SUCCESS_RESPONSE_PATTERN_PROP = "http.successResponsePattern";
  private static final String ERROR_RESPONSE_PATTERN_PROP = "http.errorResponsePattern";
  private static final String TEMPLATE_PARAM_PROP = "http.templateParamName";
  private static final String URL_ENCODE_PROP = "http.urlEncodeTemplate";
  
  private static final int DEFAULT_CONNECT_TIMEOUT = 30000; // 30 seconds
  private static final int DEFAULT_READ_TIMEOUT = 30000; // 30 seconds
  
  protected GatewayResult send_inner() {
    GatewayResult retVal = null;
    String url = this.context.getProperty( URL_PROP );
    String method = this.context.getProperty( METHOD_PROP );
    Map<String,String> headers = this.getHeaders();
    String encoding = this.context.getProperty(ENCODING_PROP);
    String template = this.context.getProperty(TEMPLATE_PROP);
    String successResponsePattern = this.context.getProperty(SUCCESS_RESPONSE_PATTERN_PROP);
    String errorResponsePattern = this.context.getProperty(ERROR_RESPONSE_PATTERN_PROP);
    String tempVal = this.context.getProperty(URL_ENCODE_PROP);
    boolean urlEncodeTemplate = (tempVal != null && tempVal.toUpperCase().equals("TRUE")) ? true : false;
    String templateParam = this.context.getProperty(TEMPLATE_PARAM_PROP);
    String connectTimeoutStr = this.context.getProperty(CONNECT_TIMEOUT_PROP);
    String readTimeoutStr = this.context.getProperty(READ_TIMEOUT_PROP);

    HttpURLConnection connection = null;
    
    String response = null;
    
    // apply defaults
    if( encoding == null ) encoding = "UTF-8";
    if( method == null ) method = "POST";
    
    int connectionTimeout = DEFAULT_CONNECT_TIMEOUT;
    int readTimeout = DEFAULT_READ_TIMEOUT;
    
    if( connectTimeoutStr != null && connectTimeoutStr.matches("^[0-9]+$") ) {
      connectionTimeout = Integer.parseInt(connectTimeoutStr); 
    }

    if( readTimeoutStr != null && readTimeoutStr.matches("^[0-9]+$") ) {
      readTimeout = Integer.parseInt(readTimeoutStr); 
    }
    
    try {
      if( doDebug )
      {
        System.out.println("Attempting to send message to: " + url );
      }
      
      URL endpoint = new URL(url);
      connection = (HttpURLConnection)endpoint.openConnection();
      connection.setDoInput(true);
      connection.setDoOutput(true);
      connection.setRequestMethod(method);
      connection.setConnectTimeout(connectionTimeout);
      connection.setReadTimeout(readTimeout);
      
      if( doDebug ) {
        System.out.println("Adding the following headers: " );
        for( String hn : headers.keySet() ) {
          System.out.println(hn + " -> " + headers.get(hn) );
        }
      }
      
      for( String headerName : headers.keySet() ) {
        connection.setRequestProperty( headerName, headers.get(headerName));
      }
      
      if( urlEncodeTemplate )
      {
        template = URLEncoder.encode(template, encoding);
      }
      
      if( templateParam != null )
      {
        template = templateParam + "=" + template;
      }
      
      if( doDebug ) {
        System.out.println("Sending: " + template );
      }
      
      if(method.toUpperCase().equals("GET")) {
        response = StreamUtil.stringFromStream(connection.getInputStream(), encoding);
      } else if( method.toUpperCase().equals("POST") ) {
          response = StreamUtil.streamWithResponse(connection, template, encoding);
      }else if( method.toUpperCase().equals("PUT") ) {
          response = StreamUtil.streamWithResponse(connection, template, encoding);
      }
      
      // TODO: Add more, as needed.
      
      if( response != null )
      {
        retVal = new GatewayResult(response, successResponsePattern, errorResponsePattern);     
      }
      else
      {
        retVal = new GatewayResult();
        retVal.setSuccessful(true);
        retVal.setRawResponse("No response received.");
      }
      
      retVal.setProtocolResponseMessage(connection.getResponseMessage());
      retVal.setResponseCode(connection.getResponseCode());
      
      if( retVal.getResponseCode() >= 400 )
      {
        retVal.setSuccessful(false);
      }

    } catch( IOException ioe ) { // this catches SocketTimeoutException too
      retVal = new GatewayResult();
      retVal.setSuccessful(false);
      retVal.setErrorMessage(ioe.toString());
      StringWriter stack = new StringWriter();
      ioe.printStackTrace(new PrintWriter(stack));
      stack.flush();
      retVal.setRawResponse(stack.toString());
    }
    finally {
      if( connection != null )
      {
        connection.disconnect();
      }
    }
    
    if( doDebug ) {
      System.out.println("Response is: " + retVal.toString() );
    }
    
    return retVal;
  }
  
  private Map<String,String> getHeaders()
  {
    Map<String,String> retVal = new HashMap<String,String>();
    
    try
    {
      String headerString = this.context.getProperty(HEADER_PROP);
      String [] headerPairs = headerString.split( "\\|");
      Matcher headerMatcher = null;
      for( String pair : headerPairs ) {
        headerMatcher = HEADER_PATTERN.matcher(pair);
        if( headerMatcher.matches() )
        {
          retVal.put(headerMatcher.group(1), headerMatcher.group(2));
        }
      }
    }
    catch( Exception e )
    {
      retVal.clear();
    }
    
    return retVal;
  }

}
