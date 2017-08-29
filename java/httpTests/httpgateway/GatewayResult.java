import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GatewayResult {

  private boolean successful = true;
  private String message = null;
  private String errorMessage = null;
  private String rawResponse = null;
  private int responseCode = 0;
  private String protocolResponseMessage = null;
  
  public GatewayResult() {}

  public GatewayResult(String rawResponse) {
    this(rawResponse,null,null);
  }

  
  public GatewayResult( String rawResponse, String successPattern, String errorPattern )
  {
    this.rawResponse = rawResponse;
    
    if( successPattern != null && successPattern.length() > 0 )
    {
      try
      {
        this.successful = false;
        Pattern success = Pattern.compile(successPattern);
        Matcher m = success.matcher(rawResponse);
        
        if( m.find() )
        {
          this.successful = true;
          if( m.groupCount() > 0 ) {
            this.message = m.group(1);
          }
        }
        else if( errorPattern != null && errorPattern.length() > 0 )
        {
          Pattern error = Pattern.compile(errorPattern);
          Matcher m2 = error.matcher(rawResponse);
          
          if( m2.find() )
          {
            if( m2.groupCount() > 0 ) {
              this.errorMessage = m2.group(1);
            }
          }
        }
      }
      catch( Exception e)
      {
        System.out.println("Problem creating gateway response for response: " + rawResponse);
        // mark the response as successful in case we mess up the pattern or something, we won't 
        // clutter the DB with invalid retries.
        this.successful = true;
      }
    }
    else
    {
      // if no success pattern is provided, just say it worked.  Matches current behavior.
      this.successful = true;
    }
  }
  
  public boolean isSuccessful() {
    return successful;
  }
  public void setSuccessful(boolean successful) {
    this.successful = successful;
  }
  public String getMessage() {
    return message;
  }
  public void setMessage(String message) {
    this.message = message;
  }
  public String getErrorMessage() {
    return errorMessage;
  }
  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public String getRawResponse() {
    return rawResponse;
  }

  public void setRawResponse(String rawResponse) {
    this.rawResponse = rawResponse;
  }

  public int getResponseCode() {
    return responseCode;
  }

  public void setResponseCode(int responseCode) {
    this.responseCode = responseCode;
  }

  public String getProtocolResponseMessage() {
    return protocolResponseMessage;
  }

  public void setProtocolResponseMessage(String protocolResponseMessage) {
    this.protocolResponseMessage = protocolResponseMessage;
  }
  
  public String getSummary() {
    StringBuilder sb = new StringBuilder();
      sb.append("["+Thread.currentThread().getId()+"]");
    sb.append("[").append(this.responseCode).append("]:[");
    sb.append(this.message == null ? (this.errorMessage == null ? this.rawResponse : this.errorMessage) : this.message );
    sb.append("]");
    return sb.toString();
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder("[GatewayResponse: \n");
    sb.append("[successful: ").append(this.successful).append("]\n");
    sb.append("[message: ").append(this.message).append("]\n");
    sb.append("[errorMessage: ").append(this.errorMessage).append("]\n");
    sb.append("[protocolResponse: ").append(this.protocolResponseMessage).append("]\n");
    sb.append("[responseCode: ").append(this.responseCode).append("]\n");
    sb.append("[rawResponse: ").append(this.rawResponse).append("]\n");
    sb.append("]");
    
    return sb.toString();
  }
  
}

