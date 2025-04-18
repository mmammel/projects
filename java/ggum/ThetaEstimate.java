public class ThetaEstimate {
  private String subjectId;
  private double estimate;
  private double error;
  
  public String getSubjectId() {
    return subjectId;
  }
  public void setSubjectId(String subjectId) {
    this.subjectId = subjectId;
  }
  public double getEstimate() {
    return estimate;
  }
  public void setEstimate(double estimate) {
    this.estimate = estimate;
  }
  public double getError() {
    return error;
  }
  public void setError(double error) {
    this.error = error;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append( " [" ).append( this.subjectId ).append( " | " ).append( this.estimate ).append( " | " ).append( this.error ).append( " ]" );
    return sb.toString();
  }
}
