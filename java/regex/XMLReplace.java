import java.util.regex.*;

public class XMLReplace {
  private static final Pattern NUM_METRICS_PATTERN = Pattern.compile("<NumMetrics>([0-9]+)</NumMetrics>");

  public static void main( String [] args ) {

    StringBuilder input = new StringBuilder();
    input.append("<root>\n");
    input.append("  <NumMetrics>2</NumMetrics>\n");
    input.append("  <Metric>\n");
    input.append("    <MetricName>Rating</MetricName>\n");
    input.append("    <MetricDesc>Rating</MetricDesc>\n");
    input.append("    <MetricValue>\n");
    input.append("      <Rating>UNO</Rating>\n");
    input.append("    </MetricValue>\n");
    input.append("  </Metric>\n");
    input.append("  <Metric>\n");
    input.append("    <MetricName>Rating</MetricName>\n");
    input.append("    <MetricDesc>Rating</MetricDesc>\n");
    input.append("    <MetricValue>\n");
    input.append("      <Rating>DOS</Rating>\n");
    input.append("    </MetricValue>\n");
    input.append("  </Metric>\n");
    input.append("</root>\n");

    System.out.println( input.toString() );

    System.out.println( insertRatingMetricNode( input.toString() )) ;

  }

  public static String insertRatingMetricNode( String xml ) {
    String retVal = xml;
    try {
      String numMetrics = "";
      int num = 0;
      Matcher m = NUM_METRICS_PATTERN.matcher(xml);
      if( m.find() ) {
        numMetrics = m.group(1);
        num = Integer.parseInt(numMetrics);
        ++num;
        retVal = m.replaceAll( "<NumMetrics>" + num + "</NumMetrics>\n" + getRatingMetricNode() );
      }
    } catch( Exception e ) {
      System.out.println("Failed to insert the rating metric node into: " + xml + "\nDue to: " + e.toString());
    }
    
    return retVal;
  }

  public static String getRatingMetricNode() {
    StringBuilder sb = new StringBuilder();
    
    sb.append("<Metric>\n");
    sb.append("  <MetricName>Rating</MetricName>\n");
    sb.append("  <MetricDesc>Rating</MetricDesc>\n");
    sb.append("  <MetricValue>\n");
    sb.append("    <Rating>NA</Rating>\n");
    sb.append("  </MetricValue>\n");
    sb.append("</Metric>");
    
    return sb.toString();
  }
}
