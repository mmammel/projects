import java.util.ArrayList;
import java.util.List;

public class ResponseDescriptor {
  // used to filter out missing responses.
  public static final int NA = -1;
  
  private String subjectId;
  
  // responses are "1" based, i.e. first option is 1, second is 2, etc.
  private List<Integer> responses;
  
  public ResponseDescriptor() {
    this.responses = new ArrayList<Integer>();
  }
  
  public ResponseDescriptor( String subjectID ) {
    this();
    this.setSubjectId(subjectID);
  }
  
  public String getSubjectId() {
    return subjectId;
  }

  public void setSubjectId(String subjectId) {
    this.subjectId = subjectId;
  }

  public List<Integer> getResponses() {
    return responses;
  }

  public void setResponses(List<Integer> responses) {
    this.responses = responses;
  }
  
  public void addResponse( int val ) {
    if( this.responses == null ) {
      this.responses = new ArrayList<Integer>();
    }
    
    this.responses.add(val);
  }
  
  public int getResponse( int idx ) {
    int retVal = NA;
    if( this.responses != null && idx >= 0 && idx < this.responses.size() ) {
      retVal = this.responses.get(idx);
    }
    
    return retVal;
  }
  
  // shortcut to get the 0-based response num.
  public int getZeroResponse( int idx ) {
    int retVal = this.getResponse(idx);
    if( retVal > 0 ) {
      retVal--;
    } else {
      retVal = 0; // NA just goes to 0
    }
    return retVal;
  }
}

