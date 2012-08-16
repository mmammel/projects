
public class CCloneObject extends BaseCloneObject
{
  private String C_Attribute = null;
  private int mSerialNum = -1;

  public CCloneObject(){ }

  public CCloneObject(String n, int serial)
  {
    mName = n;
    mSerialNum = serial;
    C_Attribute = "C_" + n;
  }

  public String getName()
  {
    return mName + "_" + C_Attribute + "["+mSerialNum+"]";
  }

  public ICloneObject getCopy()
  {
    ICloneObject copy = new CCloneObject( "CCOPY_" + mName, mSerialNum );
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
