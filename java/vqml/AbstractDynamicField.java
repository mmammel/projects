import java.util.HashSet;
import java.util.Set;

/**
 * This was originally meant to hold more, but it is a nice container for
 * the setters and getters.
 *
 * @author mammelma
 *
 */
public abstract class AbstractDynamicField implements DynamicField
{
  protected String name = null;
  protected String label = null;

  public AbstractDynamicField( String name, String label )
  {
     this.name = name;
     this.label = label;
  }

  public void setName( String name )
  {
    this.name = name;
  }

  public void setLabel( String label )
  {
    this.label = label;
  }

  public String getName()
  {
    return this.name;
  }

  public String getLabel()
  {
    return this.label;
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("[Name:").append(this.name).append("][Label:").append(this.label).append("]");
    return sb.toString();
  }

  public Set<String> getFieldRefs()
  {
    return new HashSet<String>();
  }

  // Render the html.
  public abstract String renderHtml(VQMLContext context, boolean submitOnChange) throws RuntimeException;
}
