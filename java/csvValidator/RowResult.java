import java.util.List;
import java.util.ArrayList;

public class RowResult
{
  private String csvRow;
  private List<String> errors = new ArrayList<String>();
  private int rowNum = -1;

  public String getCsvRow()
  {
    return this.csvRow;
  }

  public void setCsvRow( String row )
  {
    this.csvRow = row;
  }

  public void addError( String error )
  {
    this.errors.add( error );
  }

  public List<String> getErrors()
  {
    return this.errors;
  }

  public int getRowNum()
  {
    return this.rowNum;
  }

  public void setRowNum( int num )
  {
    this.rowNum = num;
  }
}