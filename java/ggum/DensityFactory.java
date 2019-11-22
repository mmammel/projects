import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealMatrixFormat;
import org.apache.commons.math3.linear.MatrixUtils;
import java.text.NumberFormat;

public class DensityFactory {
  
  private static NormalDistribution standardNormalDistribution = new NormalDistribution( 0.0, 1.0 );
  
  public static RealMatrix getDensities( int numQuads ) {
    double inc = 8.0d / (numQuads - 1);
    RealMatrix retVal = MatrixUtils.createRealMatrix( numQuads, 2 );
    double point = -4.0d;
    for( int i = 0; i < numQuads; i++ ) {
      retVal.setEntry( i, 0, point );
      retVal.setEntry( i, 1, standardNormalDistribution.density( point ) );
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

    NumberFormat nf = NumberFormat.getNumberInstance();
    nf.setMinimumFractionDigits(8);
    nf.setMaximumFractionDigits(8);
    RealMatrix quads = DensityFactory.getDensities( numQuads );
    RealMatrixFormat format = new RealMatrixFormat("{","}","{","}","\n",",", nf);
    System.out.println( format.format(quads) );
  }
}
