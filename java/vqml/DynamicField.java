import java.util.Set;

public interface DynamicField
{
  public String getName();
  public String getLabel();
  public void setName(String name);
  public void setLabel(String label);
  public Set<String> getFieldRefs(); // return a list of field names that this field depends on.
  public String renderHtml(VQMLContext context, boolean submitOnChange) throws RuntimeException;
}


