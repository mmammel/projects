import org.apache.commons.math.distribution.*;
import org.apache.commons.math.MathException;
import java.text.NumberFormat;

public class PercentileTest {
  private float standardDeviation = 0.0f;
  private float mean = 0.0f;

  public PercentileTest( float mean, float sd ) {
    this.mean = mean;
    this.standardDeviation = sd;
  }

  public static void main( String [] args ) {
    try {
      NumberFormat nf = NumberFormat.getIntegerInstance();
      float m = Float.parseFloat( args[0] );
      float s = Float.parseFloat( args[1] );
      float r = Float.parseFloat( args[2] );
      PercentileTest PT = new PercentileTest( m, s );
      System.out.println( "From raw, " + r + ": " + nf.format(PT.getPercentileFromRawScore( r )) );
      System.out.println( "From z-score, " + r + ": " + nf.format(PT.getPercentileFromZScore( r )) );
      System.out.println( "Back to Z-Score: " + PT.getZScoreFromProbability( PT.getPercentileFromZScore( r ) ) );
    } catch( MathException me ) {
      System.out.println( "Exception!: " + me.toString() );
    }
  }

  private double getZScore( float raw ) {
    double retVal = 0.0;

    if( this.mean != 0.0f ) {
      retVal = new Float((raw - this.mean) / this.standardDeviation).doubleValue();
    }
    return retVal;
  }

  public float getPercentileFromRawScore( float raw ) throws MathException {
    NormalDistribution nd = new NormalDistributionImpl( this.mean, this.standardDeviation );
    return 100.0f * new Double( nd.cumulativeProbability( raw ) ).floatValue();
  }

  public float getPercentileFromZScore( float raw ) throws MathException {
    NormalDistribution nd = new NormalDistributionImpl( 0.0, 1.0 );
      System.out.println( "Unrounded: " + nd.cumulativeProbability( this.getZScore( raw ) ) );
    return 100.0f * new Double( nd.cumulativeProbability( this.getZScore( raw ) ) ).floatValue();
  }

  public float getZScoreFromProbability( float p ) throws MathException {
    NormalDistribution nd = new NormalDistributionImpl( this.mean, this.standardDeviation );
    double num = new Float(p / 100.0).doubleValue();
    return new Double( nd.inverseCumulativeProbability( num ) ).floatValue();
  }
}

