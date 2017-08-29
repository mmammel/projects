import java.util.Map;

public abstract class AbstractGatewayImpl implements Gateway {
  protected static final String DEFAULT_ENCODING = "UTF-8";
  protected GatewayContext context = null;
  
  protected abstract GatewayResult send_inner();
  
  public GatewayResult send( GatewayContext context )
  {
    this.context = context;
      return this.send_inner();
  }

}
