import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixFormat;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.util.Precision;
import java.text.NumberFormat;

public class DensityFactory {
  
  private static NormalDistribution standardNormalDistribution = new NormalDistribution( 0.0, 1.0 );
  
  public static double[][] getDensities( int numQuads ) {
    double doubleNum = (new Integer(numQuads)).doubleValue();
    double inc = 8.0d / (doubleNum - 1.0d);
    double [][] retVal = new double[numQuads][];
    double point = -4.0d;
    for( int i = 0; i < numQuads; i++ ) {
      retVal[i] = new double [2];
      retVal[i][0] = Precision.round(point,7);
      retVal[i][1] = Precision.round(standardNormalDistribution.density( point ), 10);
      point += inc;
    }

    return retVal;
  }

  
  public static void main( String [] args ) {
    int numQuads = 30;

    if( args.length == 1 ) {
      try {
        numQuads = Integer.parseInt( args[0] );
      } catch( Exception e ) {
        numQuads = 30;
      }
    }

    NumberFormat nf = NumberFormat.getInstance();
    nf.setMinimumFractionDigits(10);
    nf.setMaximumFractionDigits(10);
    double [][] quads = DensityFactory.getDensities( numQuads );
    System.out.println("{");
    for( int i = 0; i < quads.length; i++ ) {
      System.out.println( "{" + quads[i][0] + "," + nf.format(quads[i][1]) + "}" );
    }
    System.out.println("}");
  }
}
