
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "LambdaInvokation")
@XmlAccessorType(XmlAccessType.NONE)
public class InvokeRequest {

  @XmlElement(name = "async")
  private boolean async;

  @XmlElement(name = "functionName")
  private String functionName;

  @XmlAnyElement(lax = true)
  private Object payload;

  public String toString() {
    StringBuilder sb = new StringBuilder();
    String payloadJson = null;
    try {
      payloadJson = this.payload == null ? "null" : JacksonUtil.getJsonString(this.payload, false);
    } catch( Exception e ) {
      payloadJson = e.toString();
    }

    sb.append( "[ async: " ).append( ""+this.async).append("\n");
    sb.append( "  functionName: " ).append(this.functionName).append("\n");
    sb.append( "  payload: ").append( this.payload == null ? "null" : payloadJson ).append("\n");
    sb.append( "  payload type: ").append( this.payload == null ? "null" : payload.getClass().getName() ).append(" ]\n" );
    
    return sb.toString();
  }
}
