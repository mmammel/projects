public class ResponseDescriptor {
  // used to filter out missing responses.
  public static final int NA = -1;
  
  private String subjectId;
  
  // responses are "1" based, i.e. first option is 1, second is 2, etc.
  private int [] responses;
  
  public String getSubjectId() {
    return subjectId;
  }

  public void setSubjectId(String subjectId) {
    this.subjectId = subjectId;
  }

  public int[] getResponses() {
    return responses;
  }

  public void setResponses(int[] responses) {
    this.responses = responses;
  }
  
  public int getResponse( int idx ) {
    int retVal = NA;
    if( this.responses != null && idx >= 0 && idx < this.responses.length ) {
      retVal = this.responses[idx];
    }
    
    return retVal;
  }
  
  // shortcut to get the 0-based response num.
  public int getZeroResponse( int idx ) {
    int retVal = this.getResponse(idx);
    if( retVal > 0 ) retVal--;
    return retVal;
  }
}
