import org.apache.commons.math.MathException;
import org.apache.commons.math.distribution.NormalDistribution;
import org.apache.commons.math.distribution.NormalDistributionImpl;

public class PercentileTests {

    /*
     * each array is: [ rawscore, avg, sd ]
     */
    private static float [][] ZSCORE_TESTS = {
        { 5.0f, 24.77f, 4.67f },
        { 24.0f, 24.77f, 4.67f },
        { 14.0f, 24.77f, 4.67f },
        { 30.0f, 24.77f, 4.67f },
        { 25.0f, 24.77f, 4.67f },
        { 10.0f, 24.77f, 4.67f },
        { 18.0f, 24.77f, 4.67f },
        { 22.0f, 24.77f, 4.67f }        
    };
    
    private static NormalDistribution standardNormalDistribution = new NormalDistributionImpl(0.0,1.0);
    
    private static double [] ZSCORE_CUTOFFS = {
      2.326,2.054,1.881,1.751,1.645,1.555,1.476,1.405,1.341,1.282,1.227,1.175,1.126,1.08,1.036,0.994,0.954,0.915,0.878,0.842,0.806,0.772,0.739,0.706,0.674,0.643,0.613,0.583,0.553,0.524,0.496,0.468,0.44,0.412,0.385,0.358,0.332,0.305,0.279,0.253,0.228,0.202,0.176,0.151,0.126,0.1,0.075,0.05,0.025,0,-0.025,-0.05,-0.075,-0.1,-0.126,-0.151,-0.176,-0.202,-0.228,-0.253,-0.279,-0.305,-0.332,-0.358,-0.385,-0.412,-0.44,-0.468,-0.496,-0.524,-0.553,-0.583,-0.613,-0.643,-0.674,-0.706,-0.739,-0.772,-0.806,-0.842,-0.878,-0.915,-0.954,-0.994,-1.036,-1.08,-1.126,-1.175,-1.227,-1.282,-1.341,-1.405,-1.476,-1.555,-1.645,-1.751,-1.881,-2.054,-2.326
    };
    

    public static void main( String [] args ) {
      PercentileTests PT = new PercentileTests();
      PT.runTests();
    }

    public void runTests() {
      float zscore = 0.0f;
      int arrayPercentile, standardNormalPercentile, nonStandardNormalPercentile;
      for( float [] arr : ZSCORE_TESTS ) {
        zscore = (arr[0] - arr[1]) / arr[2];
        arrayPercentile = this.getArrayPercentile(zscore);
        standardNormalPercentile = this.getStandardNormalPercentile(zscore);
        nonStandardNormalPercentile = this.getNonStandardNormalPercentile(arr[0], arr[1], arr[2]);
        
        System.out.println( "["+ arrayPercentile + "," + standardNormalPercentile + "," + nonStandardNormalPercentile + "]");
      }
    }

    private int getArrayPercentile( float zscore ) {
      int retVal = 1;
      for( int i = 0; i < ZSCORE_CUTOFFS.length; i++ ) {
        if( zscore >= ZSCORE_CUTOFFS[i] ) {
          retVal = 99 - i;
          break;
        }
      }
      
      return retVal;
    }
    
    private int getPercentileFromProbability( Double prob ) {
        int retVal = 1;
        Double cumulativeProbability = 100.0 * prob;

        if( cumulativeProbability < 1.0 ) {
          retVal = 1;
        } else if( cumulativeProbability > 99.0 ) {
          retVal = 99;
        } else {
          retVal = cumulativeProbability.intValue();
        }
    
        return retVal;
    }

    private int getStandardNormalPercentile( float zscore ) {
      int retVal = 1;
      try {
        Double cumulativeProbability = standardNormalDistribution.cumulativeProbability((new Float(zscore)).doubleValue() );
        retVal = this.getPercentileFromProbability( cumulativeProbability );
      } catch( MathException me ) {
        System.out.println( "Exception! " + me.toString() );
        retVal = 1;
      }
      
      return retVal;
    }
    
    private int getNonStandardNormalPercentile( float raw, float avg, float sd ) {
      int retVal = 1;
      try {
        NormalDistribution dist = new NormalDistributionImpl(avg,sd);
        Double cumulativeProbability = dist.cumulativeProbability((new Float(raw)).doubleValue() );
        retVal = this.getPercentileFromProbability( cumulativeProbability );
      } catch( MathException me ) {
        System.out.println( "Exception! " + me.toString() );
        retVal = 1;
      }
      
      return retVal;
    }

}
