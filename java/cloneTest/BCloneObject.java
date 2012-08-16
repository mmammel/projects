
public class BCloneObject extends BaseCloneObject
{
  private String B_Attribute = null;
  private int mSerialNum = -1;

  public BCloneObject(){ }

  public BCloneObject(String n, int serial)
  {
    mName = n;
    mSerialNum = serial;
    B_Attribute = "B_"+n;
  }

  public String getName()
  {
    return mName + "_" + B_Attribute + "["+mSerialNum+"]";
  }

  public ICloneObject getCopy()
  {
    ICloneObject copy = new BCloneObject( "BCOPY_" + mName, mSerialNum );
    ICloneObject tempParent = null;

    if( mParent != null )
    {
      tempParent = this.getParent().getCopy();
      tempParent.addChild(copy);
      copy.setParent( tempParent );
    }

    return copy;
  }

}
