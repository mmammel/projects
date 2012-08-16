import java.util.List;
import java.util.ArrayList;

public abstract class BaseCloneObject implements ICloneObject
{
  protected String mName = null;
  protected List mChildren = new ArrayList();
  protected ICloneObject mParent = null;

  public abstract String getName();

  public abstract ICloneObject getCopy();

  public void setName(String name)
  {
    mName = name;
  }

  public ICloneObject getParent()
  {
    return mParent;
  }

  public void setParent(ICloneObject o)
  {
    mParent = o;
  }

  public void addChild(ICloneObject o)
  {
    ICloneObject temp = o;

    while( temp.getParent() != null ) temp = temp.getParent();

    temp.setParent(this);
    mChildren.add(temp);
  }

  public ICloneObject getChild(int idx)
  {
    return (ICloneObject)mChildren.get(idx);
  }

  public int getChildCount()
  {
    return mChildren.size();
  }

}

