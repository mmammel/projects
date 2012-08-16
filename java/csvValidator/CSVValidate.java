import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

public class CSVValidate extends AbstractValidator
{
  public CSVValidate( Properties properties )
  {
    super(properties);
  }

  public String [] getFieldsFromRow( String row )
  {
    return row.split(",");
  }

}
