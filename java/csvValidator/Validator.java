import java.util.List;

public interface Validator
{
  public List<RowResult> validateFile( String fileName );
}