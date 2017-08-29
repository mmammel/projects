import java.util.Map;

public interface Gateway {
  public static final String PREFIX_PROP = "gatewayPropertyPrefix";

  public GatewayResult send(GatewayContext context);
}

