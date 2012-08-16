public interface ICloneObject
{
  public String getName();
  public void setName(String name);
  public ICloneObject getParent();
  public void setParent(ICloneObject o);
  public void addChild(ICloneObject o);
  public ICloneObject getChild(int idx);
  public int getChildCount();
  public ICloneObject getCopy();
}

