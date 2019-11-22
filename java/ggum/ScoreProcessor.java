
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.analysis.function.Exp;
import org.apache.commons.math3.linear.RealMatrix;

/**
 * See : https://rdrr.io/cran/ScoreGGUM/src/R/score.GGUM.R
 * @author mmammel
 *
 */
public class ScoreProcessor {
  
  private static final int DEFAULT_QUADRATURES = 30;
  private static final Exp EXP = new Exp();
   
  private List<ItemDescriptor> items;
  private RealMatrix quadratures;
   
  public ScoreProcessor( List<ItemDescriptor> items ) {
    this( items, DEFAULT_QUADRATURES );
  }

  public ScoreProcessor( List<ItemDescriptor> items, int numQuadratures ) {
    this.items = items;
    this.quadratures = DensityFactory.getDensities(numQuadratures);
  }

  /**
   * Get the score/error for this response vector.
   * @param response
   * @return
   */
  public ThetaEstimate process( ResponseDescriptor response ) {
    ThetaEstimate retVal = this.getEAP(response);
    return retVal;
  }
  
  /**
   * Process a list of responses and get the scores/errors
   * @param responses
   * @return
   */
  public List<ThetaEstimate> process(List<ResponseDescriptor> responses ) {
    List<ThetaEstimate> estimates = new ArrayList<ThetaEstimate>();
    ThetaEstimate temp = null;
    if( responses != null ) {
      for( ResponseDescriptor response : responses ) {
         if( (temp = this.process(response)) != null ) {
           estimates.add( temp );   
         }
      }
    }
    
    return estimates;
  }
  
  /**
   * Right now this assumes the data lines up with the num items etc.
   * Need to validate in process()
   * @param response
   * @return
   */
  private ThetaEstimate getEAP( ResponseDescriptor response ) {
    ThetaEstimate retVal = new ThetaEstimate();
    double [][] eap_data = new double [ this.quadratures.getRowDimension() ][];
    double [] numerators = null, denominators = null, probs = null;
    double denomSum = 0.0d, like = 0.0d;
    double [] quad = null;
    ItemDescriptor item = null;
    for( int q = 0; q < this.quadratures.getRowDimension(); q++ ) {
      eap_data[q] = new double [5];
      quad = this.quadratures.getRow(q);
      numerators = new double[ this.items.size() ];
      denominators = new double[ this.items.size() ];
      probs = new double[ this.items.size() ];
      
      for( int i = 0; i < this.items.size(); i++ ) {
        item = this.items.get(i);
        if( response.getResponse(i) != ResponseDescriptor.NA ) {
          numerators[i] = EXP.value(item.getAlpha() * (response.getZeroResponse(i) * ( quad[0] - item.getDelta() ) - item.getTauSum(response.getZeroResponse(i)) ) ) +
                          EXP.value(item.getAlpha() * ((item.getM() - response.getZeroResponse(i)) * (quad[0] - item.getDelta() ) - item.getTauSum(response.getZeroResponse(i)) ) );
        } else {
          numerators[i] = Double.NaN;
        }
      }
      // we have all of the item num vals for this quad, get the denominators.
      for( int i = 0; i < this.items.size(); i++ ) {
        item = this.items.get(i);
        denomSum = 0.0d;
        
        for( int w = 0; w < item.getNumCategories(); w++ ) {
          denomSum += EXP.value(item.getAlpha() * (w * (quad[0] - item.getDelta()) - item.getTauSum(w) ) ) +
                      EXP.value(item.getAlpha() * ((item.getM() - w)*(quad[0] - item.getDelta()) - item.getTauSum(w) ) );
        }
        
        denominators[i] = denomSum;
        // we have num and den for i, do prob
        probs[i] = Double.isNaN(numerators[i]) ? Double.NaN : numerators[i] / denominators[i];
      }
      
      like = this.mult(probs);
      eap_data[q][0] = quad[0] * like * quad[1]; // EAP_num
      eap_data[q][1] = like * quad[1];           // EAP_den
      eap_data[q][2] = like;                     // like
      eap_data[q][3] = quad[0];                  // point
      eap_data[q][4] = quad[1];                  // density
    }
    
    retVal.setEstimate(this.getThetaFromEAP(eap_data));
    retVal.setError(this.getErrorFromEAP(eap_data, retVal.getEstimate()));
    retVal.setSubjectId(response.getSubjectId());
    
    return retVal;
  }
  
  /**
   * Calculate the error 
   * @param eap
   * @param theta_j
   * @return
   */
  private double getErrorFromEAP( double [][] eap, double theta_j ) {
    double [] std_nums = new double[ eap.length ];
    double [] std_dens = new double[ eap.length ];
    
    for( int i = 0; i < eap.length; i++ ) {
      // EAP[,"std_num"] <- ((EAP[,"point"] - theta_j)^2)*EAP[,"like"]*EAP[,"density"] 
      std_nums[i] = Math.pow(eap[i][3] - theta_j, 2.0d) * eap[i][2] * eap[i][4];
      // EAP[,"std_den"] <- EAP[,"like"]*EAP[,"density"]
      std_dens[i] = eap[i][2] * eap[i][4];
    }
    
    // std_j <- sqrt(sum(EAP[,"std_num"])/sum(EAP[,"std_den"]))     
    double retVal = Math.sqrt( this.sum(std_nums) / this.sum(std_dens) );
    
    return retVal;
  }
  
  /**
   * Get theta_j
   * @param eap
   * @return
   */
  private double getThetaFromEAP( double [][] eap ) {
    double numSum = 0.0d;
    double denSum = 0.0d;
    
    for( int i = 0; i < eap.length; i++ ) {
      numSum += eap[i][0];
      denSum += eap[i][1];
    }
    
    return numSum/denSum;
  }
  
  /**
   * Utility to multiply an array of doubles
   * @param array
   * @return
   */
  private double mult( double [] array ) {
    double retVal = 1.0d;
    for( int i = 0; i < array.length; i++ ) {
      if( !Double.isNaN(array[i]) ) {
        retVal *= array[i];
      }
    }
    
    return retVal;
  }
  
  /**
   * Utility to sum an array of doubles.
   * @param array
   * @return
   */
  private double sum( double [] array ) {
    double retVal = 0.0d;
    for( int i = 0; i < array.length; i++ ) {
      if( !Double.isNaN(array[i]) ) {
        retVal += array[i];
      }
    }
    
    return retVal;
  }
  
  public List<ItemDescriptor> getItems() {
    return items;
  }

  public void setItems(List<ItemDescriptor> items) {
    this.items = items;
  }

  public int getNumQuadratures() {
    return this.quadratures.getRowDimension();
  }
}

