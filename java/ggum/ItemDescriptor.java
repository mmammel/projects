public class ItemDescriptor {
  public static final int MAX_CATS = 10;
  
  private String id;
  private int numCategories; // max. 10
  private double initial;
  private double dstd;
  private double astd;
  private double delta; // position on the continuum
  private double alpha; // discrimination
  private double [] tau; // max. 10
  
  public ItemDescriptor() {
    this.tau = new double [ MAX_CATS ];
  }
  
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public int getNumCategories() {
    return numCategories;
  }
  public void setNumCategories(int numCategories) {
    this.numCategories = numCategories;
  }
  public double getInitial() {
    return initial;
  }
  public void setInitial(double initial) {
    this.initial = initial;
  }
  public double getDstd() {
    return dstd;
  }
  public void setDstd(double dstd) {
    this.dstd = dstd;
  }
  public double getAstd() {
    return astd;
  }
  public void setAstd(double astd) {
    this.astd = astd;
  }
  public double getDelta() {
    return delta;
  }
  public void setDelta(double delta) {
    this.delta = delta;
  }
  public double getAlpha() {
    return alpha;
  }
  public void setAlpha(double alpha) {
    this.alpha = alpha;
  }
  
  public int getM() {
    return this.numCategories * 2 - 1;
  }
  
  public double getTauSum() {
    return this.getTauSum( this.numCategories - 1 );
  }
  
  public double getTauSum(int to) {
    double retVal = 0.0d;
    if( this.tau != null && this.tau.length >= this.numCategories ) {
      for( int i = 0; i <= to; i++ ) {
        retVal += this.tau[i];
      }
    }
    
    return retVal;
  }
  
  public double getTau( int idx ) {
    return this.tau != null && idx >= 0 && idx < MAX_CATS ? this.tau[idx] : 0.0d;
  }
  
  public double[] getTau() {
    return tau;
  }
  public void setTau(double[] tau) {
    this.tau = tau;
  }
}
